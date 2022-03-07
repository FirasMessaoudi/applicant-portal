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
    private long id;
    private String digitalId;
    private String contactDigitalId;
    private String contactFullNameAr;
    private String contactFullNameEn;
    private long typeId;
    private String alias;
    private String avatar;
    private boolean systemDefined;
    private String staffTitleCode;
    private String relationshipCode;
    private String mobileNumber;
    private String countryPhonePrefix;
    private String countryCode;
    private boolean autoAdded;
    private Long applicantRitualId;
    private Date creationDate;
    private Date updateDate;
    private String statusCode;
    private boolean deleted;
    private String messageText;
    private Date sentDate;
    private Date receivedDate;
    private Date readDate;
    private long unreadMessagesCount;
}