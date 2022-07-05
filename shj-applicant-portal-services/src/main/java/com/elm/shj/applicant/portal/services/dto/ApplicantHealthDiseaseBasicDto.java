/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Dto class for the applicant health disease basic domain.
 *
 * @author f.messaoudi
 * @since 1.3.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantHealthDiseaseBasicDto implements Serializable {

    private static final long serialVersionUID = -6301360184421797666L;
    private long id;
    private String labelAr;
    private String labelEn;
    @JsonBackReference
    private ApplicantHealthBasicDto applicantHealth;

}
