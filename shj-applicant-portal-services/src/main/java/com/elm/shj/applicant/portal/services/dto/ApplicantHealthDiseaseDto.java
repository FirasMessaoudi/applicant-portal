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
 * Dto class for the applicant health disease domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantHealthDiseaseDto implements Serializable {

    private static final long serialVersionUID = 5702724312215614750L;

    private long id;
    @JsonBackReference
    private ApplicantHealthDto applicantHealth;
    private String labelAr;
    private String labelEn;
    private Date creationDate;
    private Date updateDate;
}
