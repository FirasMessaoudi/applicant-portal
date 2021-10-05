/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.jwt;

import com.elm.shj.applicant.portal.web.navigation.Navigation;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT Token Filter used for Spring Security authentication process
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
public class JwtTokenFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    public JwtTokenFilter(@Autowired AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        JwtToken userToken = null;

        boolean checkPasswordExpiryFlag = !request.getRequestURI().endsWith(Navigation.API_USERS_CHANGE_PWRD)
        &&!request.getRequestURI().endsWith(Navigation.API_INTEGRATION_USERS_CHANGE_PWRD)
                && !request.getRequestURI().endsWith(Navigation.API_AUTH_LOGOUT) && !request.getRequestURI().endsWith(Navigation.API_INTEGRATION_AUTH_LOGOUT);

        try {
            userToken = jwtTokenService.validateToken(request, checkPasswordExpiryFlag);
        } catch (JwtException je) {
            logger.error(je.getMessage(), je);
        }

        // set authentication, if token not valid it will be set to <code>null</code>
        SecurityContextHolder.getContext().setAuthentication(userToken);

        filterChain.doFilter(request, response);
    }
}
