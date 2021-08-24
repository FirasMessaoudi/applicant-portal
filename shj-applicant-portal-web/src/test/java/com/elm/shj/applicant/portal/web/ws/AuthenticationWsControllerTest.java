package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.login.AuthenticationController;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationWsControllerTest extends AbstractControllerTestSuite {

    @Autowired
    private AuthenticationController authenticationController;

    @Override
    public void setUp() throws Exception {
        mockSuccessfulLogin();
    }

    @Override
    public void tearDown() {

    }

    @Test
    public void test_login_success() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_INTEGRATION_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.TOKEN_COOKIE_NAME)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk());
    }

    @Test
    public void test_login_many_number_of_tries() throws Exception {
        loggedInUser.setNumberOfTries(5);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_INTEGRATION_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.WEB_SERVICE_CALLER_TYPE)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.error", is(WsError.EWsError.EXCEEDED_NUMBER_OF_TRIES.getCode())));
        loggedInUser.setNumberOfTries(0);
    }


    @Test
    public void test_login_otp_success() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("otp", "111");
        mockMvc.perform(post(Navigation.API_INTEGRATION_AUTH + "/otp").contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.TOKEN_COOKIE_NAME)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk()).andDo(result -> {
            String token = result.getResponse().getHeader(JwtTokenService.JWT_HEADER_NAME);
            assertNotNull(token);
        });
    }


    @Test
    public void test_logout_after_login() throws Exception {
        ReflectionTestUtils.setField(authenticationController, "simultaneousLoginEnabled", false);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_INTEGRATION_AUTH + "/otp").contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.TOKEN_COOKIE_NAME)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk()).andDo(result -> {
            String token = result.getResponse().getHeader(JwtTokenService.JWT_HEADER_NAME);
            assertNotNull(token);

            mockMvc.perform(post(Navigation.API_INTEGRATION_AUTH + "/logout").with(csrf()).header("Authorization","Bearer "+token)
                    .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.TOKEN_COOKIE_NAME)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk()).andDo(response -> assertNull(SecurityContextHolder.getContext().getAuthentication()));
        });
    }
}
