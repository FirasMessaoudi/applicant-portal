/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.lookup.LookupService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for exposing lookups for external party.
 *
 * @author salzoubi
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
@RequestMapping(Navigation.API_INTEGRATION_USERS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LookupWsController {

    private final LookupService lookupService;

    /**
     * List ritual types.
     *
     * @return list of ritual types
     */
    @GetMapping("/ritual-type/list")
    public ResponseEntity<WsResponse<?>> listRitualTypes() {
        log.debug("list ritual types...");
        List<RitualTypeLookupDto> ritualTypeLookupDtos = lookupService.retrieveRitualTypes();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(ritualTypeLookupDtos).build());
    }

    /**
     * List card statuses.
     *
     * @return list of card statuses
     */
    @GetMapping("/card-status/list")
    public ResponseEntity<WsResponse<?>> listCardStatuses() {
        log.debug("list card statuses...");

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(lookupService.retrieveCardStatuses()).build());
    }

    /**
     * List relative relationships.
     *
     * @return list of relative relationships
     */
    @GetMapping("/relative-relationship/list")
    public ResponseEntity<WsResponse<?>> listRelativeRelationships() {
        log.debug("list relative relationships...");
        List<RelativeRelationshipLookupDto> relativeRelationshipLookupDtos = lookupService.retrieveRelativeRelationships();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(relativeRelationshipLookupDtos).build());

    }

    /**
     * List marital statuses.
     *
     * @return list of marital statuses
     */
    @GetMapping("/marital-status/list")
    public ResponseEntity<WsResponse<?>> listMaritalStatuses() {
        log.debug("list marital statuses...");
        List<MaritalStatusLookupDto> maritalStatusLookupDtos = lookupService.retrieveMaritalStatuses();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(maritalStatusLookupDtos).build());
    }

    /**
     * List countries.
     *
     * @return list of countries
     */
    @GetMapping("/country/list")
    public ResponseEntity<WsResponse<?>> listCountries() {
        log.debug("list countries...");
        List<CountryLookupDto> countryLookupDtos = lookupService.retrieveCountries();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(countryLookupDtos).build());
    }

    /**
     * List health special needs types.
     *
     * @return list of health special needs types
     */
    @GetMapping("/health-special-needs/list")
    public ResponseEntity<WsResponse<?>> listHealthSpecialNeeds() {
        log.debug("list health special needs...");
        List<HealthSpecialNeedsTypeLookupDto> healthSpecialNeedsTypeLookupDtos = lookupService.retrieveHealthSpecialNeedsTypes();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(healthSpecialNeedsTypeLookupDtos).build());
    }

    @GetMapping("/housing-category/list")
    public ResponseEntity<WsResponse<?>> listHousingCategories(Authentication authentication) {
        log.debug("list housing category...");
        List<HousingCategoryLookupDto> housingCategoryLookupDtos = lookupService.retrieveHousingCategories();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(housingCategoryLookupDtos).build());
    }

    @GetMapping("/housing-type/list")
    public ResponseEntity<WsResponse<?>> listHousingTypes(Authentication authentication) {
        log.debug("list housing type...");
        List<HousingTypeLookupDto> housingTypeLookupDtos = lookupService.retrieveHousingTypes();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(housingTypeLookupDtos).build());
    }

    @GetMapping("/package-type/list")
    public ResponseEntity<WsResponse<?>> listPackageTypes(Authentication authentication) {
        log.debug("list package type...");
        List<PackageTypeLookupDto> packageTypeLookupDtos = lookupService.retrievePackageTypes();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(packageTypeLookupDtos).build());
    }

    /**
     * List Company Ritual Steps Label
     *
     * @return List Company Ritual Steps Label
     */
    @GetMapping("/company-ritual-step/list")
    public ResponseEntity<WsResponse<?>> listCompanyRitualStepsLabel(Authentication authentication) {
        log.debug("list company ritual step labels...");
        List<CompanyRitualStepLookupDto> companyRitualStepLookupDtos = lookupService.retrieveCompanyRitualStepLookups();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(companyRitualStepLookupDtos).build());
    }

    /**
     * List Company Staff Title Labels
     *
     * @return List Company Staff Title Labels
     */
    @GetMapping("/company-staff-title-label/list")
    public ResponseEntity<WsResponse<?>> listCompanyStaffTitleLabels(Authentication authentication) {
        log.debug("list company staff title labels...");
        List<CompanyStaffTitleLookupDto> companyStaffTitleLookupDtos = lookupService.retrieveCompanyStaffTitleLookups();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(companyStaffTitleLookupDtos).build());
    }

    @GetMapping("/housing-site/list")
    public ResponseEntity<WsResponse<?>> listHousingSites(Authentication authentication) {
        log.debug("list housing site...");
        List<HousingSiteLookupDto> housingSiteLookupDtos = lookupService.retrieveHousingSites();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(housingSiteLookupDtos).build());
    }

    @GetMapping("/transportation-type/list")
    public ResponseEntity<WsResponse<?>> listTransportationTypes(Authentication authentication) {
        log.debug("list transportation type...");
        List<TransportationTypeLookupDto> transportationTypeLookupDtos = lookupService.retrieveTransportationTypes();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(transportationTypeLookupDtos).build());
    }

    @GetMapping("/health-immunization/list")
    public ResponseEntity<WsResponse<?>> listHealthImmunization(Authentication authentication) {
        log.debug("list health immunizations...");
        List<HealthImmunizationLookupDto> healthImmunizationLookup = lookupService.retrieveHealthImmunizations();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(healthImmunizationLookup).build());
    }

    @GetMapping("/religious-occasions-day/list")
    public ResponseEntity<WsResponse<?>> listReligiousOccasionsDay(Authentication authentication) {
        log.debug("list religious occasions day...");
        List<ReligiousOccasionsDayLookupDto> religiousOccasionsDayLookup = lookupService.retrieveReligiousOccasionsDay();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(religiousOccasionsDayLookup).build());
    }

    @GetMapping("/notification-category/list")
    public ResponseEntity<WsResponse<?>> listNotificationCategories(Authentication authentication) {
        log.debug("list religious occasions day...");
        List<NotificationCategoryLookupDto> notificationCategoryLookupDtos = lookupService.retrieveNotificationCategories();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(notificationCategoryLookupDtos).build());
    }

    @GetMapping("/notification-name/list")
    public ResponseEntity<WsResponse<?>> listNotificationNames(Authentication authentication) {
        log.debug("list notification names...");
        List<NotificationTemplateNameLookupDto> notificationTemplateNameLookupDtos = lookupService.retrieveNotificationNames();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(notificationTemplateNameLookupDtos).build());
    }
}
