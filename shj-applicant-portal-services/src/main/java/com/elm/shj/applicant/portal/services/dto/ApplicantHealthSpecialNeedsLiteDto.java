/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Dto class for the applicant health special needs lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantHealthSpecialNeedsLiteDto implements Serializable {

    private static final long serialVersionUID = 5466437442562245759L;

    private String specialNeedTypeCode;
}
