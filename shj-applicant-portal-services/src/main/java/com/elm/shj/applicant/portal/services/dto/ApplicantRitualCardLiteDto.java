/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Dto class for the applicant ritual card lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantRitualCardLiteDto implements Serializable {

    private static final long serialVersionUID = -1461632558480493838L;

    private String hamlahPackageCode;
    private String tafweejCode;
    private String zoneCode;
    private String groupCode;
    private String unitCode;
    private String campCode;
    private String seatNumber;
    private String busNumber;
    private int hijriSeason;
    private String fullNameAr;
    private String fullNameEn;
    private String photo;
    private String leaderName;
    private String leaderMobile;
    private String nationalityCode;
}
