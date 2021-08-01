/*
 *  Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.CountVo;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Repository for User Table.
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<JpaUser, Long> {

    Page<JpaUser> findDistinctByDeletedFalseAndIdNot(Pageable pageable, long userId);

    JpaUser findDistinctByDeletedFalseAndUinEqualsAndDateOfBirthGregorianEquals(long uin, Date dateOfBirth);

    Page<JpaUser> findDistinctByDeletedFalseAndIdNotAndUserRolesRoleIdNot(Pageable pageable, long userId, long roleId);

    JpaUser findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(long nin);

    JpaUser findByUinAndDeletedFalseAndActivatedTrue(long uin);

    @Modifying
    @Query("update JpaUser user set user.deleted = true, user.actionDate = CURRENT_TIMESTAMP where user.id =:userId")
    void markDeleted(@Param("userId") long userId);

    @Modifying
    @Query("update JpaUser user set user.activated = true, user.actionDate = CURRENT_TIMESTAMP where user.id =:userId")
    void activate(@Param("userId") long userId);

    @Modifying
    @Query("update JpaUser user set user.activated = false, user.actionDate = CURRENT_TIMESTAMP where user.id =:userId")
    void deactivate(@Param("userId") long userId);

    @Modifying
    @Query("update JpaUser user set user.passwordHash = :passwordHash, user.changePasswordRequired = false, " +
            "user.updateDate = CURRENT_TIMESTAMP where user.uin =:idNumber")
    void updatePassword(@Param("idNumber") long idNumber, @Param("passwordHash") String passwordHash);

    @Modifying
    @Query("update JpaUser user set user.avatar = :avatar, user.updateDate = CURRENT_TIMESTAMP  where user.id =:userId")
    void updateAvatar(@Param("userId") long userId, @Param("avatar") String avatar);

    @Modifying
    @Query("update JpaUser user set user.tokenExpiryDate = null where user.uin =:idNumber")
    void clearToken(@Param("idNumber") long idNumber);

    @Query("select user.passwordHash from JpaUser user where user.uin =:idNumber")
    String retrievePasswordHash(@Param("idNumber") long idNumber);

    @Query("select user.tokenExpiryDate from JpaUser user where user.uin =:idNumber")
    Date retrieveTokenExpiryDate(@Param("idNumber") long idNumber);

    @Modifying
    @Query("update JpaUser user set user.passwordHash = :pwdHash, user.changePasswordRequired = :changePwd where user.id =:userId")
    void resetPwd(@Param("userId") long userId, @Param("pwdHash") String pwdHash, @Param("changePwd") boolean changePwd);

    @Modifying
    @Query("update JpaUser user set user.numberOfTries = :numberOfTries, user.updateDate = :updateDate where user.id =:userId")
    void updateLoginTries(@Param("userId") long userId, @Param("numberOfTries") int numberOfTries, @Param("updateDate") Date updateDate);

    @Modifying
    @Query("update JpaUser user set user.numberOfTries = :numberOfTries, user.updateDate = :updateDate, " +
            "user.lastLoginDate = :lastLoginDate, user.tokenExpiryDate = :tokenExpiryDate where user.id =:userId")
    void updateLoginInfo(@Param("numberOfTries") int numberOfTries, @Param("lastLoginDate") Date lastLoginDate,
                         @Param("updateDate") Date updateDate, @Param("tokenExpiryDate") Date tokenExpiryDate, @Param("userId") long userId);


    @Query("select distinct u from JpaUser u left join u.userRoles ur where u.deleted = false and " +
            "(ur.role.id = :roleId or :roleId is null) and " +
            "(concat('', u.nin) like :nin or :nin is null) and " +
            "(u.activated = :activated or :activated is null) and " +
            "u.id <> :loggedInUserId and " +
            "(ur.role.id <> :systemAdminRoleId or :systemAdminRoleId is null)")
    Page<JpaUser> findByRoleOrNinOrStatus(Pageable pageable, @Param("roleId") Long roleId, @Param("nin") String nin,
                                          @Param("activated") Boolean activated, @Param("loggedInUserId") long loggedInUserId,
                                          @Param("systemAdminRoleId") Long systemAdminRoleId);


    // ----------------------------------------------------- //
    //                     STATISTICAL QUERIES
    // ----------------------------------------------------- //

    // --------------------------------
    // -------- COUNTS
    // --------------------------------
    long countDistinctByDeletedFalse();
    long countDistinctByDeletedFalseAndActivated(boolean activated);
    long countDistinctByDeletedTrue();

    @Query("SELECT COUNT(DISTINCT u) FROM JpaUser u WHERE u.deleted = false AND u.activated = true AND CAST(u.nin AS string) like :ninPattern")
    long countDistinctByDeletedFalseAndActivatedTrueAndNinIsLike(@Param("ninPattern") String ninPattern);

    // --------------------------------
    // -------- USERS BY AUTHORITY
    // --------------------------------
    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo(ra.authority.labelAr, 0, COUNT(DISTINCT u)) " +
            "FROM JpaUser u LEFT JOIN JpaUserRole ur ON ur.user.id = u.id LEFT JOIN JpaRoleAuthority ra ON ur.role.id = ra.role.id " +
            "WHERE u.deleted = false AND ra.authority.parent IS NULL " +
            "GROUP BY ra.authority.labelAr")
    List<CountVo> countUsersByParentAuthority();
    // --------------------------------
    // -------- HOURLY USERS BY STATUS
    // --------------------------------
    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', FUNCTION('HOUR', u.creationDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.deleted = false AND u.creationDate >= :currentDate " +
            "GROUP BY FUNCTION('HOUR', u.creationDate) ORDER BY FUNCTION('HOUR', u.creationDate)")
    List<CountVo> countHourlyCreatedUsers(@Param("currentDate") Date currentDate);

    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', FUNCTION('HOUR', u.actionDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.activated = :activated AND u.deleted = false AND u.actionDate >= :currentDate " +
            "GROUP BY FUNCTION('HOUR', u.actionDate) ORDER BY FUNCTION('HOUR', u.actionDate)")
    List<CountVo> countHourlyActiveInactiveUsers(@Param("currentDate") Date currentDate, @Param("activated") boolean activated);

    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', FUNCTION('HOUR', u.actionDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.deleted = true AND u.actionDate >= :currentDate " +
            "GROUP BY FUNCTION('HOUR', u.actionDate) ORDER BY FUNCTION('HOUR', u.actionDate)")
    List<CountVo> countHourlyDeletedUsers(@Param("currentDate") Date currentDate);
    // --------------------------------
    // -------- WEEKLY USERS BY STATUS
    // --------------------------------
    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', EXTRACT(DW FROM u.creationDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.deleted = false AND u.creationDate >= :currentDate " +
            "GROUP BY EXTRACT(DW FROM u.creationDate) ORDER BY EXTRACT(DW FROM u.creationDate)")
    List<CountVo> countWeekDayCreatedUsers(@Param("currentDate") Date startOfWeekDate);

    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', EXTRACT(DW FROM u.actionDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.activated = :activated AND u.deleted = false AND u.actionDate >= :currentDate " +
            "GROUP BY EXTRACT(DW FROM u.actionDate) ORDER BY EXTRACT(DW FROM u.actionDate)")
    List<CountVo> countWeekDayActiveInactiveUsers(@Param("currentDate") Date startOfWeekDate, @Param("activated") boolean activated);

    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', EXTRACT(DW FROM u.actionDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.deleted = true AND u.actionDate >= :currentDate " +
            "GROUP BY EXTRACT(DW FROM u.actionDate) ORDER BY EXTRACT(DW FROM u.actionDate)")
    List<CountVo> countWeekDayDeletedUsers(@Param("currentDate") Date startOfWeekDate);
    // --------------------------------
    // -------- MONTHLY USERS BY STATUS
    // --------------------------------
    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', FUNCTION('DAY', u.creationDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.deleted = false AND u.creationDate >= :currentDate " +
            "GROUP BY FUNCTION('DAY', u.creationDate) ORDER BY FUNCTION('DAY', u.creationDate)")
    List<CountVo> countMonthDayCreatedUsers(@Param("currentDate") Date startOfMonthDate);

    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', FUNCTION('DAY', u.actionDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.activated = :activated AND u.deleted = false AND u.actionDate >= :currentDate " +
            "GROUP BY FUNCTION('DAY', u.actionDate) ORDER BY FUNCTION('DAY', u.actionDate)")
    List<CountVo> countMonthDayActiveInactiveUsers(@Param("currentDate") Date startOfMonthDate, @Param("activated") boolean activated);

    @Query("SELECT NEW com.elm.shj.applicant.portal.orm.entity.CountVo('', FUNCTION('DAY', u.actionDate), COUNT(u)) FROM JpaUser u " +
            "WHERE u.deleted = true AND u.actionDate >= :currentDate " +
            "GROUP BY FUNCTION('DAY', u.actionDate) ORDER BY FUNCTION('DAY', u.actionDate)")
    List<CountVo> countMonthDayDeletedUsers(@Param("currentDate") Date startOfMonthDate);
}
