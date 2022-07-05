/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Dto class for the applicant ritual domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApplicantRitualBasicDto implements Serializable {

    private static final long serialVersionUID = 8699536906254699723L;

    private long id;

    private ApplicantBasicDto applicant;
}
