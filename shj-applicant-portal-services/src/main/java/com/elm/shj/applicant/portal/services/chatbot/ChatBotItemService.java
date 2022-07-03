/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.chatbot;

import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintDto;
import com.elm.shj.applicant.portal.services.dto.ChatBotItemDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling user incidents operations
 *
 * @author rameez imtiaz
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatBotItemService {

    private final IntegrationService integrationService;
    /**
     * Find chatbot items by lang.
     *
     * @param lang
     * @return
     */
    public List<ChatBotItemDto> findAllChatBotItemsByLang(String lang) {
        return integrationService.findAllChatBotItems(lang);
    }
}
