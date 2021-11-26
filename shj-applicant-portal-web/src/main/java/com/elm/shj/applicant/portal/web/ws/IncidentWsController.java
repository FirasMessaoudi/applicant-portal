/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.ApplicantIncidentDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantRitualDto;
import com.elm.shj.applicant.portal.services.incident.IncidentService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *  Controller for exposing notification web services for external party.
 *
 * @author f.messaoudi
 * @since 1.1.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_INCIDENTS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class IncidentWsController {
    private final IncidentService incidentService;

    /**
     * get all incidents by ritual id
     *
     * @param authentication the authenticated user
     */

    @GetMapping("/list/{ritualId}")
    public ResponseEntity<WsResponse<?>> findIncidents(@PathVariable long ritualId ,Authentication authentication) {
        List<ApplicantIncidentDto> incidentDtos = incidentService.findIncidents(ritualId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(incidentDtos).build());
    }


    @GetMapping(value = "/download/{id}")
    public ResponseEntity<WsResponse<?>> downloadFile(@PathVariable long id,
                                                           Authentication authentication) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(incidentService.getAttachment(id)).build());
    }

    /**
     * Creates a new applicant incident
     *
     * @param incidentAttachment       the incident attachment
     * @return WsResponse of  the persisted applicant incident
     */
    @PostMapping(value = "/create-incident/{typeCode}/{applicantRitualId}/{locationLat}/{locationLng}/{description}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WsResponse<?>> createIncident(
            @PathVariable("typeCode") String typeCode,
            @PathVariable("description") String description,
            @PathVariable("locationLat") double locationLat,
            @PathVariable("locationLng") double locationLng,
            @PathVariable("applicantRitualId") long applicantRitualId,
            @RequestPart("attachment") MultipartFile incidentAttachment) throws Exception {

        log.info("adding  applicant incident");
        ApplicantIncidentDto incidentDto = new ApplicantIncidentDto();
        incidentDto.setTypeCode(typeCode);
        incidentDto.setLocationLat(locationLat);
        incidentDto.setDescription(description);
        incidentDto.setLocationLng(locationLng);
        ApplicantRitualDto applicantRitualDto = new ApplicantRitualDto();
        applicantRitualDto.setId(applicantRitualId);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("attachment", incidentAttachment.getResource());
        builder.part("incident", incidentDto);
        builder.part("applicantRitual", applicantRitualDto);
        return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(incidentService.createIncident(builder)).build());

    }


}
