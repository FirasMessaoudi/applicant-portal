/*
 *  Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaOtpCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for User Table.
 *
 * @author ahmad flaifel
 * @since 1.2.9
 */
public interface OtpCacheRepository extends JpaRepository<JpaOtpCache, Long> {

    Optional<JpaOtpCache> findDistinctTopByPrincipleAndOtpOrderByCreationDate(String principle, String otp);

    void deleteAllByPrinciple(String principle);
}
