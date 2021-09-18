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
 * Dto class for applicant package transportation
 *
 * @author firas messaoudi
 * @since 1.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantPackageTransportationDto implements Serializable {
    private static final long serialVersionUID = -8137564418002673291L;

    private long id;
    private String seatNumber;
    private String wagonNumber;
    private String vehicleNumber;
    private Date creationDate;
    private Date updateDate;
    private ApplicantPackageDTO applicantPackage;
    private PackageTransportationDto packageTransportation;
}
