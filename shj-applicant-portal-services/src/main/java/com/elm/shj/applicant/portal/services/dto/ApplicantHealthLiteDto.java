/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Dto class for the applicant health lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantHealthLiteDto implements Serializable {

    private static final long serialVersionUID = -6152719368063316734L;

    private String bloodType;
    private Boolean hasSpecialNeeds;
    private String insurancePolicyNumber;
    private List<ApplicantHealthDiseaseLiteDto> diseases;
    private List<ApplicantHealthSpecialNeedsLiteDto> specialNeeds;
    private List<ApplicantHealthImmunizationLiteDto> immunizations;

}
