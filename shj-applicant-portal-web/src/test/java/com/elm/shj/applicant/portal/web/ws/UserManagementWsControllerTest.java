package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.admin.ChangePasswordCmd;
import com.elm.shj.applicant.portal.web.admin.ResetPasswordCmd;
import com.elm.shj.applicant.portal.web.admin.UserLocationsCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.elm.shj.applicant.portal.web.ws.WsError.EWsError.NOT_FOUND_IN_ADMIN;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserManagementWsControllerTest extends AbstractControllerTestSuite {

    private UserDto user;
    private static final String TEST_UIN = "502087000000";
    private static final Long TEST_WRONG_UIN = 1234567899L;
    private static final String TEST_EMAIL = "app@elm.sa";
    private static final String TEST_COUNTRY_CODE = "SA";
    private static final String TEST_COUNTRY_PHONE_PREFIX = "+966";
    private static final String TEST_MOBILE = "555359285";

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

    @Test
    public void test_find_applicant_main_data_by_uin_and_ritualId_success() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/main-data/" + TEST_UIN;
        ApplicantMainDataDto applicantMainDataDto = new ApplicantMainDataDto();
        applicantMainDataDto.setUin(TEST_UIN);
        when(userService.findUserMainDataByUin(any(String.class), any(Long.class))).thenReturn(Optional.of(applicantMainDataDto));
        mockMvc.perform(get(url)
                .cookie(tokenCookie).with(csrf())
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.uin").value((applicantMainDataDto.getUin())));
        verify(userService, times(1)).findUserMainDataByUin(any(String.class), any(Long.class));

    }

    @Test
    public void test_find_applicant_main_data_by_uin_and_ritualId_fail() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/main-data/" + TEST_WRONG_UIN;
        mockMvc.perform(get(url)
                .cookie(tokenCookie).with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.body.uin").doesNotExist());
        verify(userService, times(1)).findUserMainDataByUin(any(String.class), any(Long.class));

    }

    @Test
    public void test_update_user_contacts_success() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/contacts";
        UpdateContactsCmd userContacts = new UpdateContactsCmd();
        UserDto user = new UserDto();
        userContacts.setCountryCode(TEST_COUNTRY_CODE);
        userContacts.setEmail(TEST_EMAIL);
        userContacts.setMobileNumber(TEST_MOBILE);
        userContacts.setCountryPhonePrefix(TEST_COUNTRY_PHONE_PREFIX);
        when(userService.findByUin(anyLong())).thenReturn(Optional.of(user));
        when(userService.updateUserInAdminPortal(any())).thenReturn(new ApplicantLiteDto());
        when(userService.save(any())).thenReturn(user);

        mockMvc.perform(put(url).cookie(tokenCookie).contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userContacts)).with(csrf()))
                .andDo(print()).andExpect(status().isOk());

        verify(otpService, times(2)).validateOtp(anyString(), anyString());
        verify(userService, times(1)).updateUserInAdminPortal(any());
        verify(userService, times(1)).save(any());

    }

    @Test
    public void test_update_user_contacts_fail_update_admin() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/contacts";
        UpdateContactsCmd userContacts = new UpdateContactsCmd();
        UserDto user = new UserDto();

        userContacts.setCountryCode(TEST_COUNTRY_CODE);
        userContacts.setEmail(TEST_EMAIL);
        userContacts.setMobileNumber(TEST_MOBILE);
        userContacts.setCountryPhonePrefix(TEST_COUNTRY_PHONE_PREFIX);
        when(userService.findByUin(anyLong())).thenReturn(Optional.of(user));
        when(userService.updateUserInAdminPortal(any())).thenReturn(null);
        when(userService.save(any())).thenReturn(user);

        mockMvc.perform(put(url).cookie(tokenCookie).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(userContacts)).with(csrf()))
                .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.body.error")
                .value(NOT_FOUND_IN_ADMIN.getCode()));
    }

    @Test
    public void test_find_applicant_ritual_seasons_by_uin_success() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/ritual-seasons";

        List<Integer> seasons = Arrays.asList(1442);
        when(userService.findApplicantRitualSeasons(any())).thenReturn(seasons);

        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.length()", is(seasons.size())))
                .andExpect(jsonPath("$.body[0]", is(1442)));


        verify(userService, times(1)).findApplicantRitualSeasons(any());

    }

    @Test
    public void test_find_applicant_ritual_seasons_by_uin_empty() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/ritual-seasons";

        when(userService.findApplicantRitualSeasons(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.length()", is(0)));


        verify(userService, times(1)).findApplicantRitualSeasons(any());

    }

    @Test
    public void test_find_applicant_ritual_by_uin_and_season_success() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/ritual-lite/1442";

        ApplicantRitualLiteDto applicantRitualLiteDto = new ApplicantRitualLiteDto();
        List<ApplicantRitualLiteDto> applicantRitualLiteDtos = Arrays.asList(applicantRitualLiteDto);
        when(userService.findApplicantRitualByUinAndSeasons(any(String.class), any(Integer.class))).thenReturn(applicantRitualLiteDtos);

        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.length()", is(applicantRitualLiteDtos.size())))
                .andExpect(jsonPath("$.body[0].hijriSeason", is(applicantRitualLiteDto.getHijriSeason())));


        verify(userService, times(1)).findApplicantRitualByUinAndSeasons(any(String.class), any(Integer.class));

    }

    @Test
    public void test_find_applicant_ritual_by_uin_and_season_empty() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/ritual-lite/1442";

        when(userService.findApplicantRitualByUinAndSeasons(any(String.class), any(Integer.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.length()", is(0)));

        verify(userService, times(1)).findApplicantRitualByUinAndSeasons(any(String.class), any(Integer.class));

    }


    @Test
    public void test_find_applicant_ritual_latest_by_uin_success() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/ritual-lite/latest";

        ApplicantRitualLiteDto applicantRitualLiteDto = new ApplicantRitualLiteDto();
        when(userService.findApplicantRitualLatestByUin(any(String.class))).thenReturn(applicantRitualLiteDto);

        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.hijriSeason", is(applicantRitualLiteDto.getHijriSeason())));


        verify(userService, times(1)).findApplicantRitualLatestByUin(any(String.class));

    }

    @Test
    public void test_find_applicant_ritual_latest_by_uin_not_found() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/ritual-lite/latest";

        when(userService.findApplicantRitualLatestByUin(any(String.class))).thenReturn(null);

        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body").isEmpty());


        verify(userService, times(1)).findApplicantRitualLatestByUin(any(String.class));

    }

    @Test
    public void test_store_user_locations_success() throws Exception {
        String url = Navigation.API_INTEGRATION_USERS + "/user_locations";

        UserLocationsCmd userLocationsCmd = new UserLocationsCmd();

        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.body.status", is(0)));


        verify(userLocationService, times(1)).storeUserLocation(anyList());

    }

}
