/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.shj.applicant.portal.services.dto.SurveyFormDto;
import com.elm.shj.applicant.portal.services.dto.SurveyQuestionLookupDto;
import com.elm.shj.applicant.portal.services.dto.UserSurveyDto;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.survey.UserSurveyService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.ws.WsError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for survey.
 *
 * @author rameez imtiaz
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_SURVEY)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SurveyController {

    private final UserSurveyService userSurveyService;

    private static final Integer USER_NOT_FOUND = 114;
    private static final Integer SURVEY_ALREADY_SUBMITTED = 131;
    private static final Integer SURVEY_NOT_AVAILABLE = 133;
    private static final Integer INVALID_SURVEY_TYPE = 134;



    @GetMapping("/find-survey/{surveyType}")
    public ResponseEntity<?> findSurveyByDigitalIdAndSurveyType(@PathVariable("surveyType") String surveyType, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse<?> wsResponse = userSurveyService.findSurveyByDigitalIdAndSurveyType(loggedInUserUin, surveyType);

        if(wsResponse.getStatus() == SURVEY_ALREADY_SUBMITTED) {
            return ResponseEntity.ok(SURVEY_ALREADY_SUBMITTED);
        } else if(wsResponse.getStatus() == SURVEY_NOT_AVAILABLE){
            return ResponseEntity.ok(SURVEY_NOT_AVAILABLE);
        } else if(wsResponse.getStatus() == USER_NOT_FOUND){
            return ResponseEntity.ok(USER_NOT_FOUND);
        } else {
            List<SurveyQuestionLookupDto> surveyQuestionLookupList = (List<SurveyQuestionLookupDto>)wsResponse.getBody();
            return ResponseEntity.ok(surveyQuestionLookupList);
        }

    }

    @PostMapping(value = "/submit-survey")
    public ResponseEntity<?> createUserSurvey(@RequestBody SurveyFormDto surveyFormDto, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        surveyFormDto.getUserSurvey().setDigitalId(loggedInUserUin);
        WsResponse response = userSurveyService.submitUserSurvey(surveyFormDto);

        if(response.getStatus() ==USER_NOT_FOUND){
            return ResponseEntity.ok(USER_NOT_FOUND);
        } else if(response.getStatus() ==INVALID_SURVEY_TYPE) {
            return ResponseEntity.ok(INVALID_SURVEY_TYPE);
        } else if(response.getStatus() ==SURVEY_ALREADY_SUBMITTED){
            return ResponseEntity.ok(SURVEY_ALREADY_SUBMITTED);
        } else {
            UserSurveyDto userSurveyDto = (UserSurveyDto)response.getBody();
            return ResponseEntity.ok(userSurveyDto);
        }

    }
    @GetMapping("/findRating/{userSurveyId}")
    public ResponseEntity<List<Integer>> findQuestionRatingByUserSurveyId(@PathVariable("userSurveyId") long  userSurveyId)
    {
        WsResponse wsResponse = userSurveyService.findQuestionRatingByUserSurveyId(userSurveyId);
        return ResponseEntity.ok((List<Integer>) wsResponse.getBody());

    }


}
