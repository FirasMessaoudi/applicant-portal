/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/**
 * Dto class for the Survey Question Lookup domain.
 *
 * @author r.chebbi
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class SurveyFormDto {
    private UserSurveyDto userSurvey;
    private List<UserSurveyQuestionDto>  userSurveyQuestions;
}
