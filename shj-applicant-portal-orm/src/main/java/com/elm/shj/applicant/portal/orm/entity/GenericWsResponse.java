package com.elm.shj.applicant.portal.orm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class GenericWsResponse<T> implements Serializable {

    private static final long serialVersionUID = -8577435158782277864L;

    public enum EWsResponseStatus {
        SUCCESS(0), FAILURE(1);

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
    public int getStatusCode(){
        return status.code;
    }
}
