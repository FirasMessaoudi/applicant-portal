package com.elm.shj.applicant.portal.web.error;


public class UserAlreadyLoggedInException extends RuntimeException {
    private static final long serialVersionUID = -1907077807691766459L;

    public UserAlreadyLoggedInException() {
    }

    public UserAlreadyLoggedInException(String message) {
        super(message);
    }

    public UserAlreadyLoggedInException(String message, Throwable th) {
        super(message, th);
    }
}