/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.chat.ChatContactService;
import com.elm.shj.applicant.portal.services.chat.ChatMessageService;
import com.elm.shj.applicant.portal.services.dto.ChatContactLiteDto;
import com.elm.shj.applicant.portal.services.dto.ChatMessageDto;
import com.elm.shj.applicant.portal.services.dto.ChatMessageLiteDto;
import com.elm.shj.applicant.portal.services.dto.CompanyStaffLiteDto;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.survey.UserSurveyService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/get/{surveyType}")
    public ResponseEntity<WsResponse<?>> findSurveyByDigitalIdAndSurveyType(@PathVariable("surveyType") String surveyType, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse wsResponse = userSurveyService.findSurveyByDigitalIdAndSurveyType(loggedInUserUin, surveyType);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }


}
