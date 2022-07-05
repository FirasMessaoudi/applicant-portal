/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Dto class for the applicant health disease lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantHealthDiseaseLiteDto implements Serializable {

    private static final long serialVersionUID = -7681170162543116274L;
    private Long id;
    private String labelAr;
    private String labelEn;
}
