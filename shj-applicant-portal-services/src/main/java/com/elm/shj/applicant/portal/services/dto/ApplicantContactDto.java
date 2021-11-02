/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant contact domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantContactDto implements Serializable {


    private static final long serialVersionUID = -5662001048401642743L;

    private long id;
    @JsonBackReference
    private ApplicantDto applicant;
    private String languageList;
    private String email;
    private String localMobileNumber;
    private String intlMobileNumber;
    private String countryCode;
    private String streetName;
    private String districtName;
    private String cityName;
    private String buildingNumber;
    private String postalCode;
    private Date creationDate;
    private Date updateDate;
}
