/*
 *  Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaUserPasswordHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for password history table.
 *
 * @author Ahmad Flaifel
 * @since 1.3.0
 */
public interface PasswordHistoryRepository extends JpaRepository<JpaUserPasswordHistory, Long> {

    @Query("SELECT ph FROM JpaUserPasswordHistory ph WHERE ph.userId = :userId")
    List<JpaUserPasswordHistory> findByUserIdOrderByCreationDateDesc(@Param("userId") long userId, Pageable pageable);

    JpaUserPasswordHistory findFirst1ByUserIdOrderByCreationDateDesc(long userId);
}
