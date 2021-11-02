/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the applicant domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantDto implements Serializable {

    private static final long serialVersionUID = 4276580006724069703L;

    private long id;
    private String gender;
    private String nationalityCode;
    private String idNumber;
    private String idNumberOriginal;
    private String passportNumber;
    private Date dateOfBirthGregorian;
    private Long dateOfBirthHijri;
    private String fullNameAr;
    private String fullNameEn;
    private String fullNameOrigin;
    private String maritalStatusCode;
    private String photo;
    private String biometricDataFinger;
    private String biometricDataFace;
    private String educationLevelCode;
    private List<ApplicantDigitalIdDto> digitalIds;
    private List<ApplicantRelativeDto> relatives;
    @JsonBackReference
    private List<ApplicantRitualDto> rituals;
    private List<ApplicantContactDto> contacts;
    private ApplicantHealthDto applicantHealth;
    private Date creationDate;
    private Date updateDate;
}
