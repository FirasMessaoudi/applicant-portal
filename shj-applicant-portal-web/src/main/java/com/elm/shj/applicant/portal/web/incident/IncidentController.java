/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.incident;

import com.elm.dcc.foundation.commons.validation.SafeFile;
import com.elm.shj.applicant.portal.services.incident.IncidentComplaintService;
import com.elm.shj.applicant.portal.services.dto.ApplicantIncidentDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantIncidentLiteDto;
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
@RequestMapping(Navigation.API_INCIDENTS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class IncidentController {
    private final IncidentComplaintService incidentService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    /**
     * get all incidents by ritual id
     *
     * @param authentication the authenticated user
     * @return the list of incidents
     */

    @GetMapping("/list")
    public ResponseEntity<?> findIncidents(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<ApplicantIncidentDto> incidentList = incidentService.findIncidents(userService.findIdApplicantRitualId(loggedInUserUin));

        return ResponseEntity.ok(incidentList);
    }

    /**
     * create new applicant incident
     *
     * @param incident
     * @param incidentAttachment
     * @return the created applicant_incident
     * @throws Exception
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createIncident(@RequestPart("incident") ApplicantIncidentLiteDto incident,
                                                         @RequestPart(value = "attachment", required = false) @SafeFile MultipartFile incidentAttachment, Authentication authentication) throws Exception {
        log.info("adding applicant incident");
        // log.info(incidentAttachment.getContentType());
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        Long applicantRitualId = userService.findIdApplicantRitualId(loggedInUserUin);
        ApplicantIncidentLiteDto incidentDto = new ApplicantIncidentLiteDto();
        incidentDto.setTypeCode(incident.getTypeCode());
        incidentDto.setCity(incident.getCity());
        incidentDto.setCampNumber(incident.getCampNumber());
        incidentDto.setMobileNumber(userService.findMobileNumber(jwtTokenService.retrieveUserIdFromToken(((JwtToken) authentication).getToken()).orElse(0L)));
        incidentDto.setDescription(incident.getDescription());
        incidentDto.setApplicantRitualId(applicantRitualId);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if (incidentAttachment != null && !incidentAttachment.isEmpty() && incidentAttachment.getSize() > 0)
            builder.part("attachment", incidentAttachment.getResource());
        builder.part("incident", incidentDto);
        WsResponse response = incidentService.createIncident(builder);
        return ResponseEntity.ok(response.getBody());
    }

    /**
     * get incident by id
     *
     * @param authentication the authenticated user
     * @return the incident
     */

    @GetMapping("/find/{complaintId}")
    public ResponseEntity<?> findIncidentBuId(@PathVariable long complaintId) {
        ApplicantIncidentDto incident = incidentService.findIncidentById(complaintId);

        return ResponseEntity.ok(incident);
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
        return ResponseEntity.ok(incidentService.downloadApplicantIncidentAttachment(attachmentId));
    }

}
