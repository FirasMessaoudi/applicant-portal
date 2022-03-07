/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.chat;

import com.elm.shj.applicant.portal.services.dto.ChatContactLiteDto;
import com.elm.shj.applicant.portal.services.dto.CompanyStaffLiteDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
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

    /**
     * List all chat contacts of a specific applicant.
     *
     * @param uin      the UIN of the applicant
     * @param ritualId the selected ritual ID
     * @return the list of chat contacts
     */
    public List<ChatContactLiteDto> findChatContactsByUinAndRitualId(String uin, Long ritualId) {
        return integrationService.findApplicantChatContacts(uin, ritualId);
    }

    public WsResponse findOneApplicantByUinAndRitualId(String uin, Long ritualId, String applicantUin) {
        return integrationService.findOneApplicantByUinAndRitualId(uin, ritualId, applicantUin);
    }

    /**
     * Creates a new applicant chat contact
     *
     * @return savedContact saved one
     */
    public WsResponse createApplicantChatContact(Long ritualId, ChatContactLiteDto applicantChatContact) {
        return integrationService.createApplicantChatContact(ritualId, applicantChatContact);
    }

    /**
     * Creates a new staff chat contact
     *
     * @param contactUin the SUIN
     * @return savedContact saved one
     */
    public ChatContactLiteDto findApplicantChatByApplicantUinAndContactUin(String applicantUin, String contactUin) {
        return integrationService.findApplicantChatContact(applicantUin, contactUin);
    }

    /**
     * Creates a new staff chat contact
     *
     * @param contactUin the SUIN
     * @return savedContact saved one
     */
    public WsResponse createStaffChatContact(String uin, Long ritualId, String contactUin) {
        return integrationService.createStaffChatContact(uin, ritualId, contactUin);
    }

    /**
     * Updates user defined chat contact
     *
     * @param applicantChatContact
     * @return updatedContact updated one
     */
    public ChatContactLiteDto updateApplicantChatContact(long id, ChatContactLiteDto applicantChatContact) {
        return integrationService.updateChatContact(id, applicantChatContact);
    }

    /**
     * Delete user defined chat contact
     *
     * @return number of rows affected
     */
    public String deleteChatContact(String applicantUin, String contactUin) {
        return integrationService.deleteChatContact(applicantUin, contactUin);
    }

    public CompanyStaffLiteDto findStaffContactBySuinAndRitualId(String suin) {
        return integrationService.findStaffContact(suin);
    }


}
