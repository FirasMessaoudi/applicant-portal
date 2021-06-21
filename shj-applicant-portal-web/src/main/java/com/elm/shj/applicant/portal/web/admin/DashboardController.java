/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.shj.applicant.portal.services.dashboard.DashboardService;
import com.elm.shj.applicant.portal.services.dashboard.DashboardVo;
import com.elm.shj.applicant.portal.services.dto.AuthorityConstants;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;


/**
 * Main controller for admin dashboard page after login
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@RestController
@RequestMapping(Navigation.API_DASHBOARD)
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DashboardController {

    private final DashboardService dashboardService;

    public enum EPeriodType {
        D, // daily
        W, // Weekly
        M, // Monthly
    }

    @GetMapping
    @RolesAllowed(AuthorityConstants.ADMIN_DASHBOARD)
    public DashboardVo loadDashboardData() {
        log.info("Handling loadDashboardData endpoint.");
        return dashboardService.loadDashboardData();
    }

    /**
     * Loads dashboard statistics for a specific period
     *
     * @param periodType the period type to load
     * @return dashboard statistics for the selected period
     */
    @GetMapping("/period/{periodType}")
    @RolesAllowed(AuthorityConstants.ADMIN_DASHBOARD)
    public DashboardVo loadPeriodicallyDashboardData(@PathVariable("periodType") EPeriodType periodType) {
        log.info("Handling loadPeriodDashboardData endpoint.");
        switch (periodType) {
            case D:
                return dashboardService.loadDailyDashboardData();
            case W:
                return dashboardService.loadWeeklyDashboardData();
            case M:
                return dashboardService.loadMonthlyDashboardData();
        }
        return null;
    }

}
