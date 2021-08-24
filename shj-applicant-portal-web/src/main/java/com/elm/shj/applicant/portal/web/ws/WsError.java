/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import lombok.Builder;
import lombok.Data;

/**
 * Integration error
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Data
@Builder
public class WsError {

    private int error = EWsError.GENERIC.getCode();
    private String referenceNumber;
    public enum EWsError {
        GENERIC(100), APPLICANT_NOT_FOUND(101),
        APPLICANT_NOT_MATCHED(102), CARD_DETAILS_NOT_FOUND(103),
        ALREADY_REGISTERED(104), NOT_FOUND_IN_ADMIN(105),INVALID_OTP(106),
        BAD_CREDENTIALS(107),EXCEEDED_NUMBER_OF_TRIES(108);
        int code;

        EWsError(int code) {
            this.code = code;
        }

        int getCode() {
            return code;
        }
    }
}
