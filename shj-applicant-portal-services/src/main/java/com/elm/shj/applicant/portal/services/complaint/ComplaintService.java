/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.complaint;

import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;

/**
 * Service handling user incidents operations
 *
 * @author f.messaoudi
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ComplaintService {

    private final IntegrationService integrationService;
    /**
     * Find incidents by ritual.
     *
     * @param ritualId
     * @return
     */
//    public List<ApplicantComplaintDto> findIncidents(long ritualId) {
//        return this.integrationService.loadIncidents(ritualId);
//    }

//    public byte[] getAttachment(long id) {
//        return this.integrationService.getAttachment(id);
//    }

    public WsResponse createComplaint(MultipartBodyBuilder builder) {
        return integrationService.createComplaint(builder);
    }
}
