/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.audit;

import com.elm.shj.applicant.portal.orm.entity.JpaAuditLog;
import com.elm.shj.applicant.portal.services.dto.AuditLogDto;
import com.elm.shj.applicant.portal.services.generic.GenericService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service handling audit logs
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Service
public class AuditLogService extends GenericService<JpaAuditLog, AuditLogDto, Long> {

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public AuditLogDto save(AuditLogDto dto) {
        // truncate error details
        dto.setErrorDetails(StringUtils.left(dto.getErrorDetails(), 512));
        // truncate params
        dto.setParams(StringUtils.left(dto.getParams(), 1000));
        return super.save(dto);
    }
}
