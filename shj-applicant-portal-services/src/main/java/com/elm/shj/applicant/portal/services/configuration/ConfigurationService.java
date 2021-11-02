/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.configuration;

import com.elm.shj.applicant.portal.services.dto.ConfigDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service handling configuration
 *
 * @author salzoubi
 * @since 1.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ConfigurationService {

    @Value("${send.user.location.period.in.minutes}")
    private int locationRequestInMinutes;

    public List<ConfigDto> findUserLocationRequestDuration() {
        List<ConfigDto> configList =new ArrayList<>();
        ConfigDto config = new ConfigDto();
        config.setConfKey("send.user.location.period.in.minutes");
        config.setConfValue(String.valueOf(locationRequestInMinutes));
        configList.add(config);

        return configList;
    }
}
