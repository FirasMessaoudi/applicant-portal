/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for controller {@link DashboardController}
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public class DashboardControllerTest extends AbstractControllerTestSuite {


    @Test
    public void test_get_request() throws Exception {
        mockMvc.perform(get(Navigation.API_DASHBOARD).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(dashboardService, times(1)).loadDashboardData();
    }

    @Test
    public void test_loadPeriodicallyDashboardData_daily() throws Exception {
        String url = Navigation.API_DASHBOARD + "/period/" + DashboardController.EPeriodType.D;
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(dashboardService, times(1)).loadDailyDashboardData();
    }

    @Test
    public void test_loadPeriodicallyDashboardData_weekly() throws Exception {
        String url = Navigation.API_DASHBOARD + "/period/" + DashboardController.EPeriodType.W;
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(dashboardService, times(1)).loadWeeklyDashboardData();
    }

    @Test
    public void test_loadPeriodicallyDashboardData_monthly() throws Exception {
        String url = Navigation.API_DASHBOARD + "/period/" + DashboardController.EPeriodType.M;
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(dashboardService, times(1)).loadMonthlyDashboardData();
    }

    @Test
    public void test_loadPeriodicallyDashboardData_does_not_exit() throws Exception {
        String url = Navigation.API_DASHBOARD + "/period/dummy";
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        mockSuccessfulLogin();
        triggerLogin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tearDown() {
        // nothing to do
    }
}
