/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.user;

import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.shj.applicant.portal.orm.entity.JpaUserPasswordHistory;
import com.elm.shj.applicant.portal.orm.repository.PasswordHistoryRepository;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing class for service {@link PasswordHistoryService}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
@ExtendWith(MockitoExtension.class)
class PasswordHistoryServiceTest {

    private static final long TEST_USER_ID = 5;
    private static final int TEST_PASSWORD_HISTORY_THRESHOLD = 5;

    @InjectMocks
    private PasswordHistoryService serviceToTest;

    @Mock
    private PasswordHistoryRepository passwordHistoryRepository;

    @Mock
    private MapperRegistry mapperRegistry;

    @Mock
    private UserPasswordHistoryDtoMapper passwordHistoryDtoMapper;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(serviceToTest, "passwordHistoryThreshold", TEST_PASSWORD_HISTORY_THRESHOLD);
        Mockito.lenient().when(mapperRegistry.mapperOf(UserPasswordHistoryDto.class, JpaUserPasswordHistory.class)).thenReturn(passwordHistoryDtoMapper);
    }

    @Test
    void test_find_by_user_id() {
        serviceToTest.findByUserId(TEST_USER_ID);
        verify(passwordHistoryRepository).findByUserIdOrderByCreationDateDesc(TEST_USER_ID, PageRequest.of(0, TEST_PASSWORD_HISTORY_THRESHOLD));
    }

    @Test
    void test_find_last_by_user_id() {
        serviceToTest.findLastByUserId(TEST_USER_ID);
        verify(passwordHistoryRepository).findFirst1ByUserIdOrderByCreationDateDesc(TEST_USER_ID);
    }

    @Test
    void test_find_last_by_user_id_null() {
        when(passwordHistoryRepository.findFirst1ByUserIdOrderByCreationDateDesc(eq(TEST_USER_ID))).thenReturn(null);
        Optional<UserPasswordHistoryDto> result = serviceToTest.findLastByUserId(TEST_USER_ID);
        verify(passwordHistoryRepository).findFirst1ByUserIdOrderByCreationDateDesc(TEST_USER_ID);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void test_add_user_password_history() {
        serviceToTest.addUserPasswordHistory(TEST_USER_ID, "DUMMY");
        verify(passwordHistoryRepository).saveAndFlush(any());
    }
}
