/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

import com.elm.shj.applicant.portal.services.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final WebClient webClient;
    @Value("${admin.portal.url}")
    private String commandIntegrationUrl;
    @Value("${integration.access.username}")
    private String integrationAccessUsername;
    @Value("${integration.access.password}")
    private String integrationAccessPassword;

    private final ObjectMapper mapper;

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
        }
        return webClient.method(httpMethod).uri(commandIntegrationUrl + serviceRelativeUrl).headers(header -> header.setBearerAuth(accessTokenWsResponse.getBody()))
                .body(BodyInserters.fromValue(bodyToSend)).retrieve().bodyToMono(responseTypeReference).block();
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
            wsResponse = callIntegrationWs(CARD_DETAILS_URL +"/" + uin + "/" + ritualId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<WsResponse<ApplicantRitualCardLiteDto>>() {});
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to load applicant card details.", e);
            return null;
        }
        return wsResponse.getBody();
    }
}
