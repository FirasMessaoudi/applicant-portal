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
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ConfigurationService {


    @Value("${send.user.location.distance}")
    private int userLocationDistance;

    public List<ConfigDto> getMobileConfigurationsList() {
        List<ConfigDto> configList =new ArrayList<>();
        configList.add(getUserLocationDistanceConfig());
        return configList;
    }

    private ConfigDto getUserLocationDistanceConfig(){
        ConfigDto config = new ConfigDto();
        config.setConfKey("send.user.location.distance");
        config.setConfValue(String.valueOf(userLocationDistance));
        return config;
    }
}
