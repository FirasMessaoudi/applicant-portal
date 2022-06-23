/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.SupplicationUserCounterDto;
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

    @GetMapping("/find-user-supplications")
    public ResponseEntity<WsResponse<?>> findUserSupplicationsByDigitalId( Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse wsResponse = rosaryService.findUserSupplicationsByDigitalId(loggedInUserUin);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @GetMapping("/find-supplications-lookup")
    public ResponseEntity<WsResponse<?>> findSupplicationsLookup() {
        WsResponse wsResponse = rosaryService.findSuggestedSupplicationLookup();
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @GetMapping("/find-supplications-user-counter")
    public ResponseEntity<WsResponse<?>> findSupplicationsUserCounterByDigitalId( Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse wsResponse = rosaryService.findSupplicationsUserCounterByDigitalId(loggedInUserUin);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @PostMapping("/delete-supplication/{id}")
    public  ResponseEntity<WsResponse<?>> deleteSupplication(@PathVariable("id") long id ){
        WsResponse wsResponse = rosaryService.deleteSupplication(id);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @PostMapping("/reset-supplication-number/{id}")
    public  ResponseEntity<WsResponse<?>> resetSupplicationCounter(@PathVariable("id") long id ){
        WsResponse wsResponse = rosaryService.resetSupplicationNumber(id);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @PostMapping("/update-supplication-numbers/{id}/{total}/{last}")
    public  ResponseEntity<WsResponse<?>> updateSupplicationCounter(@PathVariable("id") long id, @PathVariable("total") int total, @PathVariable("last") int last){
        WsResponse wsResponse = rosaryService.updateSupplicationNumbers(id,total,last);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());
    }
    @PostMapping("/save-supplication-user-counter")
    public ResponseEntity<WsResponse<?>> saveSupplicationCounter(@RequestBody SupplicationUserCounterDto supplicationUserCounterDto, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        supplicationUserCounterDto.setDigitalId(loggedInUserUin);
        WsResponse response =rosaryService.createSupplicationCounter(supplicationUserCounterDto);
        return ResponseEntity.ok(
                WsResponse.builder().status(response.getStatus())
                        .body(response.getBody()).build());
    }
}
