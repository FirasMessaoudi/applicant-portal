/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

/**
 * Repository for role table.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
public interface RoleRepository extends JpaRepository<JpaRole, Long> {

    long SYSTEM_ADMIN_ROLE_ID = 1;
    long SYSTEM_USER_ROLE_ID = 2;
    int SYSTEM_ADMIN_USER_ROLE_ID = 1;
    List<Long> SYSTEM_ROLE_ID_LIST = Arrays.asList(SYSTEM_ADMIN_ROLE_ID, SYSTEM_USER_ROLE_ID);

    Page<JpaRole> findByDeletedFalse(Pageable pageable);

    Page<JpaRole> findDistinctByDeletedFalseAndIdNot(Pageable pageable, long roleId);

    @Query("select distinct role from JpaRole role where " +
            "role.deleted = false and (role.activated = :activated or :activated is null) " +
            "and role.id <> :systemAdminRoleId ")
    List<JpaRole> findByDeletedFalseAndActivatedAndIdNot(@Param("activated") Boolean activated, @Param("systemAdminRoleId") long systemAdminRoleId);

    /**
     * Find not deleted and active roles not matching passed role ids.
     * @param roleIds
     * @return
     */
    List<JpaRole> findByDeletedFalseAndActivatedTrueAndIdNotIn(List<Long> roleIds);

    /**
     * Find not deleted and active roles matching passed role ids.
     * @param includedRoleIds
     * @return
     */
    List<JpaRole> findByDeletedFalseAndActivatedTrueAndIdIn(List<Long> includedRoleIds);

    @Query("select distinct role from JpaRole role where " +
            "role.deleted = false and (role.activated = :activated or :activated is null)")
    List<JpaRole> findByDeletedFalseAndActivated(@Param("activated") Boolean activated);

    JpaRole findById(long roleId);

    long countDistinctByDeletedFalse();
    long countDistinctByDeletedFalseAndActivated(boolean activated);

    @Modifying
    @Query("update JpaRole role set role.deleted = true where role.id =:roleId")
    void markDeleted(@Param("roleId") long roleId);

    @Modifying
    @Query("update JpaRole role set role.activated = true where role.id =:roleId")
    void activate(@Param("roleId") long roleId);

    @Modifying
    @Query("update JpaRole role set role.activated = false where role.id =:roleId")
    void deactivate(@Param("roleId") long roleId);

    @Query("select distinct role from JpaRole role join role.roleAuthorities ra where " +
            "ra.role.id = role.id and " +
            "ra.role.deleted = false and" +
            "(:authorityId = -1L or ra.authority.id = :authorityId or ra.authority.parentId = :authorityId) and " +
            "(:labelAr is null or lower(role.labelAr) like :labelAr) and " +
            "(:labelEn is null or lower(role.labelEn) like :labelEn)")
    Page<JpaRole> findByAuthorityOrName(Pageable pageable, @Param("authorityId") Long authorityId,
                                        @Param("labelAr") String arabicName,
                                        @Param("labelEn") String englishName);
}
