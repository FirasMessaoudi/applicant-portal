/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import com.elm.shj.applicant.portal.orm.entity.JpaAuditLog;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link AuditLogDto} class
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public abstract class AuditLogDtoMapper implements IGenericMapper<AuditLogDto, JpaAuditLog> {
}
