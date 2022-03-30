/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.survey.UserSurveyService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for exposing web services related to survey for external party.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_SURVEY)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SurveyWsController {

    private final UserSurveyService userSurveyService;


    @GetMapping("/find-survey/{surveyType}")
    public ResponseEntity<WsResponse<?>> findSurveyByDigitalIdAndSurveyType(@PathVariable("surveyType") String surveyType, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse wsResponse = userSurveyService.findSurveyByDigitalIdAndSurveyType(loggedInUserUin, surveyType);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }

    @PostMapping(value = "/submit-survey")
    public ResponseEntity<WsResponse<?>> createUserSurvey(@RequestBody SurveyFormDto surveyFormDto, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        UserSurveyDto userSurveyDto=new UserSurveyDto();
        userSurveyDto.setSurveyType(surveyFormDto.getSurveyType());
        userSurveyDto.setDigitalId(loggedInUserUin);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("userSurvey", userSurveyDto);
        builder.part("userSurveyQuestions", surveyFormDto.getUserSurveyQuestions());
        WsResponse response = userSurveyService.submitUserSurvey(builder);
        return ResponseEntity.ok(
                WsResponse.builder().status(response.getStatus())
                        .body(response.getBody()).build());
    }
    @GetMapping("/findRating/{userSurveyId}")
    public ResponseEntity<WsResponse<?>> findQuestionRatingByUserSurveyId(@PathVariable("userSurveyId") long  userSurveyId)
    {
        WsResponse wsResponse = userSurveyService.findQuestionRatingByUserSurveyId(userSurveyId);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());

    }


}
