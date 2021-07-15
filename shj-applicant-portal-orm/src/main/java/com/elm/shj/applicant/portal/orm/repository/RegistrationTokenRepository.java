/*
 *  Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.CountVo;
import com.elm.shj.applicant.portal.orm.entity.JpaRegistrationToken;
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
public interface RegistrationTokenRepository extends JpaRepository<JpaRegistrationToken, Long> {

    Page<JpaRegistrationToken> findDistinctByDeletedFalseAndIdNot(Pageable pageable, long rtId);
    JpaRegistrationToken findByUinAndDeletedFalse(long uin);

    @Modifying
    @Query("update JpaRegistrationToken rt set rt.deleted = true  where rt.id =:rtId")
    void markDeleted(@Param("rtId") long rtId);

    @Modifying
    @Query("update JpaRegistrationToken rt set rt.tokenExpirationDate = null where rt.nin =:uin")
    void clearToken(@Param("uin") long uin);

    @Query("select rt.tokenExpirationDate from JpaRegistrationToken rt where rt.uin =:uin")
    Date retrieveTokenExpiryDate(@Param("uin") long uin);


}
