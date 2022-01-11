/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Web service operation result
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WsResponse<T> implements Serializable {

    private static final long serialVersionUID = -8577435158782277864L;

    public enum EWsResponseStatus {
        SUCCESS(0), FAILURE(1);

        int code;

        EWsResponseStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    private int status = EWsResponseStatus.SUCCESS.getCode();
    private T body;
}
