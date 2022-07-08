package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import com.elm.shj.applicant.portal.orm.entity.JpaOtpCache;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class OtpCacheDtoMapper implements IGenericMapper<OtpCacheDto, JpaOtpCache> {
}
