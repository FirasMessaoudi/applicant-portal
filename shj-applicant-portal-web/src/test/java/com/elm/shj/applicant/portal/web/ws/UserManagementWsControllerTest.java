package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.admin.ChangePasswordCmd;
import com.elm.shj.applicant.portal.web.admin.ResetPasswordCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserManagementWsControllerTest extends AbstractControllerTestSuite {

    private UserDto user;
    private static final String TEST_UIN = "502087000000";

    @Override
    public void setUp() throws Exception {
        user = buildUser();
        mockSuccessfulLogin();
        triggerLogin();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        when(userService.retrievePasswordHash(any(Long.class))).thenReturn(encoder.encode(TEST_USER_PASSWORD));

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
    public void test_reset_user_password_success() throws Exception {
        String newPassword = "newC0mpl@x";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date birthdate = sdf.parse("1994-11-27");
        long uin = 12345678;
        String url = Navigation.API_INTEGRATION_USERS + "/reset-password";

        ResetPasswordCmd params = new ResetPasswordCmd();
        params.setDateOfBirthGregorian(birthdate);
        params.setIdNumber(uin);

        UserDto user = new UserDto();
        user.setDateOfBirthGregorian(birthdate);
        user.setUin(uin);

        when(userService.findByUin(uin)).thenReturn(Optional.of(user));

        mockMvc.perform(post(url).content(objectToJson(params)).contentType(MediaType.APPLICATION_JSON_UTF8).with(csrf())).andDo(print()).andExpect(status().isOk());

        verify(userService, times(1)).resetPassword(user);
    }

    @Test
    public void test_change_user_password_invalid() throws Exception {
        String newPassword = "invalidPassword";
        String url = Navigation.API_INTEGRATION_USERS + "/change-password";

        ChangePasswordCmd params = new ChangePasswordCmd();
        params.setOldPassword(TEST_USER_PASSWORD);
        params.setNewPassword(newPassword);
        params.setNewPasswordConfirm(newPassword);

        mockMvc.perform(post(url).cookie(tokenCookie).content(objectToJson(params)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.WEB_SERVICE_CALLER_TYPE).with(csrf())).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void test_change_user_password_empty() throws Exception {
        String newPassword = "";
        String url = Navigation.API_INTEGRATION_USERS + "/change-password";

        ChangePasswordCmd params = new ChangePasswordCmd();
        params.setOldPassword(TEST_USER_PASSWORD);
        params.setNewPassword(newPassword);
        params.setNewPasswordConfirm(newPassword);

        mockMvc.perform(post(url).cookie(tokenCookie).content(objectToJson(params)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.WEB_SERVICE_CALLER_TYPE).with(csrf())).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void test_change_user_password_does_not_match() throws Exception {
        String oldPasswordDoesNotMatch = TEST_USER_PASSWORD + "Delta";
        String newPassword = "newC0mpl@x";
        String url = Navigation.API_INTEGRATION_USERS + "/change-password";

        ChangePasswordCmd params = new ChangePasswordCmd();
        params.setOldPassword(oldPasswordDoesNotMatch);
        params.setNewPassword(newPassword);
        params.setNewPasswordConfirm(newPassword);

        mockMvc.perform(post(url).cookie(tokenCookie).content(objectToJson(params)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.WEB_SERVICE_CALLER_TYPE).with(csrf())).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.body.error", is(WsError.EWsError.BAD_CREDENTIALS.getCode())));
    }

    @Test
    public void test_change_user_password_contains_username() throws Exception {
        String newPassword = "P@ssw0rd" + TEST_USER_NIN;
        String url = Navigation.API_INTEGRATION_USERS + "/change-password";

        ChangePasswordCmd params = new ChangePasswordCmd();
        params.setOldPassword(TEST_USER_PASSWORD);
        params.setNewPassword(newPassword);
        params.setNewPasswordConfirm(newPassword);

        mockMvc.perform(post(url).cookie(tokenCookie).content(objectToJson(params)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.WEB_SERVICE_CALLER_TYPE).with(csrf())).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.body.error", is(WsError.EWsError.PWRD_CONTAINS_USERNAME_ERROR.getCode())));
    }

    @Test
    public void test_change_user_password_success() throws Exception {
        String newPassword = "newC0mpl@x";
        String url = Navigation.API_INTEGRATION_USERS + "/change-password";

        ChangePasswordCmd params = new ChangePasswordCmd();
        params.setOldPassword(TEST_USER_PASSWORD);
        params.setNewPassword(newPassword);
        params.setNewPasswordConfirm(newPassword);

        String encodedOldPassword = passwordEncoder.encode(TEST_USER_PASSWORD);
        when(userService.retrievePasswordHash(Long.parseLong(TEST_USER_NIN))).thenReturn(encodedOldPassword);

        String code = userService.retrievePasswordHash(Long.parseLong(TEST_USER_NIN));

        assertEquals(code, encodedOldPassword);

        mockMvc.perform(post(url).cookie(tokenCookie).content(objectToJson(params)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.WEB_SERVICE_CALLER_TYPE).with(csrf())).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.status", is(WsResponse.EWsResponseStatus.SUCCESS.getCode())));

        verify(userService, times(1)).updateUserPassword(eq(Long.parseLong(TEST_USER_NIN)), anyString());
    }

}
