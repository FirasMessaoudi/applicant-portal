/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.notification;

import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testing class for service {@link PasswordExpiryNotificationScheduler}
 *
 * @author Ahmed Ali
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class PasswordExpiryNotificationSchedulerTest {
    @InjectMocks
    private PasswordExpiryNotificationScheduler serviceToTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IntegrationService integrationService;
    @Mock
    private PasswordHistoryService passwordHistoryService;


    //these  dates are considered valid and not valid based on today date and user password Age In Months  and  password Expiry Notification Period
    private static final String TEST_VALID_NOTIFICATION_5DAYS_BEFORE = "2021-07-19";
    private static final String TEST_VALID_NOTIFICATION_3DAYS_BEFORE = "2021-07-17";
    private static final String TEST_VALID_NOTIFICATION_DAY_BEFORE = "2021-07-15";
    private static final String TEST_NOT_VALID_NOTIFICATION_SAME_DAY = "2021-07-14";
    private static final String TEST_NOT_VALID_NOTIFICATION_DAY_AFTER = "2021-07-13";
    private static final int TEST_PASSWORD_AGE_IN_MONTHS = 3;
    private static final int TEST_PASSWORD_EXPIRY_NOTIFICATION_PERIOD = 5;
    private static final long TEST_USER_ID_1 = 1;
    private static final long TEST_USER_ID_2 = 2;


    @BeforeEach
    public void setUpStreams() throws IllegalAccessException {
        ReflectionTestUtils.setField(serviceToTest, "passwordAgeInMonths", TEST_PASSWORD_AGE_IN_MONTHS);
        ReflectionTestUtils.setField(serviceToTest, "passwordExpiryNotificationPeriod", TEST_PASSWORD_EXPIRY_NOTIFICATION_PERIOD);

    }


    @Test
    public void test_notify_password_expired_users_success_5_or_3_days_before_expiration() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user = new JpaUser();
        JpaUser user2 = new JpaUser();
        user.setId(TEST_USER_ID_1);
        users.add(user);
        user2.setId(TEST_USER_ID_2);
        users.add(user2);
        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_VALID_NOTIFICATION_5DAYS_BEFORE, user.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        UserPasswordHistoryDto userPasswordHistory2 = buildUserPasswordsHistory(TEST_VALID_NOTIFICATION_3DAYS_BEFORE, user2.getId());
        when(passwordHistoryService.findLastByUserId(users.get(1).getId())).thenReturn(Optional.of(userPasswordHistory2));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        verify(integrationService).sendPasswordExpiryNotificationRequest(any());

    }

    @Test
    public void test_notify_password_expired_users_success_day_before_expiration() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user = new JpaUser();
        user.setId(TEST_USER_ID_1);
        users.add(user);
        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_VALID_NOTIFICATION_DAY_BEFORE, user.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        verify(integrationService).sendPasswordExpiryNotificationRequest(any());


    }


    @Test
    public void test_notify_password_expired_users_failed_same_day() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user = new JpaUser();
        user.setId(TEST_USER_ID_1);
        users.add(user);

        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_NOT_VALID_NOTIFICATION_SAME_DAY, user.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        verify(integrationService, never()).sendPasswordExpiryNotificationRequest(any());

    }

    @Test
    public void test_notify_password_expired_users_failed_day_after() throws ParseException {
        List<JpaUser> users = new ArrayList<>();
        JpaUser user = new JpaUser();
        user.setId(TEST_USER_ID_1);
        users.add(user);

        UserPasswordHistoryDto userPasswordHistory = buildUserPasswordsHistory(TEST_NOT_VALID_NOTIFICATION_DAY_AFTER, user.getId());
        when(passwordHistoryService.findLastByUserId(users.get(0).getId())).thenReturn(Optional.of(userPasswordHistory));
        when(userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse()).thenReturn(users);
        serviceToTest.notifyPasswordExpiredUsers();
        verify(userRepository).findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        verify(integrationService, never()).sendPasswordExpiryNotificationRequest(any());
    }

    private UserPasswordHistoryDto buildUserPasswordsHistory(String creationDate, long userId) throws ParseException {
        UserPasswordHistoryDto userPasswordHistory = new UserPasswordHistoryDto();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(creationDate);
        userPasswordHistory.setCreationDate(date);
        userPasswordHistory.setUserId(userId);
        return userPasswordHistory;
    }
}
