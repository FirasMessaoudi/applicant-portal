/*
 * Copyright (c) 2019 ELM. All rights reserved.
 */

package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import com.elm.shj.applicant.portal.orm.entity.JpaConfig;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link ConfigDto} class
 *
 * @author Aymen DHAOUI
 * @since 1.5.0
 */
@Mapper(componentModel = "spring")
public abstract class ConfigDtoMapper implements IGenericMapper<ConfigDto, JpaConfig> {
}
