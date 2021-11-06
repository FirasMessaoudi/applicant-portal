package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import com.elm.shj.applicant.portal.orm.entity.JpaUserLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserLocationDtoMapper implements IGenericMapper<UserLocationDto, JpaUserLocation> {
}
