package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.ValidateApplicantCmd;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationWsControllerTest extends AbstractControllerTestSuite {

    private UserDto user;
    private static final String TEST_UIN = "502087000000";

    @Override
    public void setUp() throws Exception {
        user = buildUser();
        mockSuccessfulLogin();
        triggerLogin();
    }

    @Override
    public void tearDown() {

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

    @Test
    public void test_verify_success() throws Exception {
        String url = Navigation.API_INTEGRATION_REGISTRATION + "/verify";

        ValidateApplicantCmd command = new ValidateApplicantCmd();
        command.setIdentifier(TEST_UIN);

        when(userService.verify(any())).thenReturn(new ApplicantLiteDto());
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_UTF8).header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.WEB_SERVICE_CALLER_TYPE)
                .content(objectToJson(command)).with(csrf()))
                .andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void test_verify_user_already_register() throws Exception {
        ValidateApplicantCmd applicant = new ValidateApplicantCmd();
        applicant.setIdentifier(TEST_UIN);

        when(otpService.createOtp(anyString(),anyInt(), anyString())).thenReturn("");
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.of(new UserDto()));

        mockMvc.perform(post(Navigation.API_INTEGRATION_REGISTRATION + "/verify").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(applicant)).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.error", is(WsError.EWsError.ALREADY_REGISTERED.getCode())));

    }

    @Test
    public void test_verify_applicant_not_found_in_admin_portal() throws Exception {

        ValidateApplicantCmd applicant = new ValidateApplicantCmd();
        applicant.setIdentifier(TEST_UIN);

        when(userService.verify(any())).thenReturn(null);
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());
        mockMvc.perform(post(Navigation.API_INTEGRATION_REGISTRATION + "/verify").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(applicant)).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.error", is(WsError.EWsError.NOT_FOUND_IN_ADMIN.getCode())));
    }

    @Test
    public void test_otp() throws Exception {

        when(otpGenerator.generateOtp(any())).thenReturn("123");

        mockMvc.perform(post(Navigation.API_INTEGRATION_REGISTRATION + "/otp").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.principal", is(Long.parseLong(TEST_UIN))));

    }

    @Test
    public void test_register_user_success() throws Exception {

        when(userService.createUser(any())).thenReturn(new UserDto());
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(post(Navigation.API_INTEGRATION_REGISTRATION + "?uadmin=false").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void test_register_user_admin_portal_update() throws Exception {

        when(userService.createUser(any())).thenReturn(new UserDto());
        when(userService.findByUin(anyLong())).thenReturn(java.util.Optional.empty());
        when(userService.updateUserInAdminPortal(any())).thenReturn(new ApplicantLiteDto());

        mockMvc.perform(post(Navigation.API_INTEGRATION_REGISTRATION + "?uadmin=true").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(user)).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(userService, times(1)).updateUserInAdminPortal(any());
        verify(userService, times(1)).createUser(user);
    }
}
