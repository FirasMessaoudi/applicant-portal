/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.services.test.DtoTest;

/**
 * Testing class for {@link ELanguage}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class ELanguageTest extends DtoTest<ELanguage> {

    @Override
    protected ELanguage getInstance() {
        return ELanguage.ARABIC;
    }

    protected boolean skipGettersAndSettersTest() {
        return true;
    }

}
