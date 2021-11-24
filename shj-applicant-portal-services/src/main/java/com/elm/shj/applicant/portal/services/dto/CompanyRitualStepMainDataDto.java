/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the company ritual step main data
 *
 * @author salzoubi
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class CompanyRitualStepMainDataDto implements Serializable {


    private static final long serialVersionUID = -7269254967366204060L;
    private long id;

    private String referenceNumber;

    private String transportationTypeCode;

    private long stepIndex;

    private String stepCode;

    private Date time;

    private double locationLat;

    private double locationLng;

    private String locationNameAr;

    private String locationNameEn;

    private Date creationDate;

    private Date updateDate;
}
