/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.registration;

import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test @RegistrationController.
 *
 * @author Ahmad Flaifel (aflaifel@elm.sa)
 */
public class RegistrationControllerTest extends AbstractControllerTestSuite {


    private UserDto user;
    private static final String RECAPTCHA_DUMMY_VALUE = "recaptcha";
    private RecaptchaInfo recaptchaInfo;

    @Test
    public void test_register_user_with_validation_error() throws Exception {
        user.setNin(null);
        mockMvc.perform(post(Navigation.API_REGISTRATION + "?" + RegistrationController.RECAPTCHA_TOKEN_NAME + "=").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors").exists());
    }


    @Test
    public void test_register_user_success() throws Exception {

        when(userService.createUser(any(), anyBoolean())).thenReturn(new UserDto());

        mockMvc.perform(post(Navigation.API_REGISTRATION + "?" + RegistrationController.RECAPTCHA_TOKEN_NAME + "=" + RECAPTCHA_DUMMY_VALUE).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(1)).createUser(user, true);
    }


    @Test
    public void test_register_user_no_recaptcha() throws Exception {

        when(userService.createUser(any(), anyBoolean())).thenReturn(new UserDto());

        mockMvc.perform(post(Navigation.API_REGISTRATION).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(0)).createUser(user, true);
    }


    @Test
    public void test_register_user_invalid_recaptcha() throws Exception {

        when(userService.createUser(any(), anyBoolean())).thenReturn(new UserDto());

        mockMvc.perform(post(Navigation.API_REGISTRATION).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(0)).createUser(user, true);
    }

    /**
     * Build UserDto instance with required fields for registration
     */
    private UserDto buildUser() {
        UserDto user = new UserDto();
        user.setFullNameEn("first name");
        user.setFullNameAr("");
        user.setPassword("C0mpl3xP@ss");
        user.setDateOfBirthHijri(14101010);
        user.setEmail("email@company.com");
        user.setMobileNumber(512345678);
        user.setNin(Long.parseLong(TEST_USER_NIN));
        return user;
    }

    /**
     * Build recaptcha response with success status
     */
    private void buildRecaptchaInfo() {
        this.recaptchaInfo = new RecaptchaInfo();
        this.recaptchaInfo.setSuccess(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() {
        user = buildUser();

        buildRecaptchaInfo();
        when(recaptchaService.verifyRecaptcha(anyString(), anyString(), Mockito.eq(Boolean.TRUE), any())).thenReturn(this.recaptchaInfo);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tearDown() {
    }
}
