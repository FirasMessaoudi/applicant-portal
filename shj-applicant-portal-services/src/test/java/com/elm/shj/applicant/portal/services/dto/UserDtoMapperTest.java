/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.services.test.MapperTest;

/**
 * Testing class for {@link UserDtoMapper}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class UserDtoMapperTest extends MapperTest<UserDto, JpaUser, UserDtoMapper> {

    @Override
    protected Class<UserDtoMapper> getMapperClass() {
        return UserDtoMapper.class;
    }

    @Override
    protected UserDto getDtoInstance() {
        return new UserDto();
    }

    @Override
    protected JpaUser getEntityInstance() {
        return new JpaUser();
    }
}

