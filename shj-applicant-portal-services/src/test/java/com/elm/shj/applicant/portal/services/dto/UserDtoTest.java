/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.services.test.DtoTest;

/**
 * Testing class for {@link UserDto}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class UserDtoTest extends DtoTest<UserDto> {

    @Override
    protected UserDto getInstance() {
        return new UserDto();
    }

}
