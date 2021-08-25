package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.web.error.ExceededNumberOfTriesException;
import com.elm.shj.applicant.portal.web.error.UserNotFoundException;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtAuthenticationProvider;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import com.elm.shj.applicant.portal.web.security.otp.OtpAuthenticationProvider;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.TOKEN_COOKIE_NAME},
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

    @Value("${login.simultaneous.enabled}")
    private boolean simultaneousLoginEnabled;

    /**
     * Authenticates the user and generate OTP
     *
     * @param credentials the user credentials
     * @return the generated token
     */
    @PostMapping("/login")
    public ResponseEntity<WsResponse<?>> login(@RequestBody Map<String, String> credentials) {
        log.debug("Login request handler");
        OtpToken authentication;
        String idNumber = credentials.get("idNumber");

        authentication = (OtpToken) otpAuthenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(idNumber, credentials.get("password")));


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
            jwtTokenService.invalidateToken(((JwtToken) authentication).getToken());
        }
        // clear security context anyway...
        authentication.setAuthenticated(false);
        SecurityContextHolder.clearContext();
        jwtTokenService.clearTokenCookies(response);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(true).build());

    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<WsResponse<?>> handleUserNotFoundException(
            UserNotFoundException ex) {
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

    @ExceptionHandler(ExceededNumberOfTriesException.class)
    public ResponseEntity<WsResponse<?>> handleExceededNumberOfTriesException(
            ExceededNumberOfTriesException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.EXCEEDED_NUMBER_OF_TRIES.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

}