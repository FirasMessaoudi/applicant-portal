/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dashboard;

import com.elm.shj.applicant.portal.orm.entity.CountVo;
import com.elm.shj.applicant.portal.orm.repository.RoleRepository;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

/**
 * Service handling dashboard related operations
 *
 * @author ahmad flaifel
 * @since 1.4.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DashboardService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DashboardVo loadDashboardData() {
        log.info("Start loading dashboard data");
        long totalNumberOfUsers = userRepository.countDistinctByDeletedFalse();

        long totalNumberOfActiveUsers = userRepository.countDistinctByDeletedFalseAndActivated(true);
        long totalNumberOfInactiveUsers = userRepository.countDistinctByDeletedFalseAndActivated(false);
        long totalNumberOfCitizenActiveUsers = userRepository.countDistinctByDeletedFalseAndActivatedTrueAndNinIsLike("1%");
        long totalNumberOfResidentActiveUsers = userRepository.countDistinctByDeletedFalseAndActivatedTrueAndNinIsLike("2%");
        long totalNumberOfDeletedUsers = userRepository.countDistinctByDeletedTrue();

        List<CountVo> usersByParentAuthorityCountVoList = userRepository.countUsersByParentAuthority();

        long totalNumberOfRoles = roleRepository.countDistinctByDeletedFalse();
        long totalNumberOfActiveRoles = roleRepository.countDistinctByDeletedFalseAndActivated(true);
        long totalNumberOfInactiveRoles = roleRepository.countDistinctByDeletedFalseAndActivated(false);

        DashboardVo hourlyDashboard = loadDailyDashboardData();

        return DashboardVo.builder()
                // users
                .totalNumberOfUsers(totalNumberOfUsers)
                // active users
                .totalNumberOfCitizenActiveUsers(totalNumberOfCitizenActiveUsers)
                .totalNumberOfResidentActiveUsers(totalNumberOfResidentActiveUsers)
                // total users by status
                .totalNumberOfActiveUsers(totalNumberOfActiveUsers)
                .totalNumberOfInactiveUsers(totalNumberOfInactiveUsers)
                .totalNumberOfDeletedUsers(totalNumberOfDeletedUsers)
                // users by authority
                .usersByParentAuthorityCountVoList(usersByParentAuthorityCountVoList)
                // roles
                .totalNumberOfRoles(totalNumberOfRoles)
                .totalNumberOfActiveRoles(totalNumberOfActiveRoles)
                .totalNumberOfInactiveRoles(totalNumberOfInactiveRoles)
                // period counts
                .createdUsersCountVoList(hourlyDashboard.getCreatedUsersCountVoList())
                .activeUsersCountVoList(hourlyDashboard.getActiveUsersCountVoList())
                .inactiveUsersCountVoList(hourlyDashboard.getInactiveUsersCountVoList())
                .deletedUsersCountVoList(hourlyDashboard.getDeletedUsersCountVoList())
                .build();
    }

    /**
     * Load hourly dashboard permits counts starting from 00:00 time.
     *
     * @return
     */
    public DashboardVo loadDailyDashboardData() {

        Date startOfDayDate = Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault())));

        List<CountVo> createdUsersCountVoList = userRepository.countHourlyCreatedUsers(startOfDayDate);
        List<CountVo> activeUsersCountVoList = userRepository.countHourlyActiveInactiveUsers(startOfDayDate, true);
        List<CountVo> inactiveUsersCountVoList = userRepository.countHourlyActiveInactiveUsers(startOfDayDate, false);
        List<CountVo> deletedUsersCountVoList = userRepository.countHourlyDeletedUsers(startOfDayDate);

        return DashboardVo.builder()
                .createdUsersCountVoList(createdUsersCountVoList)
                .activeUsersCountVoList(activeUsersCountVoList)
                .inactiveUsersCountVoList(inactiveUsersCountVoList)
                .deletedUsersCountVoList(deletedUsersCountVoList)
                .build();
    }

    /**
     * Load week day dashboard permits counts starting from the beginning of this week (Sunday) till today.
     *
     * @return
     */
    public DashboardVo loadWeeklyDashboardData() {

        Date startOfWeekDate = Date.from(Instant.from(LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                .atStartOfDay(ZoneId.systemDefault())));

        List<CountVo> createdUsersCountVoList = userRepository.countWeekDayCreatedUsers(startOfWeekDate);
        List<CountVo> activeUsersCountVoList = userRepository.countWeekDayActiveInactiveUsers(startOfWeekDate, true);
        List<CountVo> inactiveUsersCountVoList = userRepository.countWeekDayActiveInactiveUsers(startOfWeekDate, false);
        List<CountVo> deletedUsersCountVoList = userRepository.countWeekDayDeletedUsers(startOfWeekDate);

        return DashboardVo.builder()
                .createdUsersCountVoList(createdUsersCountVoList)
                .activeUsersCountVoList(activeUsersCountVoList)
                .inactiveUsersCountVoList(inactiveUsersCountVoList)
                .deletedUsersCountVoList(deletedUsersCountVoList)
                .build();
    }

    /**
     * Load daily dashboard permits counts starting from the beginning of this month till today.
     *
     * @return
     */
    public DashboardVo loadMonthlyDashboardData() {
        Date startOfMonthDate = Date.from(Instant.from(LocalDate.now().
                with(TemporalAdjusters.firstDayOfMonth()).
                atStartOfDay(ZoneId.systemDefault())));

        List<CountVo> createdUsersCountVoList = userRepository.countMonthDayCreatedUsers(startOfMonthDate);
        List<CountVo> activeUsersCountVoList = userRepository.countMonthDayActiveInactiveUsers(startOfMonthDate, true);
        List<CountVo> inactiveUsersCountVoList = userRepository.countMonthDayActiveInactiveUsers(startOfMonthDate, false);
        List<CountVo> deletedUsersCountVoList = userRepository.countMonthDayDeletedUsers(startOfMonthDate);

        return DashboardVo.builder()
                .createdUsersCountVoList(createdUsersCountVoList)
                .activeUsersCountVoList(activeUsersCountVoList)
                .inactiveUsersCountVoList(inactiveUsersCountVoList)
                .deletedUsersCountVoList(deletedUsersCountVoList)
                .build();
    }
}
