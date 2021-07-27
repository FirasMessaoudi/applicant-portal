/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.test.AbstractJpaTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Testing class for {@link UserRepository}
 *
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
public class UserRepositoryTest extends AbstractJpaTest {

    private final static String USER_PASSWORD_HASH = "$2a$10$A81/FuMFJWcxaJhUcL8isuVeKKa.hk7GVzTVTyf7xe/XoMVWuKckK";
    private final static int NIN_USER_NOT_DELETED = 1234567897;
    private final static int NIN_USER_DELETED = 1000000164;
    private final static int NIN_USER_FAKE = 1111111111;

    private final static int UIN_USER_FAKE = 1111111111;
    private final static long UIN_USER_NOT_DELETED = 223456789;
    private final static long UIN_USER_DELETED = 2000000164;


    @Autowired
    private UserRepository userRepository;

    @Test
    public void test_find_by_deleted_false() {
        Page<JpaUser> usersNotDeleted = userRepository.findDistinctByDeletedFalseAndIdNot(Mockito.any(Pageable.class), Mockito.anyLong());
        assertEquals(2, usersNotDeleted.getContent().size());
    }

    @Test
    public void test_find_by_nin_success() {
        JpaUser existingUser = userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(NIN_USER_NOT_DELETED);
        assertNotNull(existingUser);
        assertEquals(NIN_USER_NOT_DELETED, existingUser.getNin());
    }

    @Test
    public void test_find_by_uin_success() {
        JpaUser existingUser = userRepository.findByUinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(UIN_USER_NOT_DELETED);
        assertNotNull(existingUser);
        assertEquals(UIN_USER_NOT_DELETED, existingUser.getUin());
    }

    @Test
    @Transactional
    public void test_update_user_password_success() {
        String newPassHash = "ThisIsFakeTestingHash";
        JpaUser existingUser = userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(NIN_USER_NOT_DELETED);
        assertNotNull(existingUser);
        assertEquals(NIN_USER_NOT_DELETED, existingUser.getNin());
        assertEquals(USER_PASSWORD_HASH, existingUser.getPasswordHash());
        userRepository.updatePassword(NIN_USER_NOT_DELETED, newPassHash);
        entityManager.clear();
        existingUser = userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(NIN_USER_NOT_DELETED);
        assertEquals(newPassHash, existingUser.getPasswordHash());
        assertNotEquals(USER_PASSWORD_HASH, existingUser.getPasswordHash());
    }

    @Test
    public void test_find_by_nin_does_not_exist() {
        JpaUser nonExistingUser = userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(NIN_USER_FAKE);
        assertNull(nonExistingUser);
    }

    @Test
    public void test_find_by_uin_does_not_exist() {
        JpaUser nonExistingUser = userRepository.findByUinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(UIN_USER_FAKE);
        assertNull(nonExistingUser);
    }

    @Test
    public void test_retrieve_password_hash_success() {
        String passwordHash = userRepository.retrievePasswordHash(NIN_USER_NOT_DELETED);
        assertEquals(USER_PASSWORD_HASH, passwordHash);
    }

    @Test
    public void test_find_by_userName_not_deleted_user_deleted() {
        JpaUser deletedUser = userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(NIN_USER_DELETED);
        assertNull(deletedUser);
    }

    @Test
    public void test_find_by_uin_not_deleted_user_deleted() {
        JpaUser deletedUser = userRepository.findByUinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(UIN_USER_DELETED);
        assertNull(deletedUser);
    }

}
