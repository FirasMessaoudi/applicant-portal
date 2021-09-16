/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the  Ritual Package .
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RitualPackageDto implements Serializable {


    private static final long serialVersionUID = 811079397522626616L;
    private long id;
    @NotNull(message = "validation.data.constraints.msg.20001")
    private String typeCode;
    private float price;
    private String departureCity;
    private int countryId;


    @JsonBackReference("applicantPackages")
    private List<ApplicantPackageDTO> applicantPackages;
    @JsonBackReference("packageHousings")
    private List<PackageHousingDto> packageHousings;
    @JsonBackReference("packageTransportations")
    private List<PackageTransportationDto> packageTransportations;


    private Date creationDate;
    private Date updateDate;
}
