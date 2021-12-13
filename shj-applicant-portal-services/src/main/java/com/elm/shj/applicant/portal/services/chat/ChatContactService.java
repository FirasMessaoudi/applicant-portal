/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.chat;

import com.elm.shj.applicant.portal.orm.entity.GenericWsResponse;
import com.elm.shj.applicant.portal.services.dto.ApplicantChatContactLiteDto;
import com.elm.shj.applicant.portal.services.dto.CompanyStaffLiteDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.MultipartBodyBuilder;
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
    public List<ApplicantChatContactLiteDto> findChatContactsByUinAndRitualId(String uin, Long ritualId) {
        return integrationService.findApplicantChatContacts(uin, ritualId);
    }

    public GenericWsResponse findOneApplicantByUinAndRitualId(String uin, Long ritualId, String applicantUin) {
        WsResponse commandResponse = integrationService.findOneApplicantByUinAndRitualId(uin, ritualId, applicantUin);
        GenericWsResponse genericWsResponse = new GenericWsResponse();
        if (commandResponse.getStatus() == WsResponse.EWsResponseStatus.SUCCESS)
            genericWsResponse.setStatus(GenericWsResponse.EWsResponseStatus.SUCCESS);
        else
            genericWsResponse.setStatus(GenericWsResponse.EWsResponseStatus.FAILURE);
        genericWsResponse.setBody(commandResponse.getBody());
        return genericWsResponse;
    }

    /**
     * Creates a new applicant chat contact
     *
     * @param builder the multipart body builder
     * @return savedContact saved one
     */
    public ApplicantChatContactLiteDto createApplicantChatContact(String uin, Long ritualId, MultipartBodyBuilder builder) {
        return integrationService.createApplicantChatContact(uin, ritualId, builder);
    }

    /**
     * Creates a new staff chat contact
     *
     * @param contactUin the SUIN
     * @return savedContact saved one
     */
    public ApplicantChatContactLiteDto createStaffChatContact(String uin, Long ritualId, String contactUin) {
        return integrationService.createStaffChatContact(uin, ritualId, contactUin);
    }

    /**
     * Updates user defined chat contact
     *
     * @param builder the multipart body builder
     * @return updatedContact updated one
     */
    public ApplicantChatContactLiteDto updateChatContact(long id, MultipartBodyBuilder builder) {
        return integrationService.updateChatContact(id, builder);
    }

    /**
     * Delete user defined chat contact
     *
     * @return number of rows affected
     */
    public String deleteChatContact(String applicantUin, String contactUin) {
        return integrationService.deleteChatContact(applicantUin, contactUin);
    }

    public CompanyStaffLiteDto findStaffContactBySuinAndRitualId(String loggedInUserUin, Long ritualId, String suin) {
        return integrationService.findStaffContact(loggedInUserUin, ritualId, suin);
    }
}
