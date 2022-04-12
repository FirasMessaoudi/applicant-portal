/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.rosary;

import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service handling islamic rosary  operations
 *
 * @author r.chebbi
 * @since 1.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RosaryService {

    private final IntegrationService integrationService;

    public WsResponse findRosarySupplicationsByDigitalId(String digitalId) {
        return integrationService.findRosarySupplicationsByDigitalId(digitalId);
    }
    public WsResponse deleteSupplication(long id) {
        return integrationService.deleteSupplication(id);
    }
    public WsResponse resetSupplicationNumber(long id) {
        return integrationService.resetSupplicationNumber(id);
    }
    public WsResponse updateSupplicationNumbers(long id,int total,int last) {
        return integrationService.updateSupplicationNumbers(id,total,last);
    }
}
