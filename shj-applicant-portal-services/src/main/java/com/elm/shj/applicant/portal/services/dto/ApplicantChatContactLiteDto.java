/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant chat contact lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantChatContactLiteDto implements Serializable {

    private static final long serialVersionUID = -3274483957064433720L;

    private Long id;
    private String applicantUin;
    private String contactUin;
    private String contactFullNameAr;
    private String contactFullNameEn;
    private Long typeId;
    private String alias;
    private String avatar;
    private Boolean systemDefined;
    private String staffTitleCode;
    private String relationshipCode;
    private String mobileNumber;
    private Boolean deleted;
    private Date creationDate;
    private Date updateDate;
}