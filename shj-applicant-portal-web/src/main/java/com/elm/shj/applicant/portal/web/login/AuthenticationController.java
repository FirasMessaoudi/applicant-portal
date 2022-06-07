/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.login;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.shj.applicant.portal.services.dto.ApplicantLoginCmd;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * The controller handling the authentication operations
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_AUTH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    public static final int INVALID_RECAPTCHA_RESPONSE_CODE = 555;
    private static final int USER_IS_NOT_ACTIVE_RESPONSE_CODE = 557;
    private static final int USER_ALREADY_LOGGED_IN_RESPONSE_CODE = 556;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final OtpAuthenticationProvider otpAuthenticationProvider;
    private final JwtTokenService jwtTokenService;

    @Value("${login.simultaneous.enabled}")
    private boolean simultaneousLoginEnabled;

    /**
     * Authenticates the user and returns a freshly generated token
     *
     * @param credentials the user credentials
     * @return the generated token
     */
    @PostMapping("/login")
    public ResponseEntity<OtpToken> login(@RequestBody ApplicantLoginCmd credentials, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Login request handler");
        OtpToken authentication;
        try {
            authentication = (OtpToken) otpAuthenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials, credentials.getPassword()));

        } catch (RecaptchaException rex) {
            return ResponseEntity.status(INVALID_RECAPTCHA_RESPONSE_CODE).body(null);
        } catch (ResourceNotFoundException rex) {
            return ResponseEntity.status(INVALID_RECAPTCHA_RESPONSE_CODE).body(null);
        } catch (DeactivatedUserException due) {
            return ResponseEntity.status(USER_IS_NOT_ACTIVE_RESPONSE_CODE).body(null);
        } catch (UserAlreadyLoggedInException uaEX) {
            return ResponseEntity.status(USER_ALREADY_LOGGED_IN_RESPONSE_CODE).body(null);
        }

        return ResponseEntity.ok(authentication);
    }

    /**
     * Validates the otp sent by the user
     *
     * @return the generated token
     */
    @PostMapping("/otp")
    public ResponseEntity<JwtToken> otpForLogin(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        log.debug("OTP request handler");
        JwtToken authentication;
        try {
            authentication = (JwtToken) jwtAuthenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials.get("idNumber"), credentials.get("otp")));
        } catch (RecaptchaException rex) {
            return ResponseEntity.status(INVALID_RECAPTCHA_RESPONSE_CODE).body(null);
        } catch (ResourceNotFoundException rex) {
            return ResponseEntity.status(INVALID_RECAPTCHA_RESPONSE_CODE).body(null);
        }

        jwtTokenService.attachTokenCookie(response, authentication);
        jwtTokenService.refreshTokenExpiry(response, authentication.getToken());

        return ResponseEntity.ok(new JwtToken(null, ((UsernamePasswordAuthenticationToken) authentication.getPrincipal()).getName(),
                authentication.getAuthorities(), authentication.isPasswordExpired(), authentication.getFirstName(),
                authentication.getLastName(), authentication.getId(), authentication.getUserRoles(), authentication.getPreferredLanguage()));
    }


    /**
     * Invalidates the users token
     *
     * @param authentication the current user
     */
    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(Authentication authentication, HttpServletResponse response) {
        Assert.notNull(authentication, "User should be logged-in in order to logout");
        log.debug("Logout request handler");
        // if the authentication exists, then clear it
        if (!simultaneousLoginEnabled && authentication instanceof JwtToken) {
            jwtTokenService.invalidateToken(((JwtToken) authentication).getToken());
        }
        // clear security context anyway...
        authentication.setAuthenticated(false);
        SecurityContextHolder.clearContext();
        jwtTokenService.clearTokenCookies(response);
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
