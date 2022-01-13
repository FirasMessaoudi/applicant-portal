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
        ALREADY_REGISTERED(104), NOT_FOUND_IN_ADMIN(105), INVALID_OTP(106),
        BAD_CREDENTIALS(107), EXCEEDED_NUMBER_OF_TRIES(108), PWRD_HISTORY_ERROR(109),
        PWRD_CONTAINS_USERNAME_ERROR(110), USER_IS_NOT_ACTIVE(111), USER_ALREADY_LOGGED_IN(112),
        UPDATE_USER_ERROR(113), USER_NOT_FOUND(114), INVALID_LOCATION_ENTRIES(115),
        APPLICANT_CHAT_CONTACT_NOT_FOUND(116), APPLICANT_CHAT_CONTACT_ALREADY_EXIST(117),
        APPLICANT_RITUAL_NOT_FOUND(118), INCIDENT_TYPE_NOT_FOUND(119),
        UPDATE_APPLICANT_FAILED(120), INVALID_COORDINATES(121), INVALID_FILE_EXTENSION(122),
        ExCEED_MAX_SIZE(123), COMPANY_STAFF_NOT_FOUND(124), COMPANY_STAFF_NOT_MATCHED(125),
        INVALID_INPUT(127);

        int code;

        EWsError(int code) {
            this.code = code;
        }

        int getCode() {
            return code;
        }
    }
}
