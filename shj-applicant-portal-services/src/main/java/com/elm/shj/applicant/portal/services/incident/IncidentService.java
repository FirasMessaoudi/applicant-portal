/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.incident;

import com.elm.shj.applicant.portal.services.dto.ApplicantIncidentDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling user incidents operations
 *
 * @author f.messaoudi
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class IncidentService {

    private final IntegrationService integrationService;
    /**
     * Find incidents by ritual.
     *
     * @param ritualId
     * @return
     */
    public List<ApplicantIncidentDto> findIncidents(long ritualId) {
        return this.integrationService.loadIncidents(ritualId);
    }

    public byte[] getAttachment(long id) {
        return this.integrationService.getAttachment(id);
    }

    public WsResponse createIncident(MultipartBodyBuilder builder) {
        return integrationService.createIncident(builder);
    }
}
