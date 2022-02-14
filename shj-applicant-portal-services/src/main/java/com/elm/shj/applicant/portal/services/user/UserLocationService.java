package com.elm.shj.applicant.portal.services.user;

import com.elm.shj.applicant.portal.services.dto.UserLocationDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserLocationService  {

    private final IntegrationService integrationService;

    public WsResponse storeUserLocation(List<UserLocationDto> locations) {

        return integrationService.storeUserLocation(locations);

    }
}
