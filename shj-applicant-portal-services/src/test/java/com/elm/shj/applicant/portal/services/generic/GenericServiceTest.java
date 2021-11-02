/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.generic;

import com.elm.dcc.foundation.commons.core.mapper.CycleAvoidingMappingContext;
import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.UserDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

/**
 * Testing class for generic service {@link GenericService}
 *
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class GenericServiceTest {

    @InjectMocks
    private DefaultTestService serviceToTest = new DefaultTestService();

    @Mock
    private UserRepository repository;

    @Mock
    private MapperRegistry mapperRegistry;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private CycleAvoidingMappingContext mappingContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(serviceToTest);
        Mockito.lenient().when(mapperRegistry.mapperOf(UserDto.class, JpaUser.class)).thenReturn(userDtoMapper);
    }

    public void test_find_one() {
        UserDto userNotFound = serviceToTest.findOne(anyLong());
        Assert.isNull(userNotFound, "There should be no user found");
        verify(repository).findById(anyLong());
    }

    @Test
    public void test_find_all() {
        serviceToTest.findAll();
        verify(repository).findAll();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void test_save_all() {
        serviceToTest.saveAll(Collections.singletonList(new UserDto()));
        ArgumentCaptor<Iterable> captor = ArgumentCaptor.forClass(Iterable.class);
        verify(repository).saveAll(captor.capture());
    }

    private class DefaultTestService extends GenericService<JpaUser, UserDto, Long> {

    }

}
