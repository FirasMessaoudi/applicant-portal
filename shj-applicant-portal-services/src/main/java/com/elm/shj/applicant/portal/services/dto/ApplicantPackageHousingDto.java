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
 * Dto class for applicant package housing
 *
 * @author firas messaoudi
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantPackageHousingDto implements Serializable {
    private static final long serialVersionUID = 4195242056822545138L;

    private long id;
    private String roomNumber;
    private String bedNumber;
    private Date creationDate;
    private Date updateDate;
    private ApplicantPackageDto applicantPackage;
    private PackageHousingDto packageHousing;
}
