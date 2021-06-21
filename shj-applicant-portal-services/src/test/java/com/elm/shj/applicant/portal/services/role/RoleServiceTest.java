/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.role;

import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.shj.applicant.portal.orm.entity.JpaRole;
import com.elm.shj.applicant.portal.orm.repository.RoleRepository;
import com.elm.shj.applicant.portal.services.dto.RoleDto;
import com.elm.shj.applicant.portal.services.dto.RoleDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Testing class for service {@link RoleService}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    private static final long TEST_ROLE_ID = 5;
    private static final PageRequest TEST_PAGE = PageRequest.of(0, 10);
    private static final String TEST_ARABIC_NAME = "تجرية";
    private static final String TEST_ENGLISH_NAME = "Test";
    private static final long TEST_AUTHORITY_ID = 2;

    @InjectMocks
    private RoleService serviceToTest;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MapperRegistry mapperRegistry;

    @Mock
    private RoleDtoMapper roleDtoMapper;

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(mapperRegistry.mapperOf(RoleDto.class, JpaRole.class)).thenReturn(roleDtoMapper);
    }

    @Test
    public void test_find_all_activated() {
        serviceToTest.findActive(Collections.singleton(RoleRepository.SYSTEM_ADMIN_USER_ROLE_ID));
        verify(roleRepository).findByDeletedFalseAndActivated(eq(true));
        serviceToTest.findActive(Collections.singleton(TEST_ROLE_ID));
        verify(roleRepository).findByDeletedFalseAndActivatedAndIdNot(eq(true), eq(RoleRepository.SYSTEM_ADMIN_ROLE_ID));
    }

    @Test
    public void test_find_all_paginated() {
        serviceToTest.findAll(TEST_PAGE, Collections.singleton(RoleRepository.SYSTEM_ADMIN_USER_ROLE_ID));
        verify(roleRepository).findByDeletedFalse(eq(TEST_PAGE));
        serviceToTest.findAll(TEST_PAGE, Collections.singleton(TEST_ROLE_ID));
        verify(roleRepository).findDistinctByDeletedFalseAndIdNot(eq(TEST_PAGE), eq(RoleRepository.SYSTEM_ADMIN_ROLE_ID));
    }

    @Test
    void test_search_by_authority_or_name_arabic() {
        serviceToTest.searchByAuthorityOrName(TEST_PAGE, TEST_AUTHORITY_ID, TEST_ARABIC_NAME, null);
        verify(roleRepository).findByAuthorityOrName(eq(TEST_PAGE), eq(TEST_AUTHORITY_ID), eq("%" + TEST_ARABIC_NAME.toLowerCase() + "%"), eq(null));
    }

    @Test
    void test_search_by_authority_or_name_english() {
        serviceToTest.searchByAuthorityOrName(TEST_PAGE, TEST_AUTHORITY_ID, null, TEST_ENGLISH_NAME);
        verify(roleRepository).findByAuthorityOrName(eq(TEST_PAGE), eq(TEST_AUTHORITY_ID), eq(null), eq("%" + TEST_ENGLISH_NAME.toLowerCase() + "%"));
    }

    @Test
    void test_find_all() {
        serviceToTest.findAll();
        verify(roleRepository).findAll();
    }

    @Test
    void test_delete_role() {
        serviceToTest.deleteRole(TEST_ROLE_ID);
        verify(roleRepository).markDeleted(eq(TEST_ROLE_ID));
    }

    @Test
    void test_activate_role() {
        serviceToTest.activateRole(TEST_ROLE_ID);
        verify(roleRepository).activate(eq(TEST_ROLE_ID));
    }

    @Test
    void test_deactivate_role() {
        serviceToTest.deactivateRole(TEST_ROLE_ID);
        verify(roleRepository).deactivate(eq(TEST_ROLE_ID));
    }

    @Test
    void test_find_new_user_default_role() {
        serviceToTest.findNewUserDefaultRole();
        verify(roleRepository).findById(RoleRepository.SYSTEM_USER_ROLE_ID);
    }
}
