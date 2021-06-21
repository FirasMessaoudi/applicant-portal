/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.orm.entity.JpaAuthorityLookup;
import com.elm.shj.applicant.portal.services.test.MapperTest;

/**
 * Testing class for {@link AuthorityLookupDtoMapper}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class AuthorityLookupDtoMapperTest extends MapperTest<AuthorityLookupDto, JpaAuthorityLookup, AuthorityLookupDtoMapper> {

    @Override
    protected Class<AuthorityLookupDtoMapper> getMapperClass() {
        return AuthorityLookupDtoMapper.class;
    }

    @Override
    protected AuthorityLookupDto getDtoInstance() {
        return new AuthorityLookupDto();
    }

    @Override
    protected JpaAuthorityLookup getEntityInstance() {
        return new JpaAuthorityLookup();
    }
}
