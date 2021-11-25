/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for applicant package catering
 *
 * @author firas messaoudi
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantPackageCateringDto implements Serializable {
    private static final long serialVersionUID = -1223255818582203064L;

    private long id;
    private String optionAr;
    private String optionEn;
    private Date creationDate;
    private Date updateDate;
    private ApplicantPackageDto applicantPackage;
    private PackageCateringDto packageCatering;

}
