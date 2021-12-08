/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Dto class for the applicant company staff lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyStaffLiteDto implements Serializable {

    private static final long serialVersionUID = 531849258704172988L;

    private String fullNameAr;
    private String fullNameEn;
    private String email;
    private String titleCode;
    private String mobileNumber;
    private String nationalityCode;
    private String photo;
}
