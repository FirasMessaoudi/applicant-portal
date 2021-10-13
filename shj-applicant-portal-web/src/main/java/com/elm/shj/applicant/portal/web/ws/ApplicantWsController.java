/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.ApplicantHealthLiteDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantPackageDetailsDto;
import com.elm.shj.applicant.portal.services.dto.ApplicantRitualCardLiteDto;
import com.elm.shj.applicant.portal.services.dto.HealthSpecialNeedsTypeLookupDto;
import com.elm.shj.applicant.portal.services.lookup.LookupService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for exposing applicant web services for external party.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_APPLICANT)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ApplicantWsController {
    private final UserService userService;
    private final LookupService lookupService;

    /**
     * get user card details by his uin and ritual ID
     *
     * @param ritualId       the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/card-details/{ritualId}")
    public ResponseEntity<WsResponse<?>> findApplicantCardDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantRitualCardLiteDto card =  userService.findApplicantCardDetailsByUinAndRitualId(loggedInUserUin, ritualId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(card).build());

    }
    /**
     * get user package details by his uin and companyRitualSeasonId
     *
     * @param companyRitualSeasonId the ID of the selected applicant's company Ritual Season Id
     * @param authentication        the authenticated user
     */
    @GetMapping("/package/details/{companyRitualSeasonId}")
    public ResponseEntity<WsResponse<?>>  findApplicantPackageDetails(@PathVariable Long companyRitualSeasonId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantPackageDetailsDto applicantPackageDetails = userService.findApplicantPackageDetails(loggedInUserUin, companyRitualSeasonId);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantPackageDetails).build());
    }
    /**
     * get user health details by uin and ritual ID
     */
    @GetMapping("/health/{ritualId}")
    public ResponseEntity<WsResponse<?>> findApplicantHealthDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        Optional<ApplicantHealthLiteDto> applicantHealthDetails = userService.findApplicantHealthDetailsByUinAndRitualId(loggedInUserUin, ritualId);
        if (!applicantHealthDetails.isPresent()) {
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, loggedInUserUin);
        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantHealthDetails.get()).build());
    }
    private ResponseEntity<WsResponse<?>> generateFailResponse(WsError.EWsError errorCode, String reference) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(errorCode.getCode()).referenceNumber(reference).build()).build());
    }
}
