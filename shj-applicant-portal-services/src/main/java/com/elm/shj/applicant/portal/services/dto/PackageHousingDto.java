/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the Package Housing  .
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageHousingDto implements Serializable {


    private static final long serialVersionUID = -2333926062779667053L;
    private long id;
    private String typeCode;
    private String siteCode;
    private RitualPackageDto ritualPackage;
    private HousingZoneDto housingZone;
    private String referenceNumber;
    private String categoryCode;
    private String locationNameAr;
    private String locationNameEn;
    private Date validityStart;
    private Date validityEnd;
    private String addressEn;
    private String addressAr;
    private boolean isDefault;
    private String lat;
    private String lng;
    @JsonBackReference("packageCatering")
    private List<PackageCateringDto> packageCatering;
    @JsonBackReference("applicantPackageHousing")
    private List<ApplicantPackageHousingDto> applicantPackageHousing;
    private Date creationDate;
    private Date updateDate;
}
