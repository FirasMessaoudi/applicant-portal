/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the User Survey Question domain.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSurveyQuestionDto implements Serializable {

    private static final long serialVersionUID = -246628788433966562L;
    private long id;

    private UserSurveyDto userSurvey;

    private SurveyQuestionLookupDto surveyQuestion;

    private String code;

    private Date creationDate;

}
