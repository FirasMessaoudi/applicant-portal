/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

import com.elm.shj.applicant.portal.services.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
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

    private final String COMMAND_INTEGRATION_AUTH_URL = "/ws/auth";
    /* lookups relative URLs */
    private final String RITUAL_TYPES_LOOKUP_URL = "/ws/ritual-type/list";
    private final String CARD_STATUSES_LOOKUP_URL = "/ws/card-status/list";
    private final String RELATIVE_RELATIONSHIPS_LOOKUP_URL = "/ws/relative-relationship/list";
    private final String MARITAL_STATUS_LOOKUP_URL = "/ws/marital-status/list";
    private final String COUNTRIES_LOOKUP_URL = "/ws/country/list";
    private final String HEALTH_SPECIAL_NEEDS_LOOKUP_URL = "/ws/health-special-needs/list";
    private final String RITUAL_SEASON_URL = "/ws/find/ritual-seasons";
    private final String RITUAL_LITE_URL = "/ws/find/ritual-lite";
    private final String RITUAL_LITE_LATEST_URL = "/ws/find/ritual-lite/latest";
    private final String APPLICANT_HEALTH_DETAILS_URL = "/ws/health";
    private final String APPLICANT_MAIN_DATA_URL = "/ws/find/main-data";
    private final String CARD_DETAILS_URL = "/ws/details";
    private final String APPLICANT_PACKAGE_URL = "/ws/applicant/package";
    private final String PACKAGE_CATERING_URL = "/ws/package/catering";
    private final String HOUSING_CATEGORY_LOOKUP_URL = "/ws/housing-category/list";
    private final String HOUSING_TYPES_LOOKUP_URL = "/ws/housing-type/list";
    private final String PACKAGE_TYPES_LOOKUP_URL = "/ws/package-type/list";
    private final String COMPANY_RITUAL_STEP_LOOKUP_URL = "/ws/company_ritual_step_label/list";
    private final String APPLICANT_TAFWEEJ_DETAILS_URL = "/ws/company-ritual-step";
    private final String APPLICANT_COMPANY_STAFF_DETAILS_URL = "/ws/find/company-employees";
    private final String COMPANY_STAFF_TITLE_LOOKUP_URL = "/ws/company_staff_title_label/list";
    private final String HOUSING_SITES_LOOKUP_URL = "/ws/housing-site/list";
    private final String TRANSPORTATION_TYPES_LOOKUP_URL = "/ws/transportation-type/list";
    private final String DIGITAL_ID_STATUS_LOOKUP_URL = "/ws/digital-id-status/list";
    private final String APPLICANT_RITUAL_SEASON_URL = "/ws/applicant/ritual-season";
    private final String APPLICANT_RITUAL_SEASON_LATEST_URL = "/ws/applicant/ritual-season/latest";
    private final String NOTIFICATION_URL = "/ws/notification";
    private final String NOTIFICATION_COUNT_URL = NOTIFICATION_URL + "/count-new-notifications/";
    private final String NOTIFICATION_CATEGORY_PREFERENCE_URL = NOTIFICATION_URL + "/user-notification-category-preference/";
    private final String PASSWORD_EXPIRY_NOTIFICATION_URL = NOTIFICATION_URL + "/password-expiry";
    private final String MARK_NOTIFICATIONS_READ_URL = NOTIFICATION_URL + "/mark-as-read";

    private final String COMPANY_DETAILS_URL = "/ws/company-details";
    private final String HEALTH_IMMUNIZATION_LOOKUP = "/ws/health-immunization/list";
    private final String RELIGIOUS_OCCASIONS_DAY_LOOKUP = "/ws/religious-occasions-day/list";
    private final String MEAL_TYPE_LOOKUP = "/ws/meal-type/list";
    private final String NOTIFICATION_CATEGORY_LOOKUP = "/ws/notification-category/list";
    private final String NOTIFICATION_NAME_LOOKUP = "/ws/notification-name/list";
    private final String NOTIFICATION_CATEGORY_UPDATE = NOTIFICATION_URL + "/update-user-notification-category-preference";
    private final String SUPPORTED_LANGUAGES_LOOKUP = "/ws/language/list";
    private final String HOUSING_DETAILS_URL = "/ws/housing";
    private final String INCIDENT_LIST = "/ws/incident/list/";
    private final String INCIDENT_TYPE_LOOKUP = "/ws/incident-type/list";
    private final String INCIDENT_STATUS_LOOKUP = "/ws/incident-status/list";
    private final String INCIDENT_DOWNLOAD = "/ws/incidents/attachment/";
    private final String CHAT_CONTACT_URL = "/ws/chat-contact";
    private final String INCIDENT_CREATE_URL = "/ws/incidents/create";
    private final String APPLICANT_RITUAL_URL = "/ws/ritual/";
    private final String APPLICANT_BY_UIN = "/ws/applicant/find-by-uin";
    private final String APPLICANT_PREFERRED_LANGUAGE = "/ws/applicant/language";
    private final String CHAT_LIST_URL = "/ws/chat-contact/chat-list";
    private final String APPLICANT_PACKAGE_RITUAL_URL = "/ws/applicant/applicant-ritual-seasons/";
    private final String LATEST_APPLICANT_RITUAL_SEASON__URL = "/ws/applicant/latest-ritual-season/";

    private final WebClient webClient;
    @Value("${admin.portal.url}")
    private String commandIntegrationUrl;
    @Value("${integration.access.username}")
    private String integrationAccessUsername;
    @Value("${integration.access.password}")
    private String integrationAccessPassword;

    /**
     * Call an integration web service, authenticate first to get the token then do the actual call using the generated token.
     *
     * @param serviceRelativeUrl    relative url of service to be called
     * @param httpMethod            HttpMethod of the service to be called
     * @param bodyToSend            body to send in the call, it can be null
     * @param responseTypeReference the return type contained in the WsResponse
     * @param <B>                   Request body type
     * @param <R>                   Response body type
     * @return WsResponse which contains status and body
     * @throws WsAuthenticationException thrown in case of failed authentication
     */
    //TODO must do refactor
    public <B, R> WsResponse callIntegrationWs2(String serviceRelativeUrl, HttpMethod httpMethod, B bodyToSend,
                                                ParameterizedTypeReference<WsResponse<R>> responseTypeReference) throws WsAuthenticationException {
        WsResponse<String> accessTokenWsResponse = webClient.post().uri(commandIntegrationUrl + COMMAND_INTEGRATION_AUTH_URL)
                .body(BodyInserters.fromValue(LoginRequestVo.builder().username(integrationAccessUsername).password(integrationAccessPassword).build()))
                .retrieve().bodyToMono(WsResponse.class).block();
        if (WsResponse.EWsResponseStatus.FAILURE == accessTokenWsResponse.getStatus()) {
            throw new WsAuthenticationException(accessTokenWsResponse.getBody());
        }


        if (bodyToSend != null) {
            return webClient.method(httpMethod).uri(commandIntegrationUrl + serviceRelativeUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .headers(header -> header.setBearerAuth(accessTokenWsResponse.getBody()))
                    .body(BodyInserters.fromMultipartData((MultiValueMap<String, HttpEntity<?>>) bodyToSend))
                    .retrieve().bodyToMono(responseTypeReference).block();
        } else {
            return webClient.method(httpMethod).uri(commandIntegrationUrl + serviceRelativeUrl)
                    .headers(header -> header.setBearerAuth(accessTokenWsResponse.getBody()))
                    .retrieve()
                    .bodyToMono(WsResponse.class).block();

        }


    }

    public <B, R> WsResponse callIntegrationWs(String serviceRelativeUrl, HttpMethod httpMethod, B bodyToSend,
                                               ParameterizedTypeReference<WsResponse<R>> responseTypeReference) throws WsAuthenticationException {
        WsResponse<String> accessTokenWsResponse = webClient.post().uri(commandIntegrationUrl + COMMAND_INTEGRATION_AUTH_URL)
                .body(BodyInserters.fromValue(LoginRequestVo.builder().username(integrationAccessUsername).password(integrationAccessPassword).build()))
                .retrieve().bodyToMono(WsResponse.class).block();
        if (WsResponse.EWsResponseStatus.FAILURE == accessTokenWsResponse.getStatus()) {
            // cannot authenticate, throw an exception
            throw new WsAuthenticationException(accessTokenWsResponse.getBody());
            // TODO: check available spring security exception to be reused instead.
        }
        // check if no body
        if (bodyToSend == null) {

            return webClient.method(httpMethod).uri(commandIntegrationUrl + serviceRelativeUrl).headers(header -> header.setBearerAuth(accessTokenWsResponse.getBody()))
                    .retrieve().bodyToMono(responseTypeReference).block();
        } else if (serviceRelativeUrl == INCIDENT_CREATE_URL ) {
            return webClient.method(httpMethod).uri(commandIntegrationUrl + serviceRelativeUrl).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED).headers(header -> header.setBearerAuth(accessTokenWsResponse.getBody()))
                    .body(BodyInserters.fromMultipartData((MultiValueMap<String, HttpEntity<?>>) bodyToSend)).retrieve().bodyToMono(responseTypeReference).block();
        }
        return webClient.method(httpMethod).uri(commandIntegrationUrl + serviceRelativeUrl).headers(header -> header.setBearerAuth(accessTokenWsResponse.getBody()))
                .body(BodyInserters.fromValue(bodyToSend)).retrieve().bodyToMono(responseTypeReference).block();
    }

    /**
     * Special web client for download attachment
     *
     * @param id
     * @return
     * @throws WsAuthenticationException
     */
    public ByteArrayResource downloadFromWs(long id) throws WsAuthenticationException {
        WsResponse<String> accessTokenWsResponse = webClient.post().uri(commandIntegrationUrl + COMMAND_INTEGRATION_AUTH_URL)
                .body(BodyInserters.fromValue(LoginRequestVo.builder().username(integrationAccessUsername).password(integrationAccessPassword).build()))
                .retrieve().bodyToMono(WsResponse.class).block();
        if (WsResponse.EWsResponseStatus.FAILURE == accessTokenWsResponse.getStatus()) {
            throw new WsAuthenticationException(accessTokenWsResponse.getBody());
            // TODO: check available spring security exception to be reused instead.
        }

        return webClient.get().uri(commandIntegrationUrl + INCIDENT_DOWNLOAD + id)
                .headers(header -> header.setBearerAuth(accessTokenWsResponse.getBody()))
                .retrieve().bodyToMono(ByteArrayResource.class).block();
    }

    /**
     * Load ritual types from command portal.
     *
     * @return
     */
    public List<RitualTypeLookupDto> loadRitualTypes() {
        WsResponse<List<RitualTypeLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(RITUAL_TYPES_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<RitualTypeLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load card statuses from command portal.
     *
     * @return list of card statuses or empty list in case of failed authentication
     */
    public List<CardStatusLookupDto> loadCardStatuses() {
        WsResponse<List<CardStatusLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CARD_STATUSES_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<CardStatusLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load relative relationships from command portal.
     *
     * @return
     */
    public List<RelativeRelationshipLookupDto> loadRelativeRelationships() {
        WsResponse<List<RelativeRelationshipLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(RELATIVE_RELATIONSHIPS_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<RelativeRelationshipLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load marital statues from command portal.
     *
     * @return
     */
    public List<MaritalStatusLookupDto> loadMaritalStatuses() {
        WsResponse<List<MaritalStatusLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(MARITAL_STATUS_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<MaritalStatusLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load countries from command portal.
     *
     * @return
     */
    public List<CountryLookupDto> loadCountries() {
        WsResponse<List<CountryLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(COUNTRIES_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<CountryLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load countries from command portal.
     *
     * @return
     */
    public List<HealthSpecialNeedsTypeLookupDto> loadSpecialNeedsTypes() {
        WsResponse<List<HealthSpecialNeedsTypeLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(HEALTH_SPECIAL_NEEDS_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<HealthSpecialNeedsTypeLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load ritual season by uin from command portal.
     *
     * @return
     */
    public List<Integer> loadRitualSeasonByUin(String uin) {
        WsResponse<List<Integer>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(RITUAL_SEASON_URL + "/" + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<Integer>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();

    }

    /**
     * Load ritual lite by uin and season from command portal.
     *
     * @return
     */
    public List<ApplicantRitualLiteDto> loadApplicantRitualByUinAndSeasons(String uin, int season) {
        WsResponse<List<ApplicantRitualLiteDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(RITUAL_LITE_URL + "/" + uin + "/" + season, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ApplicantRitualLiteDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();

    }

    /**
     * Load the latest ritual lite by uin from command portal.
     *
     * @return
     */
    public ApplicantRitualLiteDto loadApplicantRitualLatestByUin(String uin) {
        WsResponse<ApplicantRitualLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(RITUAL_LITE_LATEST_URL + "/" + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantRitualLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public ApplicantHealthLiteDto loadApplicantHealthDetails(String uin, Long ritualId) {
        WsResponse<ApplicantHealthLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_HEALTH_DETAILS_URL + "/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantHealthLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load applicant health details.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public ApplicantMainDataDto loadUserMainData(String uin, long ritualId) {
        WsResponse<ApplicantMainDataDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_MAIN_DATA_URL + "/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantMainDataDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load applicant main data details.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Load user card details by uin and ritual ID from command portal.
     *
     * @return
     */
    public ApplicantRitualCardLiteDto loadApplicantCardDetails(String uin, Long ritualId) {
        WsResponse<ApplicantRitualCardLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CARD_DETAILS_URL + "/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantRitualCardLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load applicant card details.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Load ritual types from command portal.
     *
     * @return
     */
    public ApplicantPackageDetailsDto loadApplicantPackageDetails(String uin, long companyRitualSeasonId) {
        WsResponse<ApplicantPackageDetailsDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_PACKAGE_URL + "/" + uin + "/" + companyRitualSeasonId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantPackageDetailsDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return null;
        }
        return wsResponse.getBody();
    }


    /**
     * Load package catering from command portal.
     *
     * @return
     */
    public List<PackageCateringDto> loadPackageCatering(String uin, long companyRitualSeasonId) {
        WsResponse<List<PackageCateringDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(PACKAGE_CATERING_URL + "/" + uin + "/" + companyRitualSeasonId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<PackageCateringDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load Package Catering.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Load housing categories from command portal.
     *
     * @return
     */
    public List<HousingCategoryLookupDto> loadHousingCategories() {
        WsResponse<List<HousingCategoryLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(HOUSING_CATEGORY_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<HousingCategoryLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load housing categories.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load housing types from command portal.
     *
     * @return
     */
    public List<HousingTypeLookupDto> loadHousingTypes() {
        WsResponse<List<HousingTypeLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(HOUSING_TYPES_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<HousingTypeLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load housing types.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load package types from command portal.
     *
     * @return
     */
    public List<PackageTypeLookupDto> loadPackageTypes() {
        WsResponse<List<PackageTypeLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(PACKAGE_TYPES_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<PackageTypeLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load package types.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public List<CompanyRitualStepLookupDto> loadRitualSteps() {
        WsResponse<List<CompanyRitualStepLookupDto>> wsResponse = null;
        try {

            wsResponse = callIntegrationWs(COMPANY_RITUAL_STEP_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<CompanyRitualStepLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public List<CompanyRitualStepMainDataDto> loadApplicantTafweejDetails(String uin, Long ritualId) {
        WsResponse<List<CompanyRitualStepMainDataDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_TAFWEEJ_DETAILS_URL + "/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<CompanyRitualStepMainDataDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load applicant health details.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public List<CompanyStaffDto> loadApplicantRelatedEmployeesDetails(String uin, Long ritualId) {
        WsResponse<List<CompanyStaffDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_COMPANY_STAFF_DETAILS_URL + "/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<CompanyStaffDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load applicant health details.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public List<CompanyStaffTitleLookupDto> loadCompanyStaffTitles() {
        WsResponse<List<CompanyStaffTitleLookupDto>> wsResponse = null;
        try {

            wsResponse = callIntegrationWs(COMPANY_STAFF_TITLE_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<CompanyStaffTitleLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load company staff title,", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public List<HousingSiteLookupDto> loadHousingSites() {
        WsResponse<List<HousingSiteLookupDto>> wsResponse = null;
        try {

            wsResponse = callIntegrationWs(HOUSING_SITES_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<HousingSiteLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load housing sites.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public List<TransportationTypeLookupDto> loadTransportationTypes() {
        WsResponse<List<TransportationTypeLookupDto>> wsResponse = null;
        try {

            wsResponse = callIntegrationWs(TRANSPORTATION_TYPES_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<TransportationTypeLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public List<ApplicantDigitalIdStatusLookupDto> loadDigitalIdStatuses() {
        WsResponse<List<ApplicantDigitalIdStatusLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(DIGITAL_ID_STATUS_LOOKUP_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ApplicantDigitalIdStatusLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load digital ID statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load the latest season ritual lite by uin from command portal.
     *
     * @return
     */
    public CompanyRitualSeasonLiteDto loadLatestApplicantRitualSeasonByUin(String uin) {
        WsResponse<CompanyRitualSeasonLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_RITUAL_SEASON_LATEST_URL + "/" + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<CompanyRitualSeasonLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Load the all season ritual lite by uin from command portal.
     *
     * @return
     */
    public List<CompanyRitualSeasonLiteDto> loadAllApplicantRitualSeasonByUin(String uin) {
        WsResponse<List<CompanyRitualSeasonLiteDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_RITUAL_SEASON_URL + "/" + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<CompanyRitualSeasonLiteDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load card statuses.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * finds user notifications by user's UIN
     *
     * @param uin the UIN of user to find notifications for
     * @return the user notifications
     */
    public List<DetailedUserNotificationDto> findUserNotificationsByUin(String uin) {
        WsResponse<List<DetailedUserNotificationDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(NOTIFICATION_URL + "/" + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<DetailedUserNotificationDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notifications by user Id.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }


    public Page<DetailedUserNotificationDto> findTypedUserNotificationsByUin(String uin, String type, Pageable pageable) {
        WsResponse<Page<DetailedUserNotificationDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(NOTIFICATION_URL + "/" + uin + "?type=" + type +
                            "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<RestResponsePage<DetailedUserNotificationDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notifications by user Id.", e);
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        return wsResponse.getBody();
    }

    /**
     * Count user new notifications.
     *
     * @param uin
     * @return number of user new notifications or 0 in case of exception.
     */
    public UserNewNotificationsCountVo countUserNewNotifications(String uin) {
        WsResponse<UserNewNotificationsCountVo> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(NOTIFICATION_COUNT_URL + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<UserNewNotificationsCountVo>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get user new notifications count for user with UIN {}.", uin, e);
            return UserNewNotificationsCountVo.builder().build();
        }
        return wsResponse.getBody();
    }

    /**
     * send user notifications Request
     *
     * @param passwordExpiryNotificationRequest the request body to be send to save notification
     * @return the User Notifications
     */
    public void sendPasswordExpiryNotificationRequest(PasswordExpiryNotificationRequest passwordExpiryNotificationRequest) {
        try {
            callIntegrationWs(PASSWORD_EXPIRY_NOTIFICATION_URL, HttpMethod.POST, passwordExpiryNotificationRequest,
                    new ParameterizedTypeReference<WsResponse<Object>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to send Password Expiry Notification Request", e);
        }
    }


    /**
     * mark User Notification As Read
     *
     * @param notificationId the id of the notification to be marked as read
     * @return the User Notifications count of affected rows
     */
    public int markUserNotificationAsRead(Long notificationId) {
        WsResponse<Integer> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(MARK_NOTIFICATIONS_READ_URL + "/" + notificationId, HttpMethod.POST, null,
                    new ParameterizedTypeReference<WsResponse<Integer>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to mark User Notification As Read", e);
        }
        return wsResponse.getBody();
    }


    public CompanyLiteDto loadCompanyDetails(String uin, Long ritualId) {
        WsResponse<CompanyLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(COMPANY_DETAILS_URL + "/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<CompanyLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load company details.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Load health immunization  from command portal.
     *
     * @return
     */
    public List<HealthImmunizationLookupDto> loadHealthImmunizations() {
        WsResponse<List<HealthImmunizationLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(HEALTH_IMMUNIZATION_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<HealthImmunizationLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load health immunization.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Load religious occasions day  from command portal.
     *
     * @return
     */
    public List<ReligiousOccasionsDayLookupDto> loadReligiousOccasionsDay() {
        WsResponse<List<ReligiousOccasionsDayLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(RELIGIOUS_OCCASIONS_DAY_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ReligiousOccasionsDayLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load religious occasions day.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }


    /**
     * Load meal types  from command portal.
     *
     * @return
     */
    public List<MealTypeLookupDto> loadMealTypes() {
        WsResponse<List<MealTypeLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(MEAL_TYPE_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<MealTypeLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load meal types.", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Find user notification category preference from command portal.
     *
     * @param userId
     * @return
     */
    public List<UserNotificationCategoryPreferenceDto> findUserNotificationCategoryPreference(String userId) {
        WsResponse<List<UserNotificationCategoryPreferenceDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(NOTIFICATION_CATEGORY_PREFERENCE_URL + userId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<UserNotificationCategoryPreferenceDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get user notification category preference by {} user Id.", userId, e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Find notification category lookup from command portal.
     *
     * @return
     */
    public List<NotificationCategoryLookupDto> loadNotificationCategories() {
        WsResponse<List<NotificationCategoryLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(NOTIFICATION_CATEGORY_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<NotificationCategoryLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification categories", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Find notification name lookup from command portal.
     *
     * @return
     */
    public List<NotificationTemplateNameLookupDto> loadNotificationNames() {
        WsResponse<List<NotificationTemplateNameLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(NOTIFICATION_NAME_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<NotificationTemplateNameLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification names", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Find all supported languages' lookup from command portal.
     *
     * @return all Supported Languages
     */
    public List<LanguageLookupDto> loadSupportedLanguages() {
        WsResponse<List<LanguageLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(SUPPORTED_LANGUAGES_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<LanguageLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get supported languages", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public UserNotificationCategoryPreferenceDto save(UserNotificationCategoryPreferenceDto userNotificationCategoryPreference) {
        WsResponse<UserNotificationCategoryPreferenceDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(NOTIFICATION_CATEGORY_UPDATE, HttpMethod.POST, userNotificationCategoryPreference,
                    new ParameterizedTypeReference<WsResponse<UserNotificationCategoryPreferenceDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification names", e);
            return null;
        }
        return wsResponse.getBody();
    }


    public ApplicantIncidentDto createIncident(MultipartBodyBuilder builder) {
        WsResponse<ApplicantIncidentDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(INCIDENT_CREATE_URL, HttpMethod.POST, builder.build(),
                    new ParameterizedTypeReference<WsResponse<ApplicantIncidentDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to create incident", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public PackageHousingDto loadHousingDetails(String uin, long ritualId) {
        WsResponse<PackageHousingDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(HOUSING_DETAILS_URL + "/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<PackageHousingDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load housing details.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Find all list of incidents
     *
     * @return list of incident
     */
    public List<ApplicantIncidentDto> loadIncidents(long applicantRitualId) {
        WsResponse<List<ApplicantIncidentDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(INCIDENT_LIST + applicantRitualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ApplicantIncidentDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get incidents", e);
            return Collections.emptyList();
        }
        if (wsResponse == null) {
            return new ArrayList<>();
        }
        return wsResponse.getBody();
    }

    public List<IncidentStatusLookupDto> loadIncidentStatus() {
        WsResponse<List<IncidentStatusLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(INCIDENT_STATUS_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<IncidentStatusLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get incident status", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public List<IncidentTypeLookupDto> loadIncidentTypes() {
        WsResponse<List<IncidentTypeLookupDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(INCIDENT_TYPE_LOOKUP, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<IncidentTypeLookupDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get supported languages", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * Find applicant chat contacts from command portal.
     *
     * @return all chat contacts by ritual ID
     */
    public List<ApplicantChatContactLiteDto> findApplicantChatContacts(String uin, Long applicantRitualId) {
        WsResponse<List<ApplicantChatContactLiteDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs2(CHAT_CONTACT_URL + "/" + uin + "/" + applicantRitualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ApplicantChatContactLiteDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification names", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public WsResponse findOneApplicantByUinAndRitualId(String uin, Long applicantRitualId, String applicantUin) {
        WsResponse wsResponse = null;
        try {
            wsResponse = callIntegrationWs2(CHAT_CONTACT_URL + "/find-one/" + uin + "/" + applicantRitualId + "/" + applicantUin,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification names", e);
            return null;
        }
        return wsResponse;
    }

    /**
     * Create applicant chat contact.
     *
     * @return the persisted chat contact
     */
    public ApplicantChatContactLiteDto createApplicantChatContact(Long ritualId, ApplicantChatContactLiteDto applicantChatContact) {
        WsResponse<ApplicantChatContactLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CHAT_CONTACT_URL + "/create/"  + ritualId,
                    HttpMethod.POST,  applicantChatContact,
                    new ParameterizedTypeReference<WsResponse<ApplicantChatContactLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to create applicant chat contact", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Create staff chat contact.
     *
     * @return the persisted chat contact
     */
    public ApplicantChatContactLiteDto createStaffChatContact(String uin, Long applicantRitualId, String contactUin) {
        WsResponse<ApplicantChatContactLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CHAT_CONTACT_URL + "/create-staff/" + uin + "/" + applicantRitualId + "/" + contactUin,
                    HttpMethod.POST, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantChatContactLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to create applicant chat contact", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Updates user defined applicant chat contact.
     *
     * @return the updated chat contact
     */
    public ApplicantChatContactLiteDto updateChatContact(long id, ApplicantChatContactLiteDto applicantChatContact) {
        WsResponse<ApplicantChatContactLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CHAT_CONTACT_URL + "/update/" + id,
                    HttpMethod.PUT, applicantChatContact,
                    new ParameterizedTypeReference<WsResponse<ApplicantChatContactLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to update applicant chat contact", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public ApplicantRitualDto findApplicantRitual(String uin, long companyRitualId) {
        WsResponse<ApplicantRitualDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_RITUAL_URL + "/" + uin + "/" + companyRitualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantRitualDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load incidents.", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public byte[] getAttachment(long id) {
        ByteArrayResource file;
        try {
            file = downloadFromWs(id);
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to incident types", e);
            return null;
        }
        return file.getByteArray();
    }

    public ApplicantLiteDto findApplicantBasicDetailsByUin(String uin) {
        WsResponse<ApplicantLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_BY_UIN + "/" + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification names", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * Delete applicant chat contact.
     *
     * @return number of rows affected
     */
    public String deleteChatContact(String applicantUin, String contactUin) {
        WsResponse<String> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CHAT_CONTACT_URL + "/delete/" + applicantUin + "/" + contactUin,
                    HttpMethod.POST, null,
                    new ParameterizedTypeReference<WsResponse<String>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to update applicant chat contact", e);
            return null;
        }
        return wsResponse.getBody();
    }


    public CompanyStaffLiteDto findStaffContact(String suin) {
        WsResponse<CompanyStaffLiteDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CHAT_CONTACT_URL + "/find-staff/" + suin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<CompanyStaffLiteDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get staff chat contact data", e);
            return null;
        }
        return wsResponse.getBody();
    }

    public void updatePreferredLanguage(long uin, String lang) {
        try {
            callIntegrationWs(APPLICANT_PREFERRED_LANGUAGE + "/" + uin + "/" + lang,
                    HttpMethod.PUT, null,
                    new ParameterizedTypeReference<WsResponse<String>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to update applicant preferred language", e);
        }
    }

    /**
     *
     * @param uin
     * @return
     */
    public List<ApplicantRitualSeasonVo> findApplicantPackageAndRitualSeasonByUin(long uin) {
        WsResponse<List<ApplicantRitualSeasonVo>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(APPLICANT_PACKAGE_RITUAL_URL + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ApplicantRitualSeasonVo>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get list of applicant ritual", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }



    public List<ChatMessageLiteDto> listChatContactsWithLatestMessage(String uin) {
        WsResponse<List<ChatMessageLiteDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs2(CHAT_LIST_URL + "/" + uin,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ApplicantChatContactLiteDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification names", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    public List<ChatMessageDto> listMessages(long contactId) {
        WsResponse<List<ChatMessageDto>> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CHAT_CONTACT_URL + "/messages/" + contactId,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<List<ChatMessageDto>>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get notification names", e);
            return Collections.emptyList();
        }
        return wsResponse.getBody();
    }

    /**
     * save user chat message.
     *
     * @return the saved chat message
     */
    public ChatMessageDto saveSenderMessage(ChatMessageDto chatMessage) {
        WsResponse<ChatMessageDto> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(CHAT_CONTACT_URL + "/save-chat-message",
                    HttpMethod.POST, chatMessage,
                    new ParameterizedTypeReference<WsResponse<ChatMessageDto>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to update applicant chat contact", e);
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     *
     * @param uin
     * @return
     */
    public ApplicantRitualSeasonVo findLatestApplicantRitualSeason(long uin) {
        WsResponse<ApplicantRitualSeasonVo> wsResponse = null;
        try {
            wsResponse = callIntegrationWs(LATEST_APPLICANT_RITUAL_SEASON__URL + uin, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantRitualSeasonVo>>() {
                    });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to get list of applicant ritual", e);
            return null;
        }
        return wsResponse.getBody();
    }
}
