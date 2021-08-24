package com.elm.shj.applicant.portal.web.error;

/**
 * Custom exception for user not found in db.
 *
 * @author Ahmed Elsayed
 * @since 1.0.0
 */
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
    }

    /**
     * Constructor with message
     *
     * @param message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
