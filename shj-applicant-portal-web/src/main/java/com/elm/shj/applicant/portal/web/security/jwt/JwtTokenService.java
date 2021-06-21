/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.jwt;

import com.elm.shj.applicant.portal.services.dto.EChannel;
import com.elm.shj.applicant.portal.services.user.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

/**
 * JWT Service that provides token operations
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
@Component
public class JwtTokenService {

    public static final String TOKEN_COOKIE_NAME = "X-SEC-TK";
    public static final String TOKEN_EXPIRY_COOKIE_NAME = "X-SEC-TK-EXP";

    public static final String CALLER_TYPE_HEADER_NAME = "CALLER-TYPE";
    public static final String WEB_SERVICE_CALLER_TYPE = "WEB-SERVICE";

    private static final String ISSUER = "com.elm.dcc";
    private static final String AUTHORITIES_CLAIM_NAME = "authorities";
    private static final String USER_ID_CLAIM_NAME = "user.id";
    private static final String PWRD_EXPIRED_FLAG_CLAIM_NAME = "password.expired";
    private static final String USER_ROLE_IDS_CLAIM_NAME = "user.role.ids";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    @Value("${jwt.secret.key}")
    public String secretKey;

    @Value("${jwt.token.expires.in.seconds}")
    private int tokenExpiresIn;

    @Value("${jwt.token.mobile.expires.in.seconds}")
    private int tokenMobileExpiresIn;

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    public String generateToken(long idNumber, List<String> grantedAuthorities, long userId,
                                boolean passwordExpired, Set<Long> userRoleIds, HttpServletRequest request) {
        final Device device = DeviceUtils.getCurrentDevice(request);
        String audience = generateAudience(device).name().toLowerCase();
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_CLAIM_NAME, grantedAuthorities);
        claims.put(USER_ID_CLAIM_NAME, userId);
        claims.put(PWRD_EXPIRED_FLAG_CLAIM_NAME, passwordExpired);
        claims.put(USER_ROLE_IDS_CLAIM_NAME, userRoleIds);
        return Jwts.builder().setSubject(Long.toString(idNumber)).setExpiration(generateExpirationDate(device)).setIssuer(ISSUER)
                .setAudience(audience).signWith(SIGNATURE_ALGORITHM, secretKey).setIssuedAt(new Date())
                .addClaims(claims).compact();
    }

    public void invalidateToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        if (claims != null) {
            claims.setSubject(null);
            claims.clear();
        }
        // clear token from the system
        long idNumber = retrieveIdNumberFromToken(token).orElse(0l);
        userService.clearToken(idNumber);
    }

    public JwtToken validateToken(HttpServletRequest request, boolean checkPasswordExpiryFlag) {

        return
                // retrieve token
                retrieveToken(request).map(token ->
                        // retrieve username
                        retrieveIdNumberFromToken(token).map(idNumber -> {
                                    if (!userService.hasToken(idNumber)) {
                                        throw new ExpiredJwtException(null, null, "The token you provided has expired!");
                                    }
                                    // retrieve expiration date
                                    return retrieveExpirationDateFromToken(token).map(expirationDate -> {
                                        if (new Date().after(expirationDate)) {
                                            throw new ExpiredJwtException(null, null, "The token you provided has expired!");
                                        }
                                        // retrieve authorities
                                        // retrieve password expiry flag
                                        return retrieveGrantedAuthoritiesFromToken(token).flatMap(
                                                grantedAuthorities -> retrievePasswordExpirationFlagFromToken(token).map(passExpired -> {
                                            if (checkPasswordExpiryFlag && Boolean.TRUE.equals(passExpired)) {
                                                return null;
                                            }
                                            return new JwtToken(token, new User(Long.toString(idNumber), "<CONFIDENTIAL>",
                                                    grantedAuthorities), grantedAuthorities, Boolean.TRUE.equals(passExpired));
                                        })).orElse(null);
                                    }).orElseThrow(() -> new MalformedJwtException("The token you provided is malformed!"));
                                }
                        ).orElseThrow(() -> new MalformedJwtException("The token you provided is malformed!"))
                ).orElse(null);
    }

    /**
     * Gets the token from Authentication header
     * e.g Bearer your_token
     *
     * @return the extracted token
     */
    public Optional<String> retrieveToken(HttpServletRequest request) {

        String authHeader = request.getHeader(tokenHeader);
        // retrieve the token from header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        } else {
            // retrieve the token from the cookie if not available in header
            Cookie tokenCookie = WebUtils.getCookie(request, TOKEN_COOKIE_NAME);
            if (tokenCookie != null) {
                return Optional.ofNullable(tokenCookie.getValue());
            }
        }

        return Optional.empty();
    }

    /**
     * Gets user id number from the token
     *
     * @return the extracted idNumber
     */
    public Optional<Long> retrieveIdNumberFromToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        return claims == null ? Optional.empty() : Optional.of(Long.valueOf(claims.getSubject()));
    }

    /**
     * Gets the username from the token
     *
     * @return the extracted username
     */
    public Optional<List<GrantedAuthority>> retrieveGrantedAuthoritiesFromToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        if (Objects.isNull(claims)) {
            return Optional.empty();
        } else {
            List<String> authorities = claims.get(AUTHORITIES_CLAIM_NAME, List.class);
            return Optional.of(authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        }
    }

    /**
     * Gets user roles from the token
     * @param token
     * @return
     */
    public Optional<List<String>> retrieveAuthoritiesFromToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        return claims == null ? Optional.empty() : Optional.ofNullable(claims.get(AUTHORITIES_CLAIM_NAME, List.class));
    }

    /**
     * Gets the user id from the token
     *
     * @param token
     * @return the extracted user id
     */
    public Optional<Long> retrieveUserIdFromToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        return claims == null ? Optional.empty() : Optional.ofNullable(claims.get(USER_ID_CLAIM_NAME, Long.class));
    }

    /**
     * Gets the password expiration flag from the token
     *
     * @param token
     * @return the extracted password expiration flag
     */
    public Optional<Boolean> retrievePasswordExpirationFlagFromToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        return claims == null ? Optional.empty() : Optional.ofNullable(claims.get(PWRD_EXPIRED_FLAG_CLAIM_NAME,
                Boolean.class));
    }

    /**
     * Gets the expiration date from the token
     *
     * @return the extracted expiration date
     */
    public Optional<Date> retrieveExpirationDateFromToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        return claims == null ? Optional.empty() : Optional.ofNullable(claims.getExpiration());
    }

    /**
     * Gets the user role ids from the token
     * @param token
     * @return
     */
    public Optional<Set<Long>> retrieveUserRoleIdsFromToken(String token) {
        final Claims claims = this.retrieveAllClaimsFromToken(token);
        return claims == null ? Optional.empty() : Optional.ofNullable(new HashSet<>(claims.get(USER_ROLE_IDS_CLAIM_NAME, List.class)));
    }

    /**
     * refreshes the given token for the targeted device
     *
     * @param response       the response to render
     * @param refreshedToken the refreshed token
     * @return a refreshed token
     */
    public int refreshTokenExpiry(HttpServletResponse response, String refreshedToken) {
        // attach cookie to the response
        int tokenExpiryInMillis = 0;
        if (refreshedToken != null) {
            Instant now = LocalDateTime.now(UTC).toInstant(UTC);
            tokenExpiryInMillis = (int) Duration.between(now, retrieveExpirationDateFromToken(refreshedToken).orElse(Date.from(now)).toInstant()).toMillis();
            Cookie tokenExpiryCookie = new Cookie(TOKEN_EXPIRY_COOKIE_NAME, String.valueOf(tokenExpiryInMillis));
            tokenExpiryCookie.setHttpOnly(false);
            tokenExpiryCookie.setMaxAge(tokenExpiryInMillis / 1000);
            tokenExpiryCookie.setPath("/");
            response.addCookie(tokenExpiryCookie);
        }
        return tokenExpiryInMillis;
    }


    /**
     * refreshes the given token for the targeted device
     *
     * @param response       the response to render
     * @param authentication the authenticated user
     * @param device         the targeted device
     * @param attachCookie   if or not to attach the token cookie to the response
     * @return a refreshed token
     */
    public String refreshToken(HttpServletResponse response, Authentication authentication, Device device, boolean attachCookie) {

        String token = null;
        // check presence of token
        if (authentication != null && authentication.getPrincipal() != null
                && JwtToken.class.isInstance(authentication)) {
            JwtToken userToken = (JwtToken) authentication;
            token = userToken.getToken();
        } else {
            // if no token, then return null
            return null;
        }

        String refreshedToken = null;
        try {
            // refresh the token
            final Claims claims = this.retrieveAllClaimsFromToken(token);
            if (claims != null) {
                claims.setIssuedAt(Date.from(LocalDateTime.now(UTC).toInstant(UTC)));
                refreshedToken = Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate(device))
                        .signWith(SIGNATURE_ALGORITHM, secretKey).compact();
            }
        } catch (Exception e) {
            refreshedToken = null;
        }

        // attach cookie to the reponse
        if (refreshedToken != null && attachCookie) {
            attachTokenCookie(response, refreshedToken);
        }

        return refreshedToken;
    }

    /**
     * Attaches to the response a new cookie with the token
     *
     * @param response       the response to render
     * @param authentication the authenticated user
     */
    public void attachTokenCookie(HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() != null
                && JwtToken.class.isInstance(authentication)) {
            JwtToken userToken = (JwtToken) authentication;
            Instant now = LocalDateTime.now(UTC).toInstant(UTC);
            String token = userToken.getToken();
            long tokenExpirationDate = Duration.between(now, retrieveExpirationDateFromToken(token).orElse(Date.from(now)).toInstant()).toMillis();
            userToken.setTokenExpirationDate(tokenExpirationDate);
            attachTokenCookie(response, token);
        }
    }

    /**
     * Attaches to the response a new cookie with the token
     *
     * @param response the response to render
     * @param token    the current token to attach to the response
     */
    public void attachTokenCookie(HttpServletResponse response, String token) {
        Cookie tokenCookie = new Cookie(TOKEN_COOKIE_NAME, token);
        tokenCookie.setHttpOnly(true);
        Instant now = LocalDateTime.now(UTC).toInstant(UTC);
        tokenCookie.setMaxAge((int) Duration.between(now, retrieveExpirationDateFromToken(token).orElse(Date.from(now)).toInstant()).toMillis() / 1000);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
    }

    /**
     * clear from the response jwt cookies
     *
     * @param response the response to render
     */
    public void clearTokenCookies(HttpServletResponse response) {
        Cookie tokenCookie = new Cookie(TOKEN_COOKIE_NAME, null);
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
        tokenCookie = new Cookie(TOKEN_EXPIRY_COOKIE_NAME, null);
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
    }

    /**
     * Gets all claims from the give token
     *
     * @param token the given token
     * @return all claims in the token
     */
    private Claims retrieveAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * Assumes the audience for a given device
     *
     * @param device the given devices
     * @return the audience for the given device
     */
    private EChannel generateAudience(Device device) {
        if (device == null) return EChannel.UNKOWN;
        EChannel channel = EChannel.UNKOWN;
        if (device.isNormal()) {
            channel = EChannel.WEB;
        } else if (device.isTablet()) {
            channel = EChannel.TABLET;
        } else if (device.isMobile()) {
            channel = EChannel.MOBILE;
        }
        return channel;
    }

    /**
     * Assumes the expiration date for the given device
     *
     * @param device the given device
     * @return expiration date for the device
     */
    private Date generateExpirationDate(Device device) {
        long expiresIn = device != null && (device.isTablet() || device.isMobile()) ? tokenMobileExpiresIn : tokenExpiresIn;
        return Date.from(LocalDateTime.now(UTC).plusSeconds(expiresIn).toInstant(UTC));
    }

}
