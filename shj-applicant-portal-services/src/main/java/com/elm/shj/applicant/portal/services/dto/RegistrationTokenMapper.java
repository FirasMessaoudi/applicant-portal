package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import com.elm.shj.applicant.portal.orm.entity.JpaRegistrationToken;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RegistrationTokenMapper implements IGenericMapper<RegistrationTokenDto, JpaRegistrationToken> {
}