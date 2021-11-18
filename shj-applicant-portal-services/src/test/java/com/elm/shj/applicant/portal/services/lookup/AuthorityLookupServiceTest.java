/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.lookup;

import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.shj.applicant.portal.orm.entity.JpaAuthorityLookup;
import com.elm.shj.applicant.portal.orm.repository.AuthorityLookupRepository;
import com.elm.shj.applicant.portal.services.dto.AuthorityLookupDto;
import com.elm.shj.applicant.portal.services.dto.AuthorityLookupDtoMapper;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

/**
 * Testing class for service {@link AuthorityLookupService}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
@ExtendWith(MockitoExtension.class)
class AuthorityLookupServiceTest {

    @InjectMocks
    private AuthorityLookupService serviceToTest;

    @Mock
    private AuthorityLookupRepository authorityLookupRepository;

    @Mock
    private AuthorityLookupDtoMapper authorityLookupDtoMapper;

    @Mock
    private MapperRegistry mapperRegistry;

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(mapperRegistry.mapperOf(AuthorityLookupDto.class, JpaAuthorityLookup.class)).thenReturn(authorityLookupDtoMapper);
    }

    @Test
    void test_find_all_success() {
        serviceToTest.findAll();
        verify(authorityLookupRepository).findAll();
    }

    @Test
    void test_find_all_parent_authorities_success() {
        serviceToTest.findAllParentAuthorities();
        verify(authorityLookupRepository).findAllByParentIsNull();
    }
}
