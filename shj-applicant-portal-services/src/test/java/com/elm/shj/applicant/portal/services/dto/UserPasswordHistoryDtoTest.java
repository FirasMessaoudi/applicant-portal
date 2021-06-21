/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.services.test.DtoTest;

/**
 * Testing class for {@link UserPasswordHistoryDto}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class UserPasswordHistoryDtoTest extends DtoTest<UserPasswordHistoryDto> {

    @Override
    protected UserPasswordHistoryDto getInstance() {
        return new UserPasswordHistoryDto();
    }

}
