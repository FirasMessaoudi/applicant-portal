/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Dto class for the applicant ritual domain.
 *
 * @author ahmad elsayed
 * @since 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantRitualLiteDto implements Serializable {


    private static final long serialVersionUID = -2590187906013696505L;

    private long id;

    private int hijriSeason;

    private Long dateStartHijri;

    private Long dateEndHijri;

    private String typeCode;

}
