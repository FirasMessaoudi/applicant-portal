/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant health immunization domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantHealthImmunizationDto implements Serializable {

    private static final long serialVersionUID = -1939250710514581003L;

    private long id;
    @JsonBackReference
    private ApplicantHealthDto applicantHealth;
    private String immunizationCode;
    private Date immunizationDate;
    private boolean mandatory;
    private Date creationDate;
    private Date updateDate;
}
