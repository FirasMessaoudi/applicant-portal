/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.complaint;

import com.elm.dcc.foundation.commons.validation.SafeFile;
import com.elm.shj.applicant.portal.services.complaint.ComplaintService;
import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintLiteDto;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for exposing notification web services for external party.
 *
 * @author salzoubi
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
@RequestMapping(Navigation.API_COMPLAINTS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ComplaintController {
    private final ComplaintService complaintService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    /**
     * get all incidents by ritual id
     *
     * @param authentication the authenticated user
     * @return the list of incidents
     */

    @GetMapping("/list")
    public ResponseEntity<?> findComplaints(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<ApplicantComplaintDto> complaintList = complaintService.findComplaints(userService.findIdApplicantRitualId(loggedInUserUin));

        return ResponseEntity.ok(complaintList);
    }

    /**
     * get incident by id
     *
     * @param authentication the authenticated user
     * @return the incident
     */

    @GetMapping("/find/{complaintId}")
    public ResponseEntity<?> findComplaintBuId(@PathVariable long complaintId) {
        ApplicantComplaintDto complaint = complaintService.findComplaintById(complaintId);

        return ResponseEntity.ok(complaint);
    }

    /**
     * create new applicant complaint
     *
     * @param complaint
     * @param complaintAttachment
     * @return the created applicant_complaint
     * @throws Exception
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createComplaint(@RequestPart("complaint") ApplicantComplaintLiteDto complaint,
                                                         @RequestPart(value = "attachment", required = false) @SafeFile MultipartFile complaintAttachment, Authentication authentication) throws Exception {
        log.info("adding applicant complaint");
        // log.info(complaintAttachment.getContentType());
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        Long applicantRitualId = userService.findIdApplicantRitualId(loggedInUserUin);
        ApplicantComplaintLiteDto complaintDto = new ApplicantComplaintLiteDto();
        complaintDto.setTypeCode(complaint.getTypeCode());
        complaintDto.setCity(complaint.getCity());
        complaintDto.setCampNumber(complaint.getCampNumber());
        complaintDto.setMobileNumber(userService.findMobileNumber(jwtTokenService.retrieveUserIdFromToken(((JwtToken) authentication).getToken()).orElse(0L)));
        complaintDto.setDescription(complaint.getDescription());
        complaintDto.setApplicantRitualId(applicantRitualId);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if (complaintAttachment != null && !complaintAttachment.isEmpty() && complaintAttachment.getSize() > 0)
            builder.part("attachment", complaintAttachment.getResource());
        builder.part("complaint", complaintDto);
        WsResponse response = complaintService.createComplaint(builder);
        return ResponseEntity.ok(response.getBody());
    }

    /**
     * Downloads applicant complaint attachment
     *
     * @param attachmentId data request Id
     * @return WsResponse of  the saved complaint attachment
     */
    @GetMapping("/attachments/{attachmentId}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable long attachmentId) throws Exception {
        log.info("Downloading complaint attachment with id# {} ", attachmentId);
        return ResponseEntity.ok(complaintService.downloadApplicantComplaintAttachment(attachmentId));
    }
}
