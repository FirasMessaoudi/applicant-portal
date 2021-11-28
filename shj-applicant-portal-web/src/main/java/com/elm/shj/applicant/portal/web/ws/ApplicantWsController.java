/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.lookup.LookupService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
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

import javax.validation.Valid;
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
     * get user card details by his uin and ritual ID
     *
     * @param ritualId       the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/card-details/{ritualId}")
    public ResponseEntity<WsResponse<?>> findApplicantCardDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantRitualCardLiteDto card = userService.findApplicantCardDetailsByUinAndRitualId(loggedInUserUin, ritualId);
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
    public ResponseEntity<WsResponse<?>> findApplicantPackageDetails(@PathVariable Long companyRitualSeasonId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantPackageDetailsDto applicantPackageDetails = userService.findApplicantPackageDetails(loggedInUserUin, companyRitualSeasonId);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantPackageDetails).build());
    }

    /**
     * get user package catering event if there is no applicant package catering by his uin and companyRitualSeasonId
     *
     * @param companyRitualSeasonId the ID of the selected applicant's company Ritual Season Id
     * @param authentication        the authenticated user
     */
    @GetMapping("/package/catering/{companyRitualSeasonId}")
    public ResponseEntity<WsResponse<?>> findPackageCatering(@PathVariable Long companyRitualSeasonId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<PackageCateringDto> packageCateringList = userService.findPackageCatering(loggedInUserUin, companyRitualSeasonId);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(packageCateringList).build());
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

    /**
     * get user latest ritual season lite by uin
     */
    @GetMapping("/ritual-season/latest")
    public ResponseEntity<WsResponse<?>> findLatestApplicantRitualSeasonByUin(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        CompanyRitualSeasonLiteDto ritualSeason = userService.findLatestApplicantRitualSeasonByUin(loggedInUserUin);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(ritualSeason).build());
    }

    /**
     * get user main data by uin and ritualId
     */
    @GetMapping("/main-data/{ritualId}")
    public ResponseEntity<WsResponse<?>> findUserMainDataByUin(@PathVariable long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        Optional<ApplicantMainDataDto> applicantMainDataDto = userService.findUserMainDataByUin(loggedInUserUin, ritualId);
        if (!applicantMainDataDto.isPresent()) {
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, loggedInUserUin);
        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantMainDataDto.get()).build());
    }

    /**
     * get user Tafweej details by uin and ritual ID
     *
     * @param ritualId       the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/tafweej/{ritualId}")
    public ResponseEntity<WsResponse<?>> findApplicantRitualStepsDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<CompanyRitualStepMainDataDto> ritualSteps = userService.findApplicantTafweejDetailsByUinAndRitualId(loggedInUserUin, ritualId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(ritualSteps).build());
    }


    /**
     * get user Group Leaders details by uin and ritual ID
     *
     * @param ritualId       the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/company-staff/{ritualId}")
    public ResponseEntity<WsResponse<?>> findRelatedEmployeesByApplicantUinAndSeasonId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<CompanyStaffDto> employees = userService.findRelatedEmployeesByApplicantUinAndSeasonId(loggedInUserUin, ritualId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(employees).build());
    }


    private ResponseEntity<WsResponse<?>> generateFailResponse(WsError.EWsError errorCode, String reference) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(errorCode.getCode()).referenceNumber(reference).build()).build());
    }

    @GetMapping("/company-details/{ritualId}")
    public ResponseEntity<WsResponse<?>> findApplicantCompanyDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        CompanyLiteDto companyDetails = userService.findCompanyDetailsByUinAndRitualId(loggedInUserUin, ritualId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(companyDetails).build());
    }

    @GetMapping("/housing-details/{ritualId}")
    public ResponseEntity<WsResponse<?>> findHousingDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        PackageHousingDto housingDetails = userService.findHousingDetailsByUinAndRitualId(loggedInUserUin, ritualId);
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
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, uin);      }
        return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(applicant).build());

    }
}
