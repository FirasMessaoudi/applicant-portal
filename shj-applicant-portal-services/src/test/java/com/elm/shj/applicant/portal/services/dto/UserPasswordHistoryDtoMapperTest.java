/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.orm.entity.JpaUserPasswordHistory;
import com.elm.shj.applicant.portal.services.test.MapperTest;

/**
 * Testing class for {@link UserPasswordHistoryDtoMapper}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class UserPasswordHistoryDtoMapperTest extends MapperTest<UserPasswordHistoryDto, JpaUserPasswordHistory, UserPasswordHistoryDtoMapper> {

    @Override
    protected Class<UserPasswordHistoryDtoMapper> getMapperClass() {
        return UserPasswordHistoryDtoMapper.class;
    }

    @Override
    protected UserPasswordHistoryDto getDtoInstance() {
        return new UserPasswordHistoryDto();
    }

    @Override
    protected JpaUserPasswordHistory getEntityInstance() {
        return new JpaUserPasswordHistory();
    }
}
