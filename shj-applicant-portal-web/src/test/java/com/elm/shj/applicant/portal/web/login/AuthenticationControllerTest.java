/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.login;

import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for controller {@link AuthenticationController}
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public class AuthenticationControllerTest extends AbstractControllerTestSuite {

    private static final String TEST_ANYTHING = "00000000000";

    @Autowired
    private AuthenticationController authenticationController;

    @Test
    public void test_login_success() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk());
    }

    @Test
    public void test_login_deactivated() throws Exception {
        loggedInUser.setActivated(false);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isInternalServerError());
        loggedInUser.setActivated(true);
    }

    @Test
    public void test_login_many_no_recaptcha() throws Exception {
        loggedInUser.setNumberOfTries(5);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().is(555));
        loggedInUser.setNumberOfTries(0);
    }

    @Test
    public void test_login_many_number_of_tries() throws Exception {
        loggedInUser.setNumberOfTries(5);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_AUTH + "/login?grt=GOOGLE_RECAPTCHA_RESPONSE").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().is(555));
        loggedInUser.setNumberOfTries(0);
    }

    @Test
    public void test_login_invalid_recaptcha() throws Exception {
        loggedInUser.setNumberOfTries(5);
        when(recaptchaService.verifyRecaptcha(any(), any())).thenReturn(new RecaptchaInfo(false));
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_AUTH + "/login?grt=GOOGLE_RECAPTCHA_RESPONSE").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().is(555));
        loggedInUser.setNumberOfTries(0);
    }

    @Test
    public void test_login_with_recaptcha() throws Exception {
        loggedInUser.setNumberOfTries(5);
        when(recaptchaService.verifyRecaptcha(any(), any())).thenReturn(new RecaptchaInfo(true));
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_AUTH + "/login?grt=GOOGLE_RECAPTCHA_RESPONSE").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk());
        loggedInUser.setNumberOfTries(0);
    }

    @Test
    public void test_login_bad_credentials() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_ANYTHING);
        mockMvc.perform(post(Navigation.API_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isInternalServerError());
    }

    @Test
    public void test_login_user_not_exist() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_ANYTHING);
        credentials.put("password", TEST_ANYTHING);
        mockMvc.perform(post(Navigation.API_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().is(AuthenticationController.INVALID_RECAPTCHA_RESPONSE_CODE));
    }

    @Test
    public void test_logout_after_login() throws Exception {
        ReflectionTestUtils.setField(authenticationController, "simultaneousLoginEnabled", false);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);
        mockMvc.perform(post(Navigation.API_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk()).andDo(result -> {
            Cookie tokenCookie = result.getResponse().getCookie(JwtTokenService.TOKEN_COOKIE_NAME);
            assertNotNull(tokenCookie);

            mockMvc.perform(post(Navigation.API_AUTH + "/logout").with(csrf()).cookie(tokenCookie).contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk()).andDo(response -> assertNull(SecurityContextHolder.getContext().getAuthentication()));
        });
    }

    @Test
    public void test_logout_empty_token() throws Exception {
        mockMvc.perform(post(Navigation.API_AUTH + "/logout").contentType(MediaType.APPLICATION_JSON_UTF8).with(csrf()))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() {
        mockSuccessfulLogin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tearDown() {
        ReflectionTestUtils.setField(authenticationController, "simultaneousLoginEnabled", true);
    }

}
