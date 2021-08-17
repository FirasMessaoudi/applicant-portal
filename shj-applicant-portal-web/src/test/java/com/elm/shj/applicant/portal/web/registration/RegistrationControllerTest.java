/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.registration;

import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.ValidateApplicantCmd;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

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
    private ValidateApplicantCmd command;
    private static final String RECAPTCHA_DUMMY_VALUE = "recaptcha";
    private RecaptchaInfo recaptchaInfo;
    private static final String TEST_UIN = "1234567899";
    private static final int TEST_MOBILE = 12345678;
    private static final String TEST_OTP = "1234";
    private static final String TEST_WRONG_OTP = "4321";
    private static final int USER_ALREADY_REGISTERED_RESPONSE_CODE = 560;
    private static final int USER_NOT_FOUND_IN_ADMIN_PORTAL_RESPONSE_CODE = 561;

    @Test
    public void test_register_user_with_validation_error() throws Exception {

        mockMvc.perform(post(Navigation.API_REGISTRATION).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors").exists());
    }


    @Test
    public void test_register_user_already_registered() throws Exception {

        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.of(new UserDto()));

        mockMvc.perform(post(Navigation.API_REGISTRATION + "?uadmin=false").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().is(USER_ALREADY_REGISTERED_RESPONSE_CODE));

        verify(userService, times(0)).createUser(user);
    }


    @Test
    public void test_register_user_success() throws Exception {

        when(userService.createUser(any())).thenReturn(new UserDto());
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(post(Navigation.API_REGISTRATION + "?uadmin=false").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void test_verify_user_already_register() throws Exception {
        ValidateApplicantCmd applicant = new ValidateApplicantCmd();
        applicant.setUin("1234567898");

        when(otpService.createOtp(anyString(), anyString())).thenReturn("");
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.of(new UserDto()));
        mockMvc.perform(post(Navigation.API_REGISTRATION + "/verify").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(applicant)).with(csrf())).andDo(print()).andExpect(status().is(USER_ALREADY_REGISTERED_RESPONSE_CODE));
        verify(userService, times(0)).verify(eq(command));
    }

    @Test
    public void test_verify_applicant_not_found_in_admin_portal() throws Exception {
        ValidateApplicantCmd applicant = new ValidateApplicantCmd();
        applicant.setUin("1234567898");

        when(userService.verify(any())).thenReturn(null);
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());
        mockMvc.perform(post(Navigation.API_REGISTRATION + "/verify").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(applicant)).with(csrf())).andDo(print()).andExpect(status().is(USER_NOT_FOUND_IN_ADMIN_PORTAL_RESPONSE_CODE));
    }

    @Test
    public void test_register_user_admin_portal_update() throws Exception {

        when(userService.createUser(any())).thenReturn(new UserDto());
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());
        when(userService.updateUserInAdminPortal(any())).thenReturn(new ApplicantLiteDto());

        mockMvc.perform(post(Navigation.API_REGISTRATION + "?uadmin=true").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(userService, times(1)).updateUserInAdminPortal(any());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void test_verify_success() throws Exception {
        ValidateApplicantCmd applicant = new ValidateApplicantCmd();
        applicant.setUin("1234567898");

        when(userService.verify(any())).thenReturn(new ApplicantLiteDto());
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());
        mockMvc.perform(post(Navigation.API_REGISTRATION + "/verify").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(applicant)).with(csrf())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void test_otp() throws Exception {
        ValidateApplicantCmd applicant = new ValidateApplicantCmd();
        applicant.setUin("1234567898");

        mockMvc.perform(post(Navigation.API_REGISTRATION + "/otp?" + RegistrationController.RECAPTCHA_TOKEN_NAME + "=" + RECAPTCHA_DUMMY_VALUE).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());
        when(otpGenerator.generateOtp(any())).thenReturn(TEST_OTP);
        when(otpService.createOtp(anyString(), anyString())).thenReturn(TEST_OTP);
    }

    @Test
    public void test_otp_validate() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("uin", TEST_UIN);
        credentials.put("otp", TEST_OTP);
        when(otpService.validateOtp(anyString(), anyString())).thenReturn(true);
        mockMvc.perform(post(Navigation.API_REGISTRATION + "/otp/validate").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void test_register_user_no_recaptcha() throws Exception {

        when(userService.createUser(any())).thenReturn(new UserDto());

        mockMvc.perform(post(Navigation.API_REGISTRATION).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(0)).createUser(user);
    }


    @Test
    public void test_register_user_invalid_recaptcha() throws Exception {

        when(userService.createUser(any())).thenReturn(new UserDto());

        mockMvc.perform(post(Navigation.API_REGISTRATION).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(0)).createUser(user);
    }

    /**
     * Build UserDto instance with required fields for registration
     */
    private UserDto buildUser() {
        UserDto user = new UserDto();
        user.setFullNameEn("first name");
        user.setFullNameAr("ahmed mohammed ali");
        user.setPassword("C0mpl3xP@ss");
        user.setDateOfBirthHijri(14101010);
        user.setEmail("email@company.com");
        user.setMobileNumber("512345678");
        user.setUin(Long.parseLong(TEST_UIN));
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
