package com.elm.shj.applicant.portal.services.integration;

import com.elm.shj.applicant.portal.services.dto.ApplicantMainDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;


/**
 * Testing class for service {@link IntegrationService}
 *
 * @author Ahmed Elsayed
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class IntegrationServiceTest {

    @InjectMocks
    private IntegrationService serviceToTest;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @Mock
    private WebClient.RequestBodyUriSpec uriSpecMock;

    @Mock
    private WebClient.RequestHeadersSpec headersSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;


    @BeforeEach
    public void setUp() {

        configWebClient();

        WsResponse<String> accessTokenWsResponse = new WsResponse<>();
        accessTokenWsResponse.setStatus(WsResponse.EWsResponseStatus.SUCCESS);
        accessTokenWsResponse.setBody("TOKEN");

        when(responseSpecMock.bodyToMono(WsResponse.class))
                .thenReturn(Mono.just(accessTokenWsResponse));
    }

    @Test
    public void test_load_user_main_data() {

        ApplicantMainDataDto applicantMainDataDto = new ApplicantMainDataDto();

        WsResponse<ApplicantMainDataDto> wsResponse = new WsResponse<>();
        wsResponse.setStatus(WsResponse.EWsResponseStatus.SUCCESS);
        wsResponse.setBody(applicantMainDataDto);

        mockWebClientResponse(wsResponse, new ParameterizedTypeReference<WsResponse<ApplicantMainDataDto>>() {
        });

        ApplicantMainDataDto response = serviceToTest.loadUserMainData("123", 2);

        assertEquals(applicantMainDataDto, response);
    }


    private void configWebClient() {

        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(webClient.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpecMock);

        when(requestBodyUriSpecMock.uri(ArgumentMatchers.<String>notNull())).thenReturn(uriSpecMock);

        when(uriSpecMock.retrieve()).thenReturn(responseSpecMock);

        when(uriSpecMock.body(ArgumentMatchers.<BodyInserter>notNull())).thenReturn(headersSpecMock);

        when(uriSpecMock.headers(notNull())).thenReturn(uriSpecMock);

        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);


    }

    private <R> void mockWebClientResponse(final WsResponse<R> resp, ParameterizedTypeReference<WsResponse<R>> responseTypeReference) {

        when(responseSpecMock.bodyToMono(responseTypeReference))
                .thenReturn(Mono.just(resp));

    }

}
