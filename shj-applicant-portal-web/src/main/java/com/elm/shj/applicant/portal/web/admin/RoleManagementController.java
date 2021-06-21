/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.shj.applicant.portal.services.dto.AuthorityConstants;
import com.elm.shj.applicant.portal.services.dto.RoleDto;
import com.elm.shj.applicant.portal.services.role.RoleService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Main controller for role management page
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@RestController
@RequestMapping(Navigation.API_ROLES)
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RoleManagementController {

    private final RoleService roleService;
    private final JwtTokenService jwtTokenService;

    /**
     * List active roles.
     *
     * @param authentication the logged in user information
     * @return the list of roles matching criteria
     */
    @GetMapping("/list/all")
    @RolesAllowed({AuthorityConstants.ROLE_MANAGEMENT})
    public List<RoleDto> listAllRoles(Authentication authentication) {
        log.info("list active roles.");
        return roleService.findAll(jwtTokenService.retrieveUserRoleIdsFromToken(((JwtToken) authentication).getToken()).orElse(new HashSet<>()));
    }

    /**
     * List active non system roles.
     * @param authentication the logged in user information
     * @return the list of active roles
     */
    @GetMapping("/list/active")
    @RolesAllowed({AuthorityConstants.USER_MANAGEMENT})
    public List<RoleDto> listActiveRoles(Authentication authentication) {
        log.info("list active roles.");
        return roleService.findActive(jwtTokenService.retrieveUserRoleIdsFromToken(((JwtToken) authentication).getToken()).orElse(new HashSet<>()));
    }

    @GetMapping("/list/paginated")
    @RolesAllowed(AuthorityConstants.ROLE_MANAGEMENT)
    public Page<RoleDto> listPaginated(Pageable pageable, Authentication authentication) {
        log.info("list all roles.");
        return roleService.findAll(pageable, jwtTokenService.retrieveUserRoleIdsFromToken(((JwtToken) authentication).getToken()).orElse(new HashSet<>()));
    }

    @GetMapping("/search")
    @RolesAllowed(AuthorityConstants.ROLE_MANAGEMENT)
    public Page<RoleDto> search(Pageable pageable, @RequestParam(required = false) Long authorityId,
                                @RequestParam(required = false) String arabicName,
                                @RequestParam(required = false) String englishName) {
        log.info("Search roles by authority and name");
        return roleService.searchByAuthorityOrName(pageable, authorityId, arabicName, englishName);
    }

    /**
     * finds a role by its ID
     *
     * @param roleId the role id to find
     * @return the found role or <code>null</code>
     */
    @GetMapping("/find/{roleId}")
    @RolesAllowed(AuthorityConstants.ROLE_MANAGEMENT)
    public RoleDto findRole(@PathVariable long roleId) {
        log.debug("Handler for {}", "Find Role");
        return roleService.findOne(roleId);
    }

    /**
     * Creates new role
     *
     * @param role the role to create
     * @return the created role
     */
    @PostMapping("/create")
    @RolesAllowed(AuthorityConstants.ADD_ROLE)
    public ResponseEntity<RoleDto> CreateRole(@RequestBody @Validated({RoleDto.CreateRoleValidationGroup.class, Default.class}) RoleDto role) {
        log.debug("Handler for {}", "Create Role");
        role.getRoleAuthorities().forEach(roleAuthorityDto -> {
            roleAuthorityDto.setRole(role);
            roleAuthorityDto.setCreationDate(new Date());
        });
        role.setCreationDate(new Date());
        return ResponseEntity.ok(roleService.save(role));
    }

    /**
     * Updates an existing role
     *
     * @param role the role to update
     * @return the updated role
     */
    @PostMapping("/update")
    @RolesAllowed(AuthorityConstants.EDIT_ROLE)
    public ResponseEntity<RoleDto> UpdateRole(@RequestBody @Validated RoleDto role) {
        log.debug("Handler for {}", "Update Role");
        role.getRoleAuthorities().forEach(roleAuthorityDto -> {
            roleAuthorityDto.setRole(role);
            roleAuthorityDto.setCreationDate(new Date());
        });
        role.setUpdateDate(new Date());
        return ResponseEntity.ok(roleService.save(role));
    }

    /**
     * Deletes the role by its ID
     *
     * @param roleId the role id for to load details
     * @return the {@link ResponseEntity} with status
     */
    @PostMapping("/delete/{roleId}")
    @RolesAllowed(AuthorityConstants.DELETE_ROLE)
    public ResponseEntity<String> deleteRole(@PathVariable long roleId) {
        log.debug("Handler for {}", "delete role");
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(StringUtils.EMPTY);
    }

    /**
     * Activate inactive role
     *
     * @param roleId
     * @return
     */
    @PostMapping("/activate/{roleId}")
    @RolesAllowed(AuthorityConstants.CHANGE_ROLE_STATUS)
    public ResponseEntity<String> activateRole(@PathVariable long roleId) {
        log.debug("Handler for {}", "activate role");
        roleService.activateRole(roleId);
        return ResponseEntity.ok(StringUtils.EMPTY);
    }

    /**
     * Deactivate active role
     *
     * @param roleId
     * @return
     */
    @PostMapping("/deactivate/{roleId}")
    @RolesAllowed(AuthorityConstants.CHANGE_ROLE_STATUS)
    public ResponseEntity<String> deactivateRole(@PathVariable long roleId) {
        log.debug("Handler for {}", "deactivate role");
        roleService.deactivateRole(roleId);
        return ResponseEntity.ok(StringUtils.EMPTY);
    }

}
