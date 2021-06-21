/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the applicant health domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantHealthDto implements Serializable {

    private static final long serialVersionUID = 2731031329221419001L;

    private long id;
    @JsonBackReference
    private ApplicantDto applicant;
    private String bloodType;
    private String insurancePolicyNumber;
    private Boolean hasSpecialNeeds;
    private Date creationDate;
    private Date updateDate;
    private List<ApplicantHealthDiseaseDto> diseases;
    private List<ApplicantHealthSpecialNeedsDto> specialNeeds;
    private List<ApplicantHealthImmunizationDto> immunizations;
}
