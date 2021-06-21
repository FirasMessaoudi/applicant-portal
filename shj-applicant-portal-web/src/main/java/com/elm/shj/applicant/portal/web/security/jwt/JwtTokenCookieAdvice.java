/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT Token Advice used to inject token cookies
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
@RestControllerAdvice
public class JwtTokenCookieAdvice implements ResponseBodyAdvice<Object> {

    private static final String JWT_HEADER_NAME = "X-SEC-TK";
    private static final String JWT_EXPIRY_HEADER_NAME = "X-SEC-EXP";

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverRequest, ServerHttpResponse serverResponse) {
        HttpServletRequest request = ((ServletServerHttpRequest) serverRequest).getServletRequest();
        HttpServletResponse response = ((ServletServerHttpResponse) serverResponse).getServletResponse();

        // if token is attached to the request, then refresh it and attach cookie to the response
        Device device = DeviceUtils.getCurrentDevice(request);
        String refreshedToken = jwtTokenService.refreshToken(response, SecurityContextHolder.getContext().getAuthentication(), device, true);

        // attach expiry date to the response to be used to handle idle time from client side
        int refreshedTokenExpiryInMillis = jwtTokenService.refreshTokenExpiry(response, refreshedToken);
        if (device != null && (device.isMobile() || device.isTablet())) {
            response.setHeader(JWT_HEADER_NAME, refreshedToken);
            response.setIntHeader(JWT_EXPIRY_HEADER_NAME, refreshedTokenExpiryInMillis);
        }
        return body;
    }
}
