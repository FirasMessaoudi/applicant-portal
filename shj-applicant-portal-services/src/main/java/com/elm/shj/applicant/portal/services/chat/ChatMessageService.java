/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.chat;

import com.elm.shj.applicant.portal.services.dto.ChatMessageDto;
import com.elm.shj.applicant.portal.services.dto.ChatMessageLiteDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling chat messages
 *
 * @author salzoubi
 * @since 1.1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatMessageService {

    private final IntegrationService integrationService;

    public List<ChatMessageLiteDto> listChatContactsWithLatestMessage(String uin) {
        return integrationService.listChatContactsWithLatestMessage(uin);
    }

    public ChatMessageDto saveMessage(ChatMessageDto chatMessage) {
        return integrationService.saveSenderMessage(chatMessage);
    }

    public List<ChatMessageDto> listMessages(int page, int limit, long contactId, long time) {
        return integrationService.listMessages(page, limit, contactId, time);
    }

    public WsResponse markChatMessageAsRead(long chatContactId) {
        return integrationService.markChatMessageAsRead(chatContactId);
    }
}
