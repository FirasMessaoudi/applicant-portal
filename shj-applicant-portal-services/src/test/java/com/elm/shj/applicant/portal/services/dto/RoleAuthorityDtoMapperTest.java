/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.orm.entity.JpaRoleAuthority;
import com.elm.shj.applicant.portal.services.test.MapperTest;

/**
 * Testing class for {@link RoleAuthorityDtoMapper}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class RoleAuthorityDtoMapperTest extends MapperTest<RoleAuthorityDto, JpaRoleAuthority, RoleAuthorityDtoMapper> {

    @Override
    protected Class<RoleAuthorityDtoMapper> getMapperClass() {
        return RoleAuthorityDtoMapper.class;
    }

    @Override
    protected RoleAuthorityDto getDtoInstance() {
        return new RoleAuthorityDto();
    }

    @Override
    protected JpaRoleAuthority getEntityInstance() {
        return new JpaRoleAuthority();
    }
}

