/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.lookup;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.lookup.LookupService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for lookup data
 *
 * @author ahmad flaifel
 * @since 1.3.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_LOOKUP)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LookupController {

    private final LookupService lookupService;

    /**
     * List ritual types.
     *
     * @return list of ritual types
     */
    @GetMapping("/ritual-type/list")
    public List<RitualTypeLookupDto> listRitualTypes() {
        log.debug("list ritual types...");
        return lookupService.retrieveRitualTypes();
    }

    /**
     * List card statuses.
     *
     * @return list of card statuses
     */
    @GetMapping("/card-status/list")
    public List<CardStatusLookupDto> listCardStatuses() {
        log.debug("list card statuses...");
        return lookupService.retrieveCardStatuses();
    }

    /**
     * List relative relationships.
     *
     * @return list of relative relationships
     */
    @GetMapping("/relative-relationship/list")
    public List<RelativeRelationshipLookupDto> listRelativeRelationships() {
        log.debug("list relative relationships...");
        return lookupService.retrieveRelativeRelationships();
    }

    /**
     * List marital statuses.
     *
     * @return list of marital statuses
     */
    @GetMapping("/marital-status/list")
    public List<MaritalStatusLookupDto> listMaritalStatuses() {
        log.debug("list marital statuses...");
        return lookupService.retrieveMaritalStatuses();
    }

    /**
     * List countries.
     *
     * @return list of countries
     */
    @GetMapping("/country/list")
    public List<CountryLookupDto> listCountries() {
        log.debug("list countries...");
        return lookupService.retrieveCountries();
    }

    /**
     * List health special needs types.
     *
     * @return list of health special needs types
     */
    @GetMapping("/health-special-needs/list")
    public List<HealthSpecialNeedsTypeLookupDto> listHealthSpecialNeeds() {
        log.debug("list health special needs...");
        return lookupService.retrieveHealthSpecialNeedsTypes();
    }

    @GetMapping("/housing-category/list")
    public List<HousingCategoryLookupDto> listHousingCategories(Authentication authentication) {
        log.debug("list housing category...");
        return lookupService.retrieveHousingCategories();
    }

    @GetMapping("/housing-type/list")
    public List<HousingTypeLookupDto> listHousingTypes(Authentication authentication) {
        log.debug("list housing type...");
        return lookupService.retrieveHousingTypes();
    }

    @GetMapping("/package-type/list")
    public List<PackageTypeLookupDto> listPackageTypes(Authentication authentication) {
        log.debug("list package type...");
        return lookupService.retrievePackageTypes();
    }

    /**
     * List Company Ritual Steps Label
     *
     * @return List Company Ritual Steps Label
     */
    @GetMapping("/company_ritual_step/list")
    public List<CompanyRitualStepLookupDto> listCompanyRitualStepsLabel(Authentication authentication) {
        log.debug("list company ritual step labels...");
        return lookupService.retrieveCompanyRitualStepLookups();
    }

    /**
     * List Company Staff Title Labels
     *
     * @return List Company Staff Title Labels
     */
    @GetMapping("/company_staff_title_label/list")
    public List<CompanyStaffTitleLookupDto> listCompanyStaffTitleLabels(Authentication authentication) {
        log.debug("list company staff title labels...");
        return lookupService.retrieveCompanyStaffTitleLookups();
    }

}
