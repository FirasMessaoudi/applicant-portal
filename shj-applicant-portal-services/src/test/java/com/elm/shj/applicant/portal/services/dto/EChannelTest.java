/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.shj.applicant.portal.services.test.DtoTest;

/**
 * Testing class for {@link EChannel}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class EChannelTest extends DtoTest<EChannel> {

    @Override
    protected EChannel getInstance() {
        return EChannel.MOBILE;
    }

    protected boolean skipGettersAndSettersTest() {
        return true;
    }

}
