/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.services.test.DtoTest;

/**
 * Testing class for {@link AuthorityLookupDto}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class AuthorityLookupDtoTest extends DtoTest<AuthorityLookupDto> {

    @Override
    protected AuthorityLookupDto getInstance() {
        return new AuthorityLookupDto();
    }

}
