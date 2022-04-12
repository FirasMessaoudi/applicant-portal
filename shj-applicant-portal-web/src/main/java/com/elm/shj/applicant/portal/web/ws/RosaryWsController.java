/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.rosary.RosaryService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for exposing applicant supplication web services for external party.
 *
 * @author r.chebbi
 * @since 1.1.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_ROSARY)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RosaryWsController {
    private final RosaryService rosaryService;

    @GetMapping("/find-rosary-supplications")
    public ResponseEntity<WsResponse<?>> findRosarySupplicationsByDigitalId( Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse wsResponse = rosaryService.findRosarySupplicationsByDigitalId(loggedInUserUin);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @PutMapping("/delete-supplication/{id}")
    public  ResponseEntity<WsResponse<?>> deleteSupplication(@PathVariable("id") long id ){
        WsResponse wsResponse = rosaryService.deleteSupplication(id);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @PutMapping("/reset-supplication-number/{id}")
    public  ResponseEntity<WsResponse<?>> resetSupplicationNumber(@PathVariable("id") long id ){
        WsResponse wsResponse = rosaryService.resetSupplicationNumber(id);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @PutMapping("/update-supplication-numbers/{id}/{total}/{last}")
    public  ResponseEntity<WsResponse<?>> updateSupplicationNumbers(@PathVariable("id") long id,@PathVariable("total") int total,@PathVariable("last") int last){
        WsResponse wsResponse = rosaryService.updateSupplicationNumbers(id,total,last);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
}
