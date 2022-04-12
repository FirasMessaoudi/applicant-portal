package com.elm.shj.applicant.portal.services.supplications;

import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service handling supplication operations
 *
 * @author Nihed Sidhom
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SupplicationsService {
    private final IntegrationService integrationService;


    public WsResponse findSupplicationsByType(String type) {

        return integrationService.findSupplicationsByType(type);
    }

}



