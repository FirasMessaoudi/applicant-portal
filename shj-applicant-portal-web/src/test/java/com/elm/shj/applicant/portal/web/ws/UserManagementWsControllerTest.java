package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.admin.ResetPasswordCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserManagementWsControllerTest extends AbstractControllerTestSuite {

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
}
