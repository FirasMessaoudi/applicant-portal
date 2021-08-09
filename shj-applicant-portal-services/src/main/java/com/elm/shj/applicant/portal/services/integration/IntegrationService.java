/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

import com.elm.shj.applicant.portal.services.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

/**
 * Service handling calling command portal.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class IntegrationService {

    @Value("${admin.portal.url}")
    private String commandIntegrationUrl;

    /* lookups relative URLs */
    private final String RITUAL_TYPES_LOOKUP_URL = "/applicant/lookup/ritual-type/list";
    private final String CARD_STATUSES_LOOKUP_URL = "/applicant/lookup/card-status/list";
    private final String RELATIVE_RELATIONSHIPS_LOOKUP_URL = "/applicant/lookup/relative-relationship/list";
    private final String MARITAL_STATUS_LOOKUP_URL = "/applicant/lookup/marital-status/list";
    private final String COUNTRIES_LOOKUP_URL = "/applicant/lookup/country/list";
    private final String HEALTH_SPECIAL_NEEDS_LOOKUP_URL = "/applicant/lookup/health-special-needs/list";


    private final WebClient webClient;

    /**
     * Load ritual types from command portal.
     *
     * @return
     */
    public List<RitualTypeLookupDto> loadRitualTypes() {
        RitualTypeLookupDto[] ritualTypesArray = webClient.get().uri(commandIntegrationUrl + RITUAL_TYPES_LOOKUP_URL).retrieve().bodyToMono(RitualTypeLookupDto[].class).block();
        return Arrays.asList(ritualTypesArray);
    }

    /**
     * Load card statuses from command portal.
     *
     * @return
     */
    public List<CardStatusLookupDto> loadCardStatuses() {
        CardStatusLookupDto[] cardStatusesArray = webClient.get().uri(commandIntegrationUrl + CARD_STATUSES_LOOKUP_URL).retrieve().bodyToMono(CardStatusLookupDto[].class).block();
        return Arrays.asList(cardStatusesArray);
    }

    /**
     * Load relative relationships from command portal.
     *
     * @return
     */
    public List<RelativeRelationshipLookupDto> loadRelativeRelationships() {
        RelativeRelationshipLookupDto[] relativeRelationshipsArray = webClient.get().uri(commandIntegrationUrl + RELATIVE_RELATIONSHIPS_LOOKUP_URL).retrieve().bodyToMono(RelativeRelationshipLookupDto[].class).block();
        return Arrays.asList(relativeRelationshipsArray);
    }

    /**
     * Load marital statues from command portal.
     *
     * @return
     */
    public List<MaritalStatusLookupDto> loadMaritalStatuses() {
        MaritalStatusLookupDto[] maritalStatusesArray = webClient.get().uri(commandIntegrationUrl + MARITAL_STATUS_LOOKUP_URL).retrieve().bodyToMono(MaritalStatusLookupDto[].class).block();
        return Arrays.asList(maritalStatusesArray);
    }

    /**
     * Load countries from command portal.
     *
     * @return
     */
    public List<CountryLookupDto> loadCountries() {
        CountryLookupDto[] countriesArray = webClient.get().uri(commandIntegrationUrl + COUNTRIES_LOOKUP_URL).retrieve().bodyToMono(CountryLookupDto[].class).block();
        return Arrays.asList(countriesArray);
    }

    /**
     * Load countries from command portal.
     *
     * @return
     */
    public List<HealthSpecialNeedsTypeLookupDto> loadSpecialNeedsTypes() {
        HealthSpecialNeedsTypeLookupDto[] specialNeedsTypesArray = webClient.get().uri(commandIntegrationUrl + HEALTH_SPECIAL_NEEDS_LOOKUP_URL).retrieve().bodyToMono(HealthSpecialNeedsTypeLookupDto[].class).block();
        return Arrays.asList(specialNeedsTypesArray);
    }
}
