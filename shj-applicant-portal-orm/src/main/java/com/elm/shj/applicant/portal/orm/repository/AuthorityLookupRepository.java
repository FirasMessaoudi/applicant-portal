/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaAuthorityLookup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for authority lookup table.
 *
 * @author Ahmad Flaifel
 * @since 1.8.0
 */
public interface AuthorityLookupRepository extends JpaRepository<JpaAuthorityLookup, Long> {

    List<JpaAuthorityLookup> findAllByParentIsNull();
}
