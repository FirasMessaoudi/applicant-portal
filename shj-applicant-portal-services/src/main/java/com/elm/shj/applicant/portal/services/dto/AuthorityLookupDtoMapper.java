/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import com.elm.shj.applicant.portal.orm.entity.JpaAuthorityLookup;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link AuthorityLookupDto} class
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Mapper(componentModel = "spring")
public abstract class AuthorityLookupDtoMapper implements IGenericMapper<AuthorityLookupDto, JpaAuthorityLookup> {
}
