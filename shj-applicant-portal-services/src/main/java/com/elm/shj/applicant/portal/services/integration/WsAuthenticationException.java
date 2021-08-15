/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

/**
 * Custom exception for integration web service authentication.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
public class WsAuthenticationException extends Exception {

    private static final long serialVersionUID = -3043905889300388247L;

    public WsAuthenticationException () {
        // no args constructor
    }

    public WsAuthenticationException(String message) {
        super(message);
    }

    public WsAuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
