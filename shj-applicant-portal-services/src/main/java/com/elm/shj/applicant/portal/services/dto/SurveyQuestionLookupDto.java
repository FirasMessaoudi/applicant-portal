/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the Survey Question Lookup domain.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestionLookupDto implements Serializable {

    private static final long serialVersionUID = -1783251010116861864L;
    private long id;

    private String code;

    private String lang;

    private String label;

    private Date creationDate;

    private SurveyTypeLookupDto surveyType;

}
