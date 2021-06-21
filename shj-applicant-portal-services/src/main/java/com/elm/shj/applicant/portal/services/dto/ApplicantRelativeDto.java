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
 * Dto class for the applicant relative domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantRelativeDto implements Serializable {

    private static final long serialVersionUID = 1502487798507783275L;

    private long id;
    private String relationshipCode;
    @JsonBackReference
    private ApplicantDto applicant;
    private ApplicantDto relativeApplicant;
    private Date creationDate;
    private Date updateDate;

}
