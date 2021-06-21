/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.error;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Error model for all API calls
 *
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
public class ApiErrorResponse {

    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

    public ApiErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
}
