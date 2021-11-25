/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.chat;

import com.elm.shj.applicant.portal.services.dto.ApplicantChatContactLiteDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling chat contacts operations
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatContactService {

    private final IntegrationService integrationService;

    public List<ApplicantChatContactLiteDto> findChatContactsByUinAndRitualId(String uin, Long ritualId) {
        return integrationService.findApplicantChatContacts(uin, ritualId);
    }

}