/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.lookup;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Service handling lookups, data will be loaded at the application startup and will be refreshed at the scheduled time from the command portal.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LookupService {

    private final IntegrationService integrationService;
    private List<RitualTypeLookupDto> ritualTypes;
    private List<CardStatusLookupDto> cardStatuses;
    private List<RelativeRelationshipLookupDto> relativeRelationships;
    private List<MaritalStatusLookupDto> maritalStatuses;
    private List<CountryLookupDto> countries;
    private List<HealthSpecialNeedsTypeLookupDto> healthSpecialNeeds;
    private List<CompanyRitualStepLookupDto> ritualSteps;
    private List<CompanyStaffTitleLookupDto> staffTitles;
    @PostConstruct
    @Scheduled(cron = "${scheduler.load.lookups.cron}")
    @SchedulerLock(name = "load-applicant-lookups-task")
    void loadLookups() {
        log.info("loading lookups...");
        // call command portal to load lookups
        this.ritualTypes = integrationService.loadRitualTypes();
        this.cardStatuses = integrationService.loadCardStatuses();
        this.relativeRelationships = integrationService.loadRelativeRelationships();
        this.maritalStatuses = integrationService.loadMaritalStatuses();
        this.countries = integrationService.loadCountries();
        this.healthSpecialNeeds = integrationService.loadSpecialNeedsTypes();
        this.ritualSteps = integrationService.loadRitualSteps();
        this.staffTitles = integrationService.loadCompanyStaffTitles();
    }

    public List<RitualTypeLookupDto> retrieveRitualTypes() {
        return this.ritualTypes;
    }

    public List<CardStatusLookupDto> retrieveCardStatuses() {
        return this.cardStatuses;
    }

    public List<RelativeRelationshipLookupDto> retrieveRelativeRelationships() {
        return this.relativeRelationships;
    }

    public List<MaritalStatusLookupDto> retrieveMaritalStatuses() {
        return this.maritalStatuses;
    }

    public List<CountryLookupDto> retrieveCountries() {
        return this.countries;
    }

    public List<HealthSpecialNeedsTypeLookupDto> retrieveHealthSpecialNeedsTypes() {
        return this.healthSpecialNeeds;
    }

    public List<CompanyRitualStepLookupDto> retrieveCompanyRitualStepLookups() {
        return this.ritualSteps;
    }

    public List<CompanyStaffTitleLookupDto> retrieveCompanyStaffTitleLookups() {
        return staffTitles;
    }
}
