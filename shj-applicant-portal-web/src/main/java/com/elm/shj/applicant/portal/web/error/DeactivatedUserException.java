/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.error;

/**
 * Custom exception for deactivated existing users.
 *
 * @author ahmad elsayed
 * @since 1.0.0
 */
public class DeactivatedUserException extends RuntimeException {

    private static final long serialVersionUID = 4192517696852757272L;

    public DeactivatedUserException() {
        // empty
    }

    /**
     * Constructor with message
     * @param message
     */
    public DeactivatedUserException(String message) {
        super(message);
    }

    /**
     * Constructor with message and details
     * @param message the exception message
     * @param th the thrown exception
     */
    public DeactivatedUserException(String message, Throwable th) {
        super(message, th);
    }
}
