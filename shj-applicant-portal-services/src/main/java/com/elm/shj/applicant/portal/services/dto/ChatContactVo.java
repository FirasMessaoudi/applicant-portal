/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Applicant chat contact value object.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
public class ChatContactVo implements Serializable {

    private static final long serialVersionUID = -8147938876661428572L;

    private String digitalId;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private String alias;

    @NotNull
    @Size(min = 5, max = 20)
    private String mobileNumber;

    @Size(min = 1, max = 10)
    private String countryPhonePrefix;

    @Size(min = 2, max = 5)
    private String countryCode;
}
