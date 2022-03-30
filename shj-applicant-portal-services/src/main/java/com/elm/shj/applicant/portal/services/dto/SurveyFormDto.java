package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SurveyFormDto {
    private String surveyType;
    //TODO:
    private List<UserSurveyQuestionDto>  userSurveyQuestions;

}
