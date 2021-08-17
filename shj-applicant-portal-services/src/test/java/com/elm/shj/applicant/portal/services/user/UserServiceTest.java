/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.user;

import com.elm.dcc.foundation.commons.core.mapper.CycleAvoidingMappingContext;
import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.dcc.foundation.providers.email.service.EmailService;
import com.elm.dcc.foundation.providers.sms.service.SmsGatewayService;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.RoleRepository;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
import com.elm.shj.applicant.portal.services.dto.UpdateApplicantCmd;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsAuthenticationException;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testing class for service {@link UserService}
 *
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final long TEST_USER_ID = 5;
    private static final long TEST_ROLE_ID = 5;
    private static final Long TEST_NIN = 1234567897L;
    private static final Long TEST_UIN = 1234567899L;
    private static final String TEST_MOBILE = "555359285";
    private static final String TEST_DATE_OG_BIRTH_GREGORIAN = "1981-11-05";
    private static final PageRequest TEST_PAGE = PageRequest.of(0, 10);
    private static final String TEST_EMAIL = "app@elm.sa";
    private static final String TEST_COUNTRY_CODE = "SA";
    private static final int TEST_DATE_OG_BIRTH_HIGRI = 14051016;
    @InjectMocks
    private UserService serviceToTest;

    @Mock
    private UserRepository userRepository;
    @Mock
    private IntegrationService integrationService;
    @Mock
    private MapperRegistry mapperRegistry;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    protected MessageSource messageSource;

    @Mock
    private SmsGatewayService smsGatewayService;

    @Mock
    private RoleService roleService;

    @Mock
    private EmailService emailService;

    @Mock
    private CycleAvoidingMappingContext mappingContext;
    @Mock
    RestTemplate restTemplate;

    private final MockMultipartFile mockAvatar = new MockMultipartFile("fileData", "mock avatar", "text/plain", "mock content".getBytes());

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(mapperRegistry.mapperOf(UserDto.class, JpaUser.class)).thenReturn(userDtoMapper);
    }

    @Test
    public void test_find_all_non_deleted() {
        serviceToTest.findAllNotDeleted(any(), anyLong(), Collections.singleton(RoleRepository.SYSTEM_ADMIN_USER_ROLE_ID));
        verify(userRepository).findDistinctByDeletedFalseAndIdNot(any(), anyLong());
        serviceToTest.findAllNotDeleted(any(), anyLong(), Collections.singleton(5L));
        verify(userRepository).findDistinctByDeletedFalseAndIdNotAndUserRolesRoleIdNot(any(), anyLong(), eq(RoleRepository.SYSTEM_ADMIN_ROLE_ID));
    }

    @Test
    public void test_find_by_nin() {
        serviceToTest.findByNin(anyInt());
        verify(userRepository).findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(anyLong());
    }

    @Test
    public void test_find_by_uin() {
        serviceToTest.findByUin(anyInt());
        verify(userRepository).findByUinAndDeletedFalseAndActivatedTrue(anyLong());
    }

    @Test
    public void test_find_by_nin_null() {
        Mockito.when(userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(anyLong())).thenReturn(null);
        Optional<UserDto> user = serviceToTest.findByNin(anyLong());
        assertEquals(Optional.empty(), user);
    }

    @Test
    public void test_find_by_uin_null() {
        Mockito.when(userRepository.findByUinAndDeletedFalseAndActivatedTrue(anyLong())).thenReturn(null);
        Optional<UserDto> user = serviceToTest.findByUin(anyLong());
        assertEquals(Optional.empty(), user);
    }

    @Test
    public void test_find_by_nin_not_null() {
        JpaUser foundUser = new JpaUser();
        UserDto foundDto = new UserDto();
        Mockito.when(userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(anyLong())).thenReturn(foundUser);
        Mockito.when(userDtoMapper.fromEntity(any(), eq(mappingContext))).thenReturn(foundDto);
        Optional<UserDto> user = serviceToTest.findByNin(anyLong());
        assertEquals(foundDto, user.get());
    }

    @Test
    public void test_find_by_uin_not_null() {
        JpaUser foundUser = new JpaUser();
        UserDto foundDto = new UserDto();
        Mockito.when(userRepository.findByUinAndDeletedFalseAndActivatedTrue(anyLong())).thenReturn(foundUser);
        Mockito.when(userDtoMapper.fromEntity(any(), eq(mappingContext))).thenReturn(foundDto);
        Optional<UserDto> user = serviceToTest.findByUin(anyLong());
        assertEquals(foundDto, user.get());
    }

    @Test
    public void test_update_last_login_date() {
        long userId = 11;
        Date tokenExpiryDate = new Date();
        serviceToTest.updateUserLoginInfo(userId, tokenExpiryDate);
        verify(userRepository, times(1)).updateLoginInfo(eq(0), any(), any(), eq(tokenExpiryDate), eq(userId));
    }

    @Test
    public void test_retrieve_password_hash() {
        serviceToTest.retrievePasswordHash(anyLong());
        verify(userRepository, times(1)).retrievePasswordHash(anyLong());
    }

    @Test
    public void test_update_login_tries() {
        UserDto user = new UserDto();
        long userId = 1;
        int numberOfTries = 1;
        user.setId(userId);
        user.setNumberOfTries(numberOfTries);
        serviceToTest.updateLoginTries(user);
        verify(userRepository, times(1)).updateLoginTries(eq(userId), eq(numberOfTries + 1), any());
    }

    @Test
    public void test_delete_user() {
        serviceToTest.deleteUser(TEST_USER_ID);
        verify(userRepository).markDeleted(TEST_USER_ID);
    }

    @Test
    void test_activate_user() {
        serviceToTest.activateUser(TEST_USER_ID);
        verify(userRepository).activate(eq(TEST_USER_ID));
    }

    @Test
    void test_deactivate_user() {
        serviceToTest.deactivateUser(TEST_USER_ID);
        verify(userRepository).deactivate(eq(TEST_USER_ID));
    }

    @Test
    public void test_update_user_password() {
        String newPassHash = "anything";
        long userNIN = 1234567897;
        serviceToTest.updateUserPassword(userNIN, newPassHash);
        verify(userRepository).updatePassword(userNIN, newPassHash);
    }

    @Test
    public void test_save_user() {
        UserDto userToSave = new UserDto();
        userToSave.setPassword("any pass");
        userToSave.setPasswordHash("any hash");
        serviceToTest.save(userToSave);
        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void test_save_user_no_pass() {
        UserDto userToSave = new UserDto();
        serviceToTest.save(userToSave);
        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void test_save_user_with_avatar_throw_exception() {
        UserDto userToSave = Mockito.mock(UserDto.class);
        doCallRealMethod().when(userToSave).setAvatarFile(any());
        doCallRealMethod().when(userToSave).getAvatarFile();
        doAnswer((Answer<Void>) invocation -> {
            throw new IOException("This was expected in the test");
        }).when(userToSave).setAvatar(anyString());
        assertNull(userToSave.getAvatar());
        userToSave.setAvatarFile(mockAvatar);
        serviceToTest.save(userToSave);
        assertNull(userToSave.getAvatar());
        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void test_save_user_with_avatar() {
        UserDto userToSave = new UserDto();
        assertNull(userToSave.getAvatar());
        userToSave.setAvatarFile(mockAvatar);
        serviceToTest.save(userToSave);
        assertNotNull(userToSave.getAvatar());
        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void test_searchByRoleStatusOrNin_not_admin() {
        serviceToTest.searchByRoleStatusOrNin(TEST_PAGE, TEST_ROLE_ID, TEST_NIN.toString(), true, TEST_USER_ID, Collections.singleton(TEST_ROLE_ID));
        verify(userRepository, times(1)).findByRoleOrNinOrStatus(TEST_PAGE, TEST_ROLE_ID, "%" + TEST_NIN.toString() + "%", true, TEST_USER_ID, RoleRepository.SYSTEM_ADMIN_ROLE_ID);

    }

    @Test
    public void test_searchByRoleStatusOrNin_admin() {
        serviceToTest.searchByRoleStatusOrNin(TEST_PAGE, TEST_ROLE_ID, TEST_NIN.toString(), true, TEST_USER_ID, Collections.singleton(RoleRepository.SYSTEM_ADMIN_USER_ROLE_ID));
        verify(userRepository, times(1)).findByRoleOrNinOrStatus(TEST_PAGE, TEST_ROLE_ID, "%" + TEST_NIN.toString() + "%", true, TEST_USER_ID, null);
    }

    @Test
    public void test_searchByRoleStatusOrNin_nin_null() {
        serviceToTest.searchByRoleStatusOrNin(TEST_PAGE, TEST_ROLE_ID, null, true, TEST_USER_ID, Collections.singleton(RoleRepository.SYSTEM_ADMIN_USER_ROLE_ID));
        verify(userRepository, times(1)).findByRoleOrNinOrStatus(TEST_PAGE, TEST_ROLE_ID, null, true, TEST_USER_ID, null);
    }

    @Test
    public void test_clearToken() {
        serviceToTest.clearToken(TEST_NIN);
        verify(userRepository, times(1)).clearToken(TEST_NIN);
    }

    @Test
    public void test_updateUserAvatar() throws IOException {
        String encodedAvatarStr = serviceToTest.updateUserAvatar(TEST_NIN, mockAvatar.getBytes());
        assertEquals(encodedAvatarStr, Base64.getEncoder().encodeToString(mockAvatar.getBytes()));
        verify(userRepository, times(1)).updateAvatar(TEST_NIN, encodedAvatarStr);
    }

    @Test
    public void test_createUser() {
        UserDto user = new UserDto();
        user.setUin(TEST_UIN);
        user.setMobileNumber(TEST_MOBILE);
        String passwordMock = "DUMMY_PASS";
        user.setPassword(passwordMock);
        when(mapperRegistry.mapperOf(UserDto.class, JpaUser.class)).thenReturn(userDtoMapper);
        when(passwordEncoder.encode(anyString())).thenReturn(passwordMock);
        when(serviceToTest.save(user)).thenReturn(user);
        when(serviceToTest.notifyRegisteredUser(user)).thenReturn(true);
        serviceToTest.createUser(user);

        verify(emailService, times(1)).sendMailFromTemplate(any(), any(), any(), any(), any());
    }

    @Test
    public void test_verify_applicant_uin_notFound() {
        ValidateApplicantCmd command = new ValidateApplicantCmd();
        command.setUin(String.valueOf(TEST_UIN));
        command.setDateOfBirthGregorian(TEST_DATE_OG_BIRTH_GREGORIAN);
        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(null);
        assertNull(serviceToTest.verify(command));
    }


    @Test
    public void test_verify_uin_applicant_uin_exist() {
        ValidateApplicantCmd command = new ValidateApplicantCmd();
        ApplicantLiteDto applicantLiteDto = new ApplicantLiteDto();
        applicantLiteDto.setMobileNumber(String.valueOf(TEST_MOBILE));
        command.setUin(String.valueOf(TEST_UIN));
        command.setDateOfBirthGregorian(TEST_DATE_OG_BIRTH_GREGORIAN);
        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(applicantLiteDto);
        assertEquals(applicantLiteDto, serviceToTest.verify(command));

    }

    @Test
    public void test_update_applicant_by_uin() throws WsAuthenticationException {
        UpdateApplicantCmd applicant = new UpdateApplicantCmd(String.valueOf(TEST_UIN), TEST_EMAIL, TEST_MOBILE, TEST_COUNTRY_CODE, TEST_DATE_OG_BIRTH_HIGRI);
        ApplicantLiteDto applicantLiteDto = new ApplicantLiteDto();
        applicantLiteDto.setMobileNumber(TEST_MOBILE);
        applicantLiteDto.setEmail(TEST_EMAIL);
        when(integrationService.callIntegrationWs(anyString(), any(), any(), any())).thenReturn(new WsResponse<ApplicantLiteDto>());
//         assertEquals(mapper.convertValue(wsResponse.getBody(),ApplicantLiteDto.class), serviceToTest.updateUserInAdminPortal(applicant ));

    }

    @Test
    public void test_resetPassword_notify_ok() {
        UserDto user = new UserDto();
        user.setUin(TEST_UIN);
        user.setNin(TEST_NIN);
        user.setMobileNumber(TEST_MOBILE);
        when(serviceToTest.notifyUserOnPasswordReset(user)).thenReturn(true);
        String updatedPasswordMock = "DUMMY_PASS";
        when(passwordEncoder.encode(anyString())).thenReturn(updatedPasswordMock);
        user.setId(TEST_USER_ID);
        serviceToTest.resetPassword(user);
        verify(userRepository, times(1)).resetPwd(TEST_USER_ID, updatedPasswordMock, true);
    }

    @Test
    public void test_resetPassword_notify_ko() {
        UserDto user = new UserDto();
        user.setUin(TEST_UIN);
        user.setNin(TEST_NIN);
        user.setMobileNumber(TEST_MOBILE);
        when(serviceToTest.notifyUserOnPasswordReset(user)).thenReturn(false);
        serviceToTest.resetPassword(user);
        verify(userRepository, times(0)).resetPwd(anyLong(), anyString(), anyBoolean());
    }

    @Test
    public void test_notifyUserOnPasswordReset() {
        when(smsGatewayService.sendMessage(any(), any())).thenReturn(true);
        when(emailService.sendMailFromTemplate(any(), any(), any(), any(), any())).thenReturn(true);
        UserDto user = new UserDto();
        user.setUin(TEST_UIN);
        user.setNin(TEST_NIN);
        user.setMobileNumber(TEST_MOBILE);

        boolean res = serviceToTest.notifyUserOnPasswordReset(user);
        assertTrue(res);
    }

    @Test
    public void test_hasToken() {
        serviceToTest.hasToken(TEST_NIN);
        verify(userRepository, times(1)).retrieveTokenExpiryDate(TEST_NIN);
    }

    @Test
    public void test_find_user_main_data_by_uin_notFound() {
        ResponseEntity responseEntity = new ResponseEntity<ApplicantMainDataDto>((ApplicantMainDataDto) null, HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>>any(), Matchers.<Class<ApplicantMainDataDto>>any())).thenReturn(responseEntity);
        Optional<ApplicantMainDataDto> applicantMainDataDto = serviceToTest.findUserMainDataByUin(TEST_UIN.toString(), 2);
        assertFalse(applicantMainDataDto.isPresent());
    }

    @Test
    public void test_find_user_main_data_by_uin_found() {
        ApplicantMainDataDto dto = new ApplicantMainDataDto();
        ResponseEntity responseEntity = new ResponseEntity<ApplicantMainDataDto>(dto, HttpStatus.OK);
        when(restTemplate.exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>>any(), Matchers.<Class<ApplicantMainDataDto>>any())).thenReturn(responseEntity);
        Optional<ApplicantMainDataDto> applicantMainDataDto = serviceToTest.findUserMainDataByUin(TEST_UIN.toString(), 2);
        assertTrue(applicantMainDataDto.isPresent());
    }

    @Test
    public void test_find_applicant_ritual_seasons_notFound() {
        ResponseEntity responseEntity = new ResponseEntity<List<Integer>>((List<Integer>) null, HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>>any(), Matchers.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        List<Integer> seasonList = serviceToTest.findApplicantRitualSeasons(TEST_UIN.toString());
        assertNotNull(seasonList);
        assertEquals(0, seasonList.size());
    }

    @Test
    public void test_find_applicant_ritual_seasons_found() {
        ResponseEntity responseEntity = new ResponseEntity<List<Integer>>(Arrays.asList(1442), HttpStatus.OK);
        when(restTemplate.exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>>any(), Matchers.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        List<Integer> seasonList = serviceToTest.findApplicantRitualSeasons(TEST_UIN.toString());
        assertNotNull(seasonList);
        assertEquals(1, seasonList.size());
        assertEquals(1442, seasonList.get(0));
    }

    @Test
    public void test_find_applicant_ritual_by_uin_and_seasons_found() {

        ResponseEntity responseEntity = new ResponseEntity<List<ApplicantRitualLiteDto>>(Arrays.asList(new ApplicantRitualLiteDto()), HttpStatus.OK);

        when(restTemplate.exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>>any(), Matchers.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        List<ApplicantRitualLiteDto> applicantRitualLiteDtos = serviceToTest.findApplicantRitualByUinAndSeasons(TEST_UIN.toString(),1442);
        assertNotNull(applicantRitualLiteDtos);
        assertEquals(1, applicantRitualLiteDtos.size());

    }


    @Test
    public void test_find_applicant_ritual_by_uin_and_seasons_notFound() {

        ResponseEntity responseEntity = new ResponseEntity<List<ApplicantRitualLiteDto>>((List<ApplicantRitualLiteDto>) null,  HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>>any(), Matchers.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        List<ApplicantRitualLiteDto> applicantRitualLiteDtos = serviceToTest.findApplicantRitualByUinAndSeasons(TEST_UIN.toString(),1442);
        assertNotNull(applicantRitualLiteDtos);
        assertEquals(0, applicantRitualLiteDtos.size());

    }


}
