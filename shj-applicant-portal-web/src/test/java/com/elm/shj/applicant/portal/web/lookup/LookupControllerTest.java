/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.lookup;

import com.elm.shj.applicant.portal.web.AbstractControllerTestSuite;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for controller {@link LookupController}
 *
 * @author Aymen DHAOUI
 * @since 1.8.0
 */
class LookupControllerTest extends AbstractControllerTestSuite {

    @Test
    void test_listParentAuthorities() throws Exception {
        String url = Navigation.API_LOOKUP + "/authority/list/parent";
        mockMvc.perform(get(url).cookie(tokenCookie).with(csrf())).andDo(print()).andExpect(status().isOk());
        verify(authorityLookupService, times(1)).findAllParentAuthorities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
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
}
