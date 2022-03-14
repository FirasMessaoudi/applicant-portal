/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the Survey Type Lookup domain.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyTypeLookupDto implements Serializable {

    private static final long serialVersionUID = -2514827291006513833L;
    private long id;
    private String code;
    private Date creationDate;

}
