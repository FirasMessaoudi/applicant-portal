/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.survey;

import com.elm.shj.applicant.portal.services.dto.SurveyFormDto;
import com.elm.shj.applicant.portal.services.dto.UserSurveyDto;
import com.elm.shj.applicant.portal.services.dto.UserSurveyQuestionDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling survey operations
 *
 * @author salzoubi
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserSurveyService {

    private final IntegrationService integrationService;

    public WsResponse findSurveyByDigitalIdAndSurveyType(String digitalId, String surveyType) {
        return integrationService.findSurveyByDigitalIdAndSurveyType(digitalId, surveyType);
    }
    public WsResponse submitUserSurvey(SurveyFormDto surveyFormDto) {
        return integrationService.submitUserSurvey(surveyFormDto);
    }
    public WsResponse findQuestionRatingByUserSurveyId(long userSurveyId ) {
        return integrationService.findQuestionRatingByUserSurveyId(userSurveyId);
    }

}
