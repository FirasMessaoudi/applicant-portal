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

    @Value("${send.user.location.batch.size}")
    private int userLocationBatchSize;

    @Value("${google.map.key}")
    private String googleMapKey;

    @Value("${user.location.tracking.enabled}")
    private String userLocationTrackingEnabled;

    public List<ConfigDto> getMobileConfigurationsList() {
        List<ConfigDto> configList =new ArrayList<>();
        configList.add(getUserLocationDistanceConfig());
        configList.add(getUserLocationBatchSizeConfig());
        configList.add(getGoogleMapKeyConfig());
        configList.add(getUserLocationTrackingEnabled());
        return configList;
    }

    private ConfigDto getUserLocationDistanceConfig(){
        ConfigDto config = new ConfigDto();
        config.setConfKey("send.user.location.distance");
        config.setConfValue(String.valueOf(userLocationDistance));
        return config;
    }
    private ConfigDto getUserLocationBatchSizeConfig(){
        ConfigDto config = new ConfigDto();
        config.setConfKey("send.user.location.batch.size");
        config.setConfValue(String.valueOf(userLocationBatchSize));
        return config;
    }
    private ConfigDto getGoogleMapKeyConfig(){
        ConfigDto config = new ConfigDto();
        config.setConfKey("google.map.key");
        config.setConfValue(String.valueOf(googleMapKey));
        return config;
    }

    public ConfigDto getUserLocationTrackingEnabled(){
        ConfigDto config = new ConfigDto();
        config.setConfKey("user.location.tracking.enabled");
        config.setConfValue(String.valueOf(userLocationTrackingEnabled));
        return config;
    }
}
