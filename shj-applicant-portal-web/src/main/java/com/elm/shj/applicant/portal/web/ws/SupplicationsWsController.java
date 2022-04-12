package com.elm.shj.applicant.portal.web.ws;


import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.supplications.SupplicationsService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for exposing web services related to supplication for external party.
 * @author Nihed Sidhom
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_SUPPLICATION)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SupplicationsWsController {

    private final SupplicationsService supplicationsService ;

    @GetMapping("/findSupplicationsByType/{type}")
    public ResponseEntity<WsResponse<?>> findSupplicationsByType(@PathVariable("type") String type)
    {
        WsResponse wsResponse = supplicationsService.findSupplicationsByType(type);
        return ResponseEntity.ok(WsResponse.builder().status(wsResponse.getStatus()).body(wsResponse.getBody()).build());

    }

}
