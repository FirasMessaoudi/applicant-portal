/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for AuditLog Table.
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public interface AuditLogRepository extends JpaRepository<JpaAuditLog, Long> {
}
