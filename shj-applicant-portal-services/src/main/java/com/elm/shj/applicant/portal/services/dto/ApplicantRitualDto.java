/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant ritual domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantRitualDto implements Serializable {

    private static final long serialVersionUID = 8699536906254699723L;

    private long id;
    private ApplicantDto applicant;
    private String hamlahPackageCode;
    private String tafweejCode;
    private int hijriSeason;
    private Date dateStartGregorian;
    private Date dateEndGregorian;
    private Long dateStartHijri;
    private Long dateEndHijri;
    private String typeCode;
    private String visaNumber;
    private String permitNumber;
    private String insuranceNumber;
    private String borderNumber;
    private String zoneCode;
    private String groupCode;
    private String unitCode;
    private String busNumber;
    private String seatNumber;
    private Date creationDate;
    private Date updateDate;
}
