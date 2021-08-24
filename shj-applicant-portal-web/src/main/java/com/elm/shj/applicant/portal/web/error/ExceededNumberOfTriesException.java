package com.elm.shj.applicant.portal.web.error;

/**
 * Custom exception for Exceeded Number Of Tries.
 *
 * @author Ahmed Elsayed
 * @since 1.0.0
 */
public class ExceededNumberOfTriesException extends RuntimeException {

    public ExceededNumberOfTriesException() {
    }

    /**
     * Constructor with message
     *
     * @param message
     */
    public ExceededNumberOfTriesException(String message) {
        super(message);
    }
}
