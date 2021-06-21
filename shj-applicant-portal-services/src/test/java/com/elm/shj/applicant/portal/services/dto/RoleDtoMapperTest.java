/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.orm.entity.JpaRole;
import com.elm.shj.applicant.portal.services.test.MapperTest;

/**
 * Testing class for {@link RoleDtoMapper}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class RoleDtoMapperTest extends MapperTest<RoleDto, JpaRole, RoleDtoMapper> {

    @Override
    protected Class<RoleDtoMapper> getMapperClass() {
        return RoleDtoMapper.class;
    }

    @Override
    protected RoleDto getDtoInstance() {
        return new RoleDto();
    }

    @Override
    protected JpaRole getEntityInstance() {
        return new JpaRole();
    }
}

