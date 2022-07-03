/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.dcc.foundation.commons.validation.SafeFile;
import com.elm.shj.applicant.portal.services.chatbot.ChatBotItemService;
import com.elm.shj.applicant.portal.services.complaint.ComplaintService;
import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintLiteDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantRitualDto;
import com.elm.shj.applicant.portal.services.dto.ChatBotItemDto;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for exposing chatbot item web services for external party.
 *
 * @author rameez imtiaz
 * @since 1.2.4
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_CHATBOT)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatBotItemWsController {
    private final ChatBotItemService chatBotItemService;

    /**
     * get all chatbot items by lang
     *
     * @param lang the selected user
     * @return the list of chatbot items
     */

    @GetMapping("/list/{lang}")
    public ResponseEntity<WsResponse<?>> findComplaints(@PathVariable String lang) {
        log.info("Start chatbot items by lang {}", lang);
        List<ChatBotItemDto> chatBotItems = chatBotItemService.findAllChatBotItemsByLang(lang);
        log.info("finish chatbot items find with {}", chatBotItems);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(chatBotItems).build());
    }

}
