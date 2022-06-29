/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.complaint;

import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintDto;
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
    public List<ApplicantComplaintDto> findComplaints(long ritualId) {
        return integrationService.loadComplaints(ritualId);
    }

    /**
     * Find incidents by id.
     *
     * @param id
     * @return
     */
    public ApplicantComplaintDto findComplaintById(long id) {
        return integrationService.findComplaintById(id);
    }

    public WsResponse createComplaint(MultipartBodyBuilder builder) {
        return integrationService.createComplaint(builder);
    }

    /**
     * fetches the original file of the data request
     *
     * @param complaintAttachmentId applicant Complaint Attachment Id
     * @return the attachment of the applicant complaint
     */
    public Resource downloadApplicantComplaintAttachment(long complaintAttachmentId) throws Exception {
        return integrationService.downloadApplicantComplaintAttachment(complaintAttachmentId);
    }


}
