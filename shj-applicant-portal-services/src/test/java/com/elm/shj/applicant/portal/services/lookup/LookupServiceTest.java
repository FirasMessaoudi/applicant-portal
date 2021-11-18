/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.lookup;

import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import com.elm.shj.applicant.portal.services.dto.LanguageLookupDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Testing class for service {@link LookupService}
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@ExtendWith(MockitoExtension.class)
public class LookupServiceTest {

    @InjectMocks
    private LookupService lookupService;

    @Mock
    private IntegrationService integrationService;

    @Mock
    private MapperRegistry mapperRegistry;

    @BeforeEach
    public void setUpToLoadLookups() {
        lookupService.loadLookups();
    }

    @Test
    void test_retrieve_supported_languages() {
        List<LanguageLookupDto> result = lookupService.retrieveSupportedLanguages();
        Assert.assertNotNull(result);

    }
}
