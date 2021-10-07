package com.elm.shj.applicant.portal.services.notification;

import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
import com.elm.shj.applicant.portal.services.dto.UserDtoMapper;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing class for service {@link UserService}
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@ExtendWith(MockitoExtension.class)
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
    //these  dates are considered valid and not valid based on today date and user password Age In Months  and  password Expiry Notification Period
    private static final String TEST_VALID_NOTIFICATION_5DAYS_BEFORE = "2021-07-11";
    private static final String TEST_VALID_NOTIFICATION_3DAYS_BEFORE = "2021-07-10";
    private static final String TEST_VALID_NOTIFICATION_DAY_BEFORE = "2021-07-08";
    private static final String TEST_NOT_VALID_NOTIFICATION_SAME_DAY = "2021-07-07";
    private static final String TEST_NOT_VALID_NOTIFICATION_DAY_AFTER = "2021-07-13";

    @BeforeEach
    public void setUpStreams() throws IllegalAccessException {
        ReflectionTestUtils.setField(serviceToTest, "passwordAgeInMonths", 3);
        ReflectionTestUtils.setField(serviceToTest, "passwordExpiryNotificationPeriod", 5);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void test_notify_password_expired_users_success() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user = new JpaUser();
        JpaUser user2 = new JpaUser();
        user.setId(1);
        users.add(user);
        user2.setId(2);
        users.add(user2);
        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_VALID_NOTIFICATION_5DAYS_BEFORE, user.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        UserPasswordHistoryDto userPasswordHistory2 = buildUserPasswordsHistory(TEST_VALID_NOTIFICATION_3DAYS_BEFORE, user2.getId());
        when(passwordHistoryService.findLastByUserId(users.get(1).getId())).thenReturn(Optional.of(userPasswordHistory2));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        Assertions.assertEquals("notifying user with id :2notifying user with id :1", outContent.toString());


    }

    @Test
    public void test_notify_password_expired_users_success_day_before_expiration() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user3 = new JpaUser();
        user3.setId(3);
        users.add(user3);
        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_VALID_NOTIFICATION_DAY_BEFORE, user3.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        Assertions.assertEquals("notifying user with id :3", outContent.toString());


    }


    @Test
    public void test_notify_password_expired_users_failed_same_day() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user3 = new JpaUser();
        user3.setId(3);
        users.add(user3);

        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_NOT_VALID_NOTIFICATION_SAME_DAY, user3.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        Assertions.assertEquals("", outContent.toString());


    }

    @Test
    public void test_notify_password_expired_users_failed_day_after() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user3 = new JpaUser();
        user3.setId(3);
        users.add(user3);

        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_NOT_VALID_NOTIFICATION_DAY_AFTER, user3.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        Assertions.assertEquals("", outContent.toString());


    }

    private UserPasswordHistoryDto buildUserPasswordsHistory(String creationDate, long userId) throws ParseException {
        UserPasswordHistoryDto userPasswordHistory = new UserPasswordHistoryDto();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(creationDate);
        userPasswordHistory.setCreationDate(date);
        userPasswordHistory.setUserId(userId);
        return userPasswordHistory;
    }
}
