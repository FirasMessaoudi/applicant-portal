/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dashboard;

import com.elm.shj.applicant.portal.services.test.DtoTest;

/**
 * Testing class for {@link DashboardVo}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class DashboardVoTest extends DtoTest<DashboardVo> {

    @Override
    protected DashboardVo getInstance() {
        return new DashboardVo();
    }

}
