/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dashboard;

import com.elm.shj.applicant.portal.orm.entity.CountVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Dashboard data that will be received
 *
 * @author ahmad flaifel
 * @since 1.4.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardVo implements Serializable {

    private static final long serialVersionUID = 7732699265219001830L;

    // users
    private long totalNumberOfUsers;

    // active users
    private long totalNumberOfCitizenActiveUsers;
    private long totalNumberOfResidentActiveUsers;

    // total users by status
    private long totalNumberOfActiveUsers;
    private long totalNumberOfInactiveUsers;
    private long totalNumberOfDeletedUsers;

    // roles
    private long totalNumberOfRoles;
    private long totalNumberOfActiveRoles;
    private long totalNumberOfInactiveRoles;

    // total users by authority
    private long totalUsersWithDashboardAccess;
    private long totalUsersWithUserManagementAccess;
    private long totalUsersWithRoleManagementAccess;

    // users by authority
    private List<CountVo> usersByParentAuthorityCountVoList;

    // period counts
    private List<CountVo> createdUsersCountVoList;
    private List<CountVo> activeUsersCountVoList;
    private List<CountVo> inactiveUsersCountVoList;
    private List<CountVo> deletedUsersCountVoList;


}
