/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import com.elm.shj.applicant.portal.orm.test.DtoTest;

/**
 * Testing class for {@link JpaConfig}
 *
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
public class JpaConfigTest extends DtoTest<JpaConfig> {

    @Override
    protected JpaConfig getInstance() {
        return new JpaConfig();
    }

}
