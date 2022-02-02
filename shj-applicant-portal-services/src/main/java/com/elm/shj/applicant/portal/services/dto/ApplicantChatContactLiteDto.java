/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
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

    private long id;
    private String applicantUin;
    private String contactUin;
    private String contactFullNameAr;
    private String contactFullNameEn;
    private Long typeId;
    @NotBlank(message = "validation.data.validation.field.required")
    private String alias;
    private String avatar;
    //TODO why using wrapper type
    private Boolean systemDefined;
    private String staffTitleCode;
    private String relationshipCode;
    private String mobileNumber;
    private String countryPhonePrefix;
    private String countryCode;
    private Boolean deleted;
    private Boolean autoAdded;
    private Date creationDate;
    private Date updateDate;
}
