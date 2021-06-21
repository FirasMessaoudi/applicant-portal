/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.jwt;

import com.elm.shj.applicant.portal.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for {@link JwtTokenService}
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    private final static String TEST_USER_AUTHORITY = "DUMMY_USER_AUTHORITY";
    private final static long TEST_USER_NIN = 1234567897;
    private final static long TEST_USER_ID = 999;
    private final static long TEST_USER_ROLE_ID = 123;

    @InjectMocks
    private JwtTokenService classToTest;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    public void test_generate_token() {
        String generatedToken = classToTest.generateToken(TEST_USER_NIN, Arrays.asList(TEST_USER_AUTHORITY),
                TEST_USER_ID, false, Collections.singleton(TEST_USER_ROLE_ID), httpServletRequest);
        assertNotNull(generatedToken);
        Optional<List<String>> authoritiesFromToken = classToTest.retrieveAuthoritiesFromToken(generatedToken);
        assertTrue(authoritiesFromToken.isPresent());
        assertEquals(1, authoritiesFromToken.get().size());
        assertEquals(TEST_USER_AUTHORITY, authoritiesFromToken.get().get(0));
        Optional<Set<Long>> userRoleIdFromToken = classToTest.retrieveUserRoleIdsFromToken(generatedToken);
        assertTrue(userRoleIdFromToken.isPresent());
        assertEquals(TEST_USER_ROLE_ID, userRoleIdFromToken.get());
    }

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(classToTest, "secretKey", "dummy-test-secret-key");
        ReflectionTestUtils.setField(classToTest, "tokenExpiresIn", 10);
        ReflectionTestUtils.setField(classToTest, "tokenMobileExpiresIn", 20);
        ReflectionTestUtils.setField(classToTest, "tokenHeader", "dummy-token-header");

        ReflectionTestUtils.setField(classToTest, "userService", userService);
    }
}
