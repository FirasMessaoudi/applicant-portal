/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for Lite applicant chat message domain.
 *
 * @author salzoubi
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageLiteDto implements Serializable {


    private static final long serialVersionUID = -8662150540665387521L;
    private long contactId;
    private String contactUin;
    private String messageText;
    private Date sentDate;
}