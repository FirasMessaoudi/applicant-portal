/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.shj.applicant.portal.services.dto.AuthorityConstants;
import com.elm.shj.applicant.portal.services.dto.AuthorityLookupDto;
import com.elm.shj.applicant.portal.services.dto.RoleAuthorityDto;
import com.elm.shj.applicant.portal.services.dto.RoleDto;
import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class for controller {@link RoleManagementController}
 *
 * @author Aymen DHAOUI
 * @since 1.8.0
 */
class RoleManagementControllerTest extends AbstractControllerTestSuite {


    private static final long TEST_AUTHORITY_ID = 5;
    private static final long TEST_ROLE_ID = 5;
    private static final String TEST_ARABIC_NAME = "تجرية";
    private static final String TEST_ENGLISH_NAME = "Test";
    private static final PageRequest TEST_PAGE = PageRequest.of(0, 10);
    private static final long NEW_ROLE_ID = 1001L;

    private List<RoleDto> roles;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        initRolesList();
        when(roleService.save(Mockito.any(RoleDto.class))).then((Answer<RoleDto>) this::mockSaveRole);
        when(roleService.findAll(any(Pageable.class), any())).thenReturn(new PageImpl<>(roles));
        when(roleService.findAll(any())).thenReturn(roles);
        when(roleService.searchByAuthorityOrName(any(), eq(TEST_AUTHORITY_ID), eq(TEST_ARABIC_NAME), eq(TEST_ENGLISH_NAME))).thenReturn(new PageImpl<>(roles));
        mockSuccessfulLogin();
        triggerLogin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tearDown() {
        // nothing to do
    }

    @Test
    void test_listAllRoles() throws Exception {
        String url = Navigation.API_ROLES + "/list/all";
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(roles.size())))
                .andExpect(jsonPath("$.[0].id", is((int) roles.get(0).getId())));


        verify(roleService, times(1)).findAll(any());
    }

    @Test
    void test_listPaginated() throws Exception {
        String url = Navigation.API_ROLES + "/list/paginated";
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.totalElements", is(roles.size())))
                .andExpect(jsonPath("$.content", hasSize(roles.size())))
                .andExpect(jsonPath("$.content[0].id", is((int) roles.get(0).getId())));


        verify(roleService, times(1)).findAll(any(Pageable.class), any());
    }

    @Test
    void test_search() throws Exception {
        String url = Navigation.API_ROLES + "/search";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", TEST_PAGE.toString());
        params.add("authorityId", ""+TEST_AUTHORITY_ID);
        params.add("labelAr", TEST_ARABIC_NAME);
        params.add("labelEn", TEST_ENGLISH_NAME);
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf()).params(params)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.totalElements", is(roles.size())))
                .andExpect(jsonPath("$.content", hasSize(roles.size())))
                .andExpect(jsonPath("$.content[0].id", is((int) roles.get(0).getId())));
    }

    @Test
    void test_findRole() throws Exception {
        String url = Navigation.API_ROLES + "/find/"+TEST_ROLE_ID;
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is((int)TEST_ROLE_ID)));

        verify(roleService, times(1)).findOne(TEST_ROLE_ID);
    }

    @Test
    void test_saveOrUpdateRole_success() throws Exception {
        String url = Navigation.API_ROLES + "/save-or-update";
        RoleDto role = new RoleDto();
        role.setLabelAr(TEST_ARABIC_NAME);
        role.setLabelEn(TEST_ENGLISH_NAME);
        AuthorityLookupDto authority = new AuthorityLookupDto();
        RoleAuthorityDto roleAuthority = new RoleAuthorityDto();
        roleAuthority.setAuthority(authority);
        authority.setCode(AuthorityConstants.ADD_USER);
        role.setRoleAuthorities(new HashSet<>(Collections.singletonList(roleAuthority)));
        mockMvc.perform(post(url).cookie(tokenCookie).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(role)).with(csrf()))
                .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is((int) NEW_ROLE_ID)));

        verify(roleService, times(1)).save(any(RoleDto.class));
    }

    @Test
    void test_saveOrUpdateRole_validation_error() throws Exception {
        String url = Navigation.API_ROLES + "/save-or-update";
        RoleDto role = new RoleDto();
        mockMvc.perform(post(url).cookie(tokenCookie).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(role)).with(csrf()))
                .andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    void test_deleteRole() throws Exception {
        int roleIdToDelete = random.nextInt(9) + 1;
        String url = Navigation.API_ROLES + "/delete/" + roleIdToDelete;
        int initialRolesCount = roles.size();
        mockMvc.perform(post(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(jsonPath("$").doesNotExist());
        verify(roleService, times(1)).deleteRole(roleIdToDelete);
        assertEquals(roles.size(), initialRolesCount - 1);
    }

    @Test
    void test_activateRole()  throws Exception {
        String url = Navigation.API_ROLES + "/activate/"+TEST_ROLE_ID;
        mockMvc.perform(post(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(roleService, times(1)).activateRole(TEST_ROLE_ID);
    }

    @Test
    void test_deactivateRole()  throws Exception {
        String url = Navigation.API_ROLES + "/deactivate/"+TEST_ROLE_ID;
        mockMvc.perform(post(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(roleService, times(1)).deactivateRole(TEST_ROLE_ID);
    }

    /**
     * Utility method to initiate roles list
     */
    private void initRolesList() {
        roles = new ArrayList<>();
        RoleDto role;
        RoleAuthorityDto roleAuthority = new RoleAuthorityDto();
        AuthorityLookupDto authority = new AuthorityLookupDto();
        authority.setCode(AuthorityConstants.ADD_ROLE);
        roleAuthority.setAuthority(authority);
        for (long i = 0; i < 10; i++) {
            Long nin = new Long("123456789" + i);
            role = new RoleDto();
            role.setId(i + 1);

            role.setRoleAuthorities(new HashSet<>(Collections.singletonList(roleAuthority)));
            roles.add(role);
            when(roleService.findOne(i + 1)).thenReturn(role);
            when(roleService.save(role)).thenReturn(role);

            doAnswer((Answer<Void>) invocation -> {
                Object[] args = invocation.getArguments();
                roles.remove(roles.get(Integer.parseInt("" + args[0]) - 1));
                return null;
            }).when(roleService).deleteRole(i + 1);

            doAnswer((Answer<Void>) invocation -> {
                // do nothing
                return null;
            }).when(roleService).deactivateRole(i + 1);

            doAnswer((Answer<Void>) invocation -> {
                // do nothing
                return null;
            }).when(roleService).activateRole(i + 1);

        }
    }

    /**
     * Used to mock role creation
     *
     * @param invocation the mock context
     * @return the mocked role after creation
     */
    private RoleDto mockSaveRole(InvocationOnMock invocation) {
        RoleDto roleToChange = invocation.getArgument(0);
        if (roleToChange == null) {
            return null;
        }
        if (roleToChange.getId() > 0) {
            roles.set((int) roleToChange.getId(), roleToChange);
        } else {
            roleToChange.setId(NEW_ROLE_ID);
            roles.add(roleToChange);
        }
        return roleToChange;
    }

}
