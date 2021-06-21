/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.services.test.DtoTest;

/**
 * Testing class for {@link RoleDto}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class RoleDtoTest extends DtoTest<RoleDto> {

    @Override
    protected RoleDto getInstance() {
        return new RoleDto();
    }

}
