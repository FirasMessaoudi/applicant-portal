export class DashboardVo {
  // users
  totalNumberOfUsers: number;

  // active users
  totalNumberOfCitizenActiveUsers: number;
  totalNumberOfResidentActiveUsers: number;

  // total users by status
  totalNumberOfActiveUsers: number;
  totalNumberOfInactiveUsers: number;
  totalNumberOfDeletedUsers: number;

  // roles
  totalNumberOfRoles: number;
  totalNumberOfActiveRoles: number;
  totalNumberOfInactiveRoles: number;

  // total users by authority
  totalUsersWithDashboardAccess: number;
  totalUsersWithUserManagementAccess: number;
  totalUsersWithRoleManagementAccess: number;

  // users by authority
  usersByParentAuthorityCountVoList: any;

  // period counts
  activeUsersCountVoList: Array<any>;
  inactiveUsersCountVoList: Array<any>;
  deletedUsersCountVoList: Array<any>;


}
