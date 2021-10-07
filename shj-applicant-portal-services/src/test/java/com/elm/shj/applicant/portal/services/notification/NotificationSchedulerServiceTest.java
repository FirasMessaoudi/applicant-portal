package com.elm.shj.applicant.portal.services.notification;

import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.dcc.foundation.commons.validation.UniqueValidator;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.RoleRepository;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.UserDtoMapper;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing class for service {@link UserService}
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class NotificationSchedulerServiceTest {
    @InjectMocks
    private NotificationSchedulerService serviceToTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IntegrationService integrationService;
    @Mock
    private PasswordHistoryService passwordHistoryService;
    @Mock
    private MapperRegistry mapperRegistry;
    @Mock
    WsResponse<ApplicantLiteDto> wsResponse;
    @Mock
    private UserDtoMapper userDtoMapper;


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() throws IllegalAccessException {
//        ReflectionTestUtils.setField(serviceToTest, "passwordAgeInMonths", 3);
        Field passwordAgeInMonthsField = ReflectionUtils.findField(serviceToTest.getClass(), "passwordAgeInMonths");
        ReflectionUtils.makeAccessible(passwordAgeInMonthsField);
        passwordAgeInMonthsField.set(serviceToTest, 3);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void test_notify_password_expired_users() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user = new JpaUser();
        user.setId(1);
        users.add(user);
        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory("2021-07-10", user.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        Assertions.assertEquals("notifying user with id :1", outContent.toString());

    }


    private UserPasswordHistoryDto buildUserPasswordsHistory(String creationDate, long userId) throws ParseException {
        UserPasswordHistoryDto userPasswordHistory = new UserPasswordHistoryDto();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(creationDate);
        userPasswordHistory.setCreationDate(date);
        userPasswordHistory.setUserId(userId);
        return userPasswordHistory;
    }
}
