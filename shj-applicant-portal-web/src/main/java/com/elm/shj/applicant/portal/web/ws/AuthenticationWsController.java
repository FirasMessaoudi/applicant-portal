/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.shj.applicant.portal.services.dto.ApplicantLoginCmd;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.error.DeactivatedUserException;
import com.elm.shj.applicant.portal.web.error.UserAlreadyLoggedInException;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtAuthenticationProvider;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import com.elm.shj.applicant.portal.web.security.otp.OtpAuthenticationProvider;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Controller for exposing Authentication web services for external party.
 *
 * @author ahmad elsayed
 * @since 1.0.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_AUTH)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthenticationWsController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final OtpAuthenticationProvider otpAuthenticationProvider;
    private final JwtTokenService jwtTokenService;
    private final IntegrationService integrationService;
    private final UserService userService;
    @Value("${login.simultaneous.enabled}")
    private boolean simultaneousLoginEnabled;

    /**
     * Authenticates the user and generate OTP
     *
     * @param credentials the user credentials
     * @return the generated token
     */
    @PostMapping("/login")
    public ResponseEntity<WsResponse<?>> login(@RequestBody ApplicantLoginCmd credentials) {
        log.debug("Login request handler");
        OtpToken authentication;

        authentication = (OtpToken) otpAuthenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(credentials, credentials.getPassword()));


        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(authentication).build());

    }


    /**
     * Validates the otp sent by the user
     *
     * @return the generated token
     */
    @PostMapping("/otp")
    public ResponseEntity<WsResponse<?>> otpForLogin(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        log.debug("OTP request handler");
        JwtToken authentication = (JwtToken) jwtAuthenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.get("idNumber"), credentials.get("otp")));

        int refreshedTokenExpiryInMillis = jwtTokenService.refreshTokenExpiry(response, authentication.getToken());

        response.setHeader(JwtTokenService.JWT_HEADER_NAME, authentication.getToken());
        response.setIntHeader(JwtTokenService.JWT_EXPIRY_HEADER_NAME, refreshedTokenExpiryInMillis);


        JwtToken jwtToken = new JwtToken(null, ((UsernamePasswordAuthenticationToken) authentication.getPrincipal()).getName(),
                authentication.getAuthorities(), authentication.isPasswordExpired(), authentication.getFirstName(),
                authentication.getLastName(), authentication.getId(), authentication.getUserRoles());

        long loggedInUserUin = Long.parseLong(credentials.get("idNumber"));
        integrationService.updateLoggedInFlag(loggedInUserUin, true);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(jwtToken).build());

    }


    /**
     * Invalidates the users token
     *
     * @param authentication the current user
     */
    @PostMapping("/logout")
    public ResponseEntity<WsResponse<?>> logout(Authentication authentication, HttpServletResponse response) {
        Assert.notNull(authentication, "User should be logged-in in order to logout");
        log.debug("Logout request handler");
        // if the authentication exists, then clear it
        if (!simultaneousLoginEnabled && authentication instanceof JwtToken) {
            String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
            integrationService.updateLoggedInFlag(Long.parseLong(loggedInUserUin), false);
            jwtTokenService.invalidateToken(((JwtToken) authentication).getToken());
        }
        // clear security context anyway...
        authentication.setAuthenticated(false);
        SecurityContextHolder.clearContext();
        jwtTokenService.clearTokenCookies(response);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(true).build());

    }

    /**
     * Authenticate the requests from mobile app background services
     *
     * @param credentials
     */
    @PostMapping("/auth")
    public ResponseEntity<WsResponse<?>> authenticate(@RequestBody Map<String, String> credentials, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Auth Webservice request handler");

        String callerType = request.getHeader(JwtTokenService.CALLER_TYPE_HEADER_NAME);
        if (callerType == null || !callerType.equals(JwtTokenService.WEB_SERVICE_CALLER_TYPE)) {
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode()).body("accees denied").build());
        }

        JwtToken authentication;
        try {
            authentication = (JwtToken) jwtAuthenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password")));
        } catch (RecaptchaException rex) {
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode()).body("Multiple failed login attempts").build());
        }

        jwtTokenService.attachTokenCookie(response, authentication);

        return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(authentication.getToken()).build());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<WsResponse<?>> handleUserNotFoundException(
            ResourceNotFoundException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.APPLICANT_NOT_FOUND.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<WsResponse<?>> handleBadCredentialsException(
            BadCredentialsException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.BAD_CREDENTIALS.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

    @ExceptionHandler(RecaptchaException.class)
    public ResponseEntity<WsResponse<?>> handleRecaptchaException(
            RecaptchaException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.EXCEEDED_NUMBER_OF_TRIES.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

    @ExceptionHandler(DeactivatedUserException.class)
    public ResponseEntity<WsResponse<?>> handleDeactivatedUserException(
            DeactivatedUserException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.USER_IS_NOT_ACTIVE.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<WsResponse<?>> handleUserAlreadyLoggedInException(
            UserAlreadyLoggedInException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.USER_ALREADY_LOGGED_IN.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

    @PostMapping("/remove")
    public ResponseEntity<WsResponse<?>> removeAccount(Authentication authentication) {

        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        log.debug("removeAccount started ::: user uin: {}", loggedInUserUin);


        try {
            long uin=  Long.parseLong(loggedInUserUin);
            int affectedRows = userService.markAccountAsDeleted(uin, true);
            if (affectedRows == 1) {
                log.debug("removeAccount Finished And the account has been deleted::: user uin: {}", loggedInUserUin);
                return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(true).build());
            } else {
                log.debug("removeAccount Finished And the account has not been deleted::: user uin: {}", loggedInUserUin);
                return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode()).body(WsError.builder().error(WsError.EWsError.INVALID_INPUT.getCode()).build()).build());
            }
        } catch (RuntimeException e) {
            log.error("removeAccount Finished ::: user uin: {} And throw this exception ", loggedInUserUin, e);
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode()).body(WsError.builder().error(WsError.EWsError.GENERIC.getCode()).build()).build());
        }

    }

}
