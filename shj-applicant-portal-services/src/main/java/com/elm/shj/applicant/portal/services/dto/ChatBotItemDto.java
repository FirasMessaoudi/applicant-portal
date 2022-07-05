/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the chatbot  domain.
 *
 * @author rameez imtiaz
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatBotItemDto implements Serializable {

    private static final long serialVersionUID = 2108238859214254284L;

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
    private String parentCode;
}
