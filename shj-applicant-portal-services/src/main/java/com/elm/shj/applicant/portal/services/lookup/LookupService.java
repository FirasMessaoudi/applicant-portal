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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${google.map.key}")
    private String googleMapKey;
    private final IntegrationService integrationService;
    private List<RitualTypeLookupDto> ritualTypes;
    private List<CardStatusLookupDto> cardStatuses;
    private List<RelativeRelationshipLookupDto> relativeRelationships;
    private List<MaritalStatusLookupDto> maritalStatuses;
    private List<CountryLookupDto> countries;
    private List<HealthSpecialNeedsTypeLookupDto> healthSpecialNeeds;
    private List<HousingCategoryLookupDto> housingCategories;
    private List<HousingTypeLookupDto> housingTypes;
    private List<PackageTypeLookupDto> packageTypes;
    private List<CompanyRitualStepLookupDto> ritualSteps;
    private List<CompanyStaffTitleLookupDto> staffTitles;
    private List<HousingSiteLookupDto> housingSites;
    private List<TransportationTypeLookupDto> transportationTypes;
    private List<HealthImmunizationLookupDto> healthImmunizations;
    private List<ApplicantDigitalIdStatusLookupDto> applicantDigitalIdStatuses;
    private List<ReligiousOccasionsDayLookupDto> religiousOccasionsDay;
    private List<NotificationCategoryLookupDto> notificationCategories;
    private List<NotificationTemplateNameLookupDto> retrieveNotificationNames;
    private List<MealTypeLookupDto> mealTypes;
    private List<MealTimeLookupDto> mealTimes;
    private List<LanguageLookupDto> supportedLanguages;

    private List<IncidentTypeLookupDto> incidentTypes;
    private List<IncidentStatusLookupDto> incidentStatus;
    private List<ComplaintStatusLookupDto> complaintStatuses;
    private List<ComplaintTypeLookupDto> complaintTypes;
    private List<CityLookupDto> cities;


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
        this.housingCategories = integrationService.loadHousingCategories();
        this.housingTypes = integrationService.loadHousingTypes();
        this.packageTypes = integrationService.loadPackageTypes();
        this.housingSites = integrationService.loadHousingSites();
        this.transportationTypes = integrationService.loadTransportationTypes();
        this.healthImmunizations = integrationService.loadHealthImmunizations();
        this.applicantDigitalIdStatuses = integrationService.loadDigitalIdStatuses();
        this.religiousOccasionsDay = integrationService.loadReligiousOccasionsDay();
        this.notificationCategories = integrationService.loadNotificationCategories();
        this.retrieveNotificationNames = integrationService.loadNotificationNames();
        this.mealTypes = integrationService.loadMealTypes();
        this.mealTimes = integrationService.loadMealTime();
        this.supportedLanguages = integrationService.loadSupportedLanguages();
        this.incidentStatus = integrationService.loadIncidentStatus();
        this.incidentTypes = integrationService.loadIncidentTypes();
        this.complaintTypes = integrationService.listComplaintTypes();
        this.complaintStatuses = integrationService.listComplaintStatus();
        this.cities = integrationService.listCities();


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

    public List<HousingCategoryLookupDto> retrieveHousingCategories() {
        return this.housingCategories;
    }

    public List<HousingTypeLookupDto> retrieveHousingTypes() {
        return this.housingTypes;
    }

    public List<PackageTypeLookupDto> retrievePackageTypes() {
        return this.packageTypes;
    }

    public List<CompanyRitualStepLookupDto> retrieveCompanyRitualStepLookups() {
        return this.ritualSteps;
    }

    public List<CompanyStaffTitleLookupDto> retrieveCompanyStaffTitleLookups() {
        return staffTitles;
    }

    public List<HousingSiteLookupDto> retrieveHousingSites() {
        return housingSites;
    }

    public List<TransportationTypeLookupDto> retrieveTransportationTypes() {
        return transportationTypes;
    }

    public List<ApplicantDigitalIdStatusLookupDto> retrieveApplicantStatuses() {
        return applicantDigitalIdStatuses;
    }

    public String retrieveGoogleMapKey() {
        return this.googleMapKey;
    }

    public List<HealthImmunizationLookupDto> retrieveHealthImmunizations() {
        return healthImmunizations;
    }

    public List<ReligiousOccasionsDayLookupDto> retrieveReligiousOccasionsDay() {
        return religiousOccasionsDay;
    }

    public List<NotificationCategoryLookupDto> retrieveNotificationCategories() {
        return notificationCategories;
    }

    public List<NotificationTemplateNameLookupDto> retrieveNotificationNames() {
        return retrieveNotificationNames;
    }

    public List<MealTypeLookupDto> retrieveMealTypes() {
        return mealTypes;
    }

    public List<MealTimeLookupDto> retrieveMealTime() {
        return mealTimes;
    }

    public List<LanguageLookupDto> retrieveSupportedLanguages() {
        return this.supportedLanguages;
    }

    public List<IncidentStatusLookupDto> retrieveIncidentStatus() {
        return this.incidentStatus;
    }
    public List<IncidentTypeLookupDto> retrieveIncidentTypes() {
        return this.incidentTypes;
    }
    public List<ComplaintStatusLookupDto> retrieveComplaintStatusesLookups() {
        return complaintStatuses;
    }
    public List<ComplaintTypeLookupDto> retrieveComplaintTypesLookups() {
        return complaintTypes;
    }
    public List<CityLookupDto> retrieveCitiesLookups() {
        return cities;
    }
}
