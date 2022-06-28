/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.dcc.foundation.commons.validation.SafeFile;
import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantComplaintLiteDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantRitualDto;
import com.elm.shj.applicant.portal.services.complaint.ComplaintService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(Navigation.API_INTEGRATION_COMPLAINTS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ComplaintWsController {
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
    public ResponseEntity<WsResponse<?>> findComplaints(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantRitualDto applicantRitualDto = userService.findApplicantRitual(loggedInUserUin);
        List<ApplicantComplaintDto> complaintList = complaintService.findComplaints(applicantRitualDto.getId());

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(complaintList).build());
    }

    /**
     * create new applicant complaint
     *
     * @param typeCode
     * @param description
     * @param city
     * @param campNumber
     * @param locationLat
     * @param locationLng
     * @param complaintAttachment
     * @return the created applicant_complaint
     * @throws Exception
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WsResponse<?>> createComplaint(@RequestPart("typeCode") String typeCode,
                                                         @RequestPart("description") String description,
                                                         @RequestPart(value = "city", required = false) String city,
                                                         @RequestPart(value = "campNumber", required = false) String campNumber,
                                                         @RequestPart(value = "locationLat", required = false) String locationLat,
                                                         @RequestPart(value = "locationLng", required = false) String locationLng,
                                                         @RequestPart(value = "attachment", required = false) @SafeFile MultipartFile complaintAttachment, Authentication authentication) throws Exception {
        log.info("adding applicant complaint");
        // log.info(complaintAttachment.getContentType());
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantRitualDto applicantRitualDto = userService.findApplicantRitual(loggedInUserUin);
        ApplicantComplaintLiteDto complaintDto = new ApplicantComplaintLiteDto();
        complaintDto.setTypeCode(typeCode);
        complaintDto.setCity(city);
        complaintDto.setCampNumber(campNumber);
        complaintDto.setMobileNumber(userService.findMobileNumber(jwtTokenService.retrieveUserIdFromToken(((JwtToken) authentication).getToken()).orElse(0L)));
        if (locationLat != null)
            complaintDto.setLocationLat(Double.parseDouble(locationLat));
        complaintDto.setDescription(description);
        if (locationLng != null)
            complaintDto.setLocationLng(Double.parseDouble(locationLng));
        complaintDto.setApplicantRitualId(applicantRitualDto.getId());
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if (complaintAttachment != null && !complaintAttachment.isEmpty() && complaintAttachment.getSize() > 0)
            builder.part("attachment", complaintAttachment.getResource());
        builder.part("complaint", complaintDto);
        WsResponse response = complaintService.createComplaint(builder);
        return ResponseEntity.ok(
                WsResponse.builder().status(response.getStatus())
                        .body(response.getBody()).build());
    }

}
