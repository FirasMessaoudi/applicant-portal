/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import lombok.Builder;
import lombok.Data;

/**
 * Web service operation result
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Data
@Builder
public class WsResponse<T> {

    public enum EWsResponseStatus {
        SUCCESS(0), FAILURE(1), ALREADY_REGISTERED(560), NOT_FOUND_IN_ADMIN(561),INVALID_OTP(562);

        int code;

        EWsResponseStatus(int code) {
            this.code = code;
        }

        int getCode() {
            return code;
        }
    }

    private EWsResponseStatus status = EWsResponseStatus.SUCCESS;
    private T body;
}
