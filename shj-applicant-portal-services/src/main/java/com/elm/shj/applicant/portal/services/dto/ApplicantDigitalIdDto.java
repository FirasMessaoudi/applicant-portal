/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant digital id domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Builder
@AllArgsConstructor
@Data
public class ApplicantDigitalIdDto implements Serializable {

    private static final long serialVersionUID = -1067949632316606990L;

    public ApplicantDigitalIdDto() {
    }

    private long id;
    private String uin;
    @JsonBackReference
    private ApplicantDto applicant;
    private Date creationDate;
}
