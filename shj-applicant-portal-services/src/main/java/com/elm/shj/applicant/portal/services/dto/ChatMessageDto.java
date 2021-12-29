/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant chat message domain.
 *
 * @author Slim Ben Hadj
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto implements Serializable {

    private static final long serialVersionUID = 4745158769022455992L;

    private long id;
    private String text;
    private ChatMessageTypeLookupDto type;
    private String contentFilePath;
    private ApplicantChatContactLiteDto sender;
    private ApplicantChatContactLiteDto receiver;
    private Date sentDate;
    private long sentDateTimestamp;
    private Date receivedDate;
    private Date readDate;
    private Boolean deleted;
    private Date creationDate;
    private Date updateDate;
}
