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
 * Controller for exposing notification web services for external party.
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
     * @return the list of incidents
     */

    @GetMapping("/list/{ritualId}")
    public ResponseEntity<WsResponse<?>> findIncidents(@PathVariable long ritualId, Authentication authentication) {
        List<ApplicantIncidentDto> incidentDtos = incidentService.findIncidents(ritualId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(incidentDtos).build());
    }

    /**
     * download incident attachment by id
     *
     * @param id
     * @param authentication
     * @return the attachment as byte
     */
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<WsResponse<?>> downloadFile(@PathVariable long id,
                                                      Authentication authentication) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(incidentService.getAttachment(id)).build());
    }


    /**
     * create new applicant incident
     *
     * @param typeCode
     * @param description
     * @param locationLat
     * @param locationLng
     * @param applicantRitualId
     * @param incidentAttachment
     * @return the created applicant_incident
     * @throws Exception
     */
    @PostMapping(value = "/create-incident", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WsResponse<?>> createIncident(@RequestPart("typeCode") String typeCode,
                                                        @RequestPart("description") String description,
                                                        @RequestPart("locationLat") String locationLat,
                                                        @RequestPart("locationLng") String locationLng,
                                                        @RequestPart("applicantRitualId") String applicantRitualId,
                                                        @RequestPart(value = "attachment", required = false) MultipartFile incidentAttachment) throws Exception {

        log.info("adding  applicant incident");

        ApplicantIncidentDto incidentDto = new ApplicantIncidentDto();
        incidentDto.setTypeCode(typeCode);
        incidentDto.setLocationLat(Double.parseDouble(locationLat));
        incidentDto.setDescription(description);
        incidentDto.setLocationLng(Double.parseDouble(locationLng));
        ApplicantRitualDto applicantRitualDto = new ApplicantRitualDto();
        applicantRitualDto.setId(Long.parseLong(applicantRitualId));
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if (incidentAttachment != null && !incidentAttachment.isEmpty() && incidentAttachment.getSize() > 0)
            builder.part("attachment", incidentAttachment.getResource());
        builder.part("incident", incidentDto);
        builder.part("applicantRitual", applicantRitualDto);
        return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(incidentService.createIncident(builder)).build());

    }


}
