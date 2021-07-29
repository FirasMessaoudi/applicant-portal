/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.1.0
 */
@NoArgsConstructor
@Data
public class ApplicantLiteDto implements Serializable {

    private static final long serialVersionUID = 2708176152525069666L;

    private Date dateOfBirthGregorian;
    private Long dateOfBirthHijri;
    private String fullNameAr;
    private String fullNameEn;
    private String fullNameOrigin;
    private String email;
    private String mobileNumber;
}
