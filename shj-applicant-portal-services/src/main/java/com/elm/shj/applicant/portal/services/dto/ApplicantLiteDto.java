/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the applicant lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Setter
@Getter
public class ApplicantLiteDto implements Serializable {

    private static final long serialVersionUID = -1755136674160473725L;

    private Date dateOfBirthGregorian;
    private Long dateOfBirthHijri;
    private String fullNameAr;
    private String fullNameEn;
    private String fullNameOrigin;
    private String passportNumber;
    private String idNumber;
    private String email;
    private List<ApplicantDigitalIdDto> digitalIds;
    private boolean hasLocalMobileNumber;
    private String mobileNumber;
    private String countryCode;
    private String gender;
    private String nationalityCode;
}
