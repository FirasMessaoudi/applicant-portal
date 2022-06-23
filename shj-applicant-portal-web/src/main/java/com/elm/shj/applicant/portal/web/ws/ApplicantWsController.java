/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.integration.ApplicantRitualPackageVo;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
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


    /**
     * get user card details by his uin and applicant package ID
     *
     * @param applicantPackageId       the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/card-details/{applicantPackageId}")
    public ResponseEntity<WsResponse<?>> findApplicantCardDetailsByUinAndApplicantPackageId(@PathVariable Long applicantPackageId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantRitualCardLiteDto card = userService.findApplicantCardDetailsByUinAndApplicantPackageId(loggedInUserUin, applicantPackageId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(card).build());
    }

    /**
     * get user package details by his uin and applicantPackageId
     *
     * @param applicantPackageId the ID of the selected applicant's package id
     * @param authentication        the authenticated user
     */
    @GetMapping("/package/details/{applicantPackageId}")
    public ResponseEntity<WsResponse<?>> findApplicantPackageDetails(@PathVariable Long applicantPackageId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantPackageDetailsDto applicantPackageDetails = userService.findApplicantPackageDetails(loggedInUserUin, applicantPackageId);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantPackageDetails).build());
    }

    /**
     * get user package catering event if there is no applicant package catering by his uin and companyRitualSeasonId
     *
     * @param applicantPackageId the ID of the selected applicant's package id
     * @param authentication        the authenticated user
     */
    @GetMapping("/package/catering/{applicantPackageId}")
    public ResponseEntity<WsResponse<?>> findPackageCatering(@PathVariable Long applicantPackageId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<ApplicantPackageCateringDto> packageCateringList = userService.findPackageCatering(loggedInUserUin, applicantPackageId);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(packageCateringList).build());
    }

    /**
     * get user health details by uin and applicant package ID
     */
    @GetMapping("/health/{applicantPackageId}")
    public ResponseEntity<WsResponse<?>> findApplicantHealthDetailsByUinAndApplicantPackageId(@PathVariable Long applicantPackageId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        Optional<ApplicantHealthLiteDto> applicantHealthDetails = userService.findApplicantHealthDetailsByUinAndApplicantPackageId(loggedInUserUin, applicantPackageId);
        if (!applicantHealthDetails.isPresent()) {
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, loggedInUserUin);
        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantHealthDetails.get()).build());
    }

    /**
     * get user main data by uin and applicantPackageId
     */
    @GetMapping("/main-data/{applicantPackageId}")
    public ResponseEntity<WsResponse<?>> findUserMainDataByUin(@PathVariable long applicantPackageId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        Optional<ApplicantMainDataDto> applicantMainDataDto = userService.findUserMainDataByUin(loggedInUserUin, applicantPackageId);
        if (!applicantMainDataDto.isPresent()) {
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, loggedInUserUin);
        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantMainDataDto.get()).build());
    }

    /**
     * get user Tafweej details by uin
     *
     * @param authentication the authenticated user
     */
    @GetMapping("/tafweej")
    public ResponseEntity<WsResponse<?>> findApplicantRitualStepsDetailsByUin(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<CompanyRitualStepMainDataDto> ritualSteps = userService.findApplicantRitualStepsDetailsByUin(loggedInUserUin);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(ritualSteps).build());
    }


    /**
     * get user Group Leaders details by uin and companyRitualSeasonId
     *
     * @param companyRitualSeasonId       the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/company-staff/{companyRitualSeasonId}")
    public ResponseEntity<WsResponse<?>> findRelatedEmployeesByApplicantUinAndCompanyRitualSeasonId(@PathVariable Long companyRitualSeasonId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<CompanyStaffDto> employees = userService.findRelatedEmployeesByApplicantUinAndCompanyRitualSeasonId(loggedInUserUin, companyRitualSeasonId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(employees).build());
    }

    /**
     * get user Group Leaders details by uin and companyRitualSeasonId
     *

     * @param authentication the authenticated user
     */
    @GetMapping("/find/company-staff/group-leader")
    public ResponseEntity<WsResponse<?>> findGroupLeaderByUinAndSeasonId(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse wsResponse = userService.findGroupLeaderByUinAndSeasonId(loggedInUserUin);
        return ResponseEntity.ok(
                WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }


    private ResponseEntity<WsResponse<?>> generateFailResponse(WsError.EWsError errorCode, String reference) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(errorCode.getCode()).referenceNumber(reference).build()).build());
    }

    @GetMapping("/company-details/{companyRitualSeasonId}")
    public ResponseEntity<WsResponse<?>> findApplicantCompanyDetailsByUinAndCompanyRitualSeasonId(@PathVariable Long companyRitualSeasonId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        CompanyLiteDto companyDetails = userService.findCompanyDetailsByUinAndCompanyRitualSeasonId(loggedInUserUin, companyRitualSeasonId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(companyDetails).build());
    }

    @GetMapping("/housing-details/{applicantPackageId}")
    public ResponseEntity<WsResponse<?>> findHousingDetailsByUinAndApplicantPackageId(@PathVariable long applicantPackageId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        PackageHousingDto housingDetails = userService.findHousingDetailsByUinAndApplicantPackageId(loggedInUserUin, applicantPackageId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(housingDetails).build());
    }
    @GetMapping("/find-applicant-ritual")
    public ResponseEntity<WsResponse<?>> findApplicantRitual(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantRitualDto applicantRitualDto = userService.findApplicantRitual(loggedInUserUin);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantRitualDto).build());

    }

    @GetMapping("/find-by-uin/{uin}")
    public ResponseEntity<WsResponse<?>> findApplicantBasicDetailsByUin(@PathVariable String uin, Authentication authentication) {
        ApplicantLiteDto applicant = userService.findApplicantBasicDetailsByUin(uin);
        if (applicant.getFullNameEn() == null) {
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, uin);
        }
        return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(applicant).build());

    }

    /**
     *
     * @param authentication
     * @return latest applicant ritual package
     */
    @GetMapping("/ritual-package/latest")
    public ResponseEntity<WsResponse<?>> findLatestApplicantRitualSeason(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantRitualPackageVo applicantRituals = userService.findLatestApplicantRitualSeason(Long.parseLong(loggedInUserUin));
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantRituals).build());

    }

    @GetMapping("/card-image")
    public ResponseEntity<WsResponse<?>> generateApplicantCardImage(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(userService.findApplicantBadge(loggedInUserUin,true)).build());
    }
}
