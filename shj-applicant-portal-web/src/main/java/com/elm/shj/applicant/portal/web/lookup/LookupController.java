/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.lookup;

import com.elm.shj.applicant.portal.services.dto.AuthorityConstants;
import com.elm.shj.applicant.portal.services.dto.AuthorityLookupDto;
import com.elm.shj.applicant.portal.services.lookup.AuthorityLookupService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * Controller for lookup data
 *
 * @author ahmad flaifel
 * @since 1.3.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_LOOKUP)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LookupController {

    private final AuthorityLookupService authorityLookupService;

    @GetMapping("/authority/list/parent")
    @RolesAllowed(AuthorityConstants.ROLE_MANAGEMENT)
    public List<AuthorityLookupDto> listParentAuthorities(Authentication authentication) {
        return authorityLookupService.findAllParentAuthorities();
    }

}
