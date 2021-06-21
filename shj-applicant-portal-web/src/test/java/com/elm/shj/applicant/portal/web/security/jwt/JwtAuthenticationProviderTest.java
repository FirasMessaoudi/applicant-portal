/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.jwt;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testing class for {@link JwtAuthenticationProvider}
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationProviderTest {

    private final static String EXISTING_USER_PASS = "$2a$10$hfXRMc5G443S5dBO/daT8u7WHI9VDK3wtZjXmzhY2/.tU6LWJ1QsC";
    private final static String EXISTING_USER_NIN = "1234567897";
    private final static long EXISTING_USER_ID = 1;

    @InjectMocks
    private JwtAuthenticationProvider classToTest;

    @Mock
    private UserService userService;

    @Mock
    private PasswordHistoryService passwordHistoryService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void test_authenticate_wrong_username() {
        assertThrows(RecaptchaException.class, () -> {
            when(userService.findByNin(anyLong())).thenReturn(Optional.empty());
            classToTest.authenticate(new TestingAuthenticationToken("0000000000000", null));
        });
    }

    @Test
    public void test_authenticate_wrong_password() {
        assertThrows(RecaptchaException.class, () -> {
            when(userService.findByNin(anyLong())).thenReturn(Optional.empty());
            try(MockedStatic<BCrypt> mocked = mockStatic(BCrypt.class)) {
                mocked.when(() -> BCrypt.checkpw(anyString(), anyString())).thenReturn(Boolean.FALSE);
                classToTest.authenticate(new TestingAuthenticationToken("0000000000000", "fake"));
            }
        });
    }

    @Test
    public void test_authenticate_success() {
        UserDto userDto = new UserDto();
        userDto.setId(EXISTING_USER_ID);
        userDto.setPasswordHash(EXISTING_USER_PASS);
        RoleDto role = new RoleDto();
        RoleAuthorityDto roleAuthority = new RoleAuthorityDto();
        AuthorityLookupDto authority = new AuthorityLookupDto();
        roleAuthority.setAuthority(authority);
        authority.setCode(AuthorityConstants.ADD_USER);
        role.setRoleAuthorities(new HashSet<>(Collections.singletonList(roleAuthority)));
        UserRoleDto userRole = new UserRoleDto();
        userRole.setRole(role);
        userRole.setUser(userDto);
        userRole.setMainRole(true);
        userDto.setNin(Long.parseLong(EXISTING_USER_NIN));
        userDto.setPasswordHash(BCrypt.hashpw(EXISTING_USER_PASS, BCrypt.gensalt()));
        userDto.setActivated(true);
        userDto.setUserRoles(Collections.singleton(userRole));
        when(userService.findByNin(anyLong())).thenReturn(Optional.of(userDto));
        doAnswer((Answer<Void>) invocation -> {
            // do nothing
            return null;
        }).when(userService).updateUserLoginInfo(any(Long.class), any(Date.class));
        when(jwtTokenService.generateToken(anyLong(), any(), anyLong(), anyBoolean(), any(), any())).thenReturn("DUMMY-GENERATED-TOKEN");

        ServletRequestAttributes attributes = new ServletRequestAttributes(new MockHttpServletRequest());
        try(MockedStatic<RequestContextHolder> mockedStatic = mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::currentRequestAttributes).thenReturn(attributes);
            when(passwordHistoryService.findLastByUserId(EXISTING_USER_ID)).thenReturn(Optional.empty());
            Authentication authentication = classToTest.authenticate(new TestingAuthenticationToken(EXISTING_USER_NIN, EXISTING_USER_PASS));
            verify(userService, times(1)).updateUserLoginInfo(any(Long.class), any(Date.class));
            assertNotNull(authentication);
        }
    }

    @BeforeEach
    public void setUp() throws SecurityException, IllegalArgumentException {
        ReflectionTestUtils.setField(classToTest, "allowedFailedLogins", 3);
        ReflectionTestUtils.setField(classToTest, "userService", userService);
        ReflectionTestUtils.setField(classToTest, "passwordHistoryService", passwordHistoryService);
        ReflectionTestUtils.setField(classToTest, "jwtTokenService", jwtTokenService);
    }

}
