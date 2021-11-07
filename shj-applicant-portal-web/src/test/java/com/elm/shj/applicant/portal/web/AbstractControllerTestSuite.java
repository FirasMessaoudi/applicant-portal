/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web;

import com.elm.dcc.foundation.commons.validation.UniqueValidator;
import com.elm.dcc.foundation.providers.filescan.service.FileScanService;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.dcc.foundation.providers.sms.service.SmsGatewayService;
import com.elm.shj.applicant.portal.services.audit.AuditLogService;
import com.elm.shj.applicant.portal.services.dashboard.DashboardService;
import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.lookup.AuthorityLookupService;
import com.elm.shj.applicant.portal.services.lookup.LookupService;
import com.elm.shj.applicant.portal.services.otp.OtpGenerator;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.role.RoleService;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserLocationService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.boot.BootApplication;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Abstract class for all controller tests
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = BootApplication.class)
@WebMvcTest
@ActiveProfiles("test")
public abstract class AbstractControllerTestSuite {

    protected static final String TEST_USER_PASSWORD = "test-password";
    protected static final String TEST_USER_NIN = "1234567897";
    protected static final long NEW_USER_ID = 1001L;

    protected final Random random = new Random();

    @MockBean
    protected EntityManagerFactory emf;

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectWriter jsonObjectWriter;

    protected ObjectMapper mapper = new ObjectMapper();

    @MockBean
    protected UserService userService;

    @MockBean
    protected PlatformTransactionManager transactionManager;

    @MockBean
    protected AuthorityLookupService authorityLookupService;

    @MockBean
    protected RoleService roleService;

    @MockBean
    protected DashboardService dashboardService;

    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;

    @MockBean
    protected PasswordHistoryService passwordHistoryService;

    @Autowired
    protected JwtTokenService jwtTokenService;

    @MockBean
    protected FileScanService fileScanService;

    @MockBean
    protected RecaptchaService recaptchaService;
    @MockBean
    protected OtpGenerator otpGenerator;
    @MockBean
    protected SmsGatewayService smsGatewayService;
    @MockBean
    protected OtpService otpService;
    @MockBean
    protected AuditLogService auditLogService;

    @MockBean
    protected LookupService lookupService;

    @MockBean
    protected IntegrationService integrationService;

    @MockBean
    protected UserLocationService userLocationService;

    protected Cookie tokenCookie;

    protected UserDto loggedInUser;

    /**
     * Method which is executed before each test
     */
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void beforeEach() throws Exception {

        // skip heavy validation
        System.setProperty(UniqueValidator.UNIQUE_VALIDATION_SKIP, "true");
        System.setProperty(RecaptchaService.RECAPTCHA_VALIDATION_SKIP, "true");

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        jsonObjectWriter = mapper.writer().withDefaultPrettyPrinter();

        setUp();
    }

    protected String objectToJson(Object o) {
        try {
            return jsonObjectWriter.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    protected <T> T jsonToObject(String s, Class<T> clazz) {
        try {
            return mapper.readerFor(clazz).readValue(s);
        } catch (IOException e) {
            return null;
        }
    }

    protected void triggerLogin() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("idNumber", TEST_USER_NIN);
        credentials.put("password", TEST_USER_PASSWORD);

        Map<String, String> otpCredentials = new HashMap<>();
        otpCredentials.put("idNumber", TEST_USER_NIN);
        otpCredentials.put("otp", "1111");

        mockMvc.perform(post(Navigation.API_AUTH + "/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectToJson(credentials)).with(csrf())).andExpect(status().isOk()).andDo(result -> {

            mockMvc.perform(post(Navigation.API_AUTH + "/otp").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectToJson(otpCredentials)).with(csrf())).andExpect(status().isOk()).andDo(result2 -> {
                Cookie tokenCookie = result2.getResponse().getCookie(JwtTokenService.TOKEN_COOKIE_NAME);
                assertNotNull(tokenCookie);
                this.tokenCookie = tokenCookie;
            });

        });
    }

    protected void mockSuccessfulLogin() {
        loggedInUser = new UserDto();

        RoleDto role = new RoleDto();
        RoleAuthorityDto roleAuthority = new RoleAuthorityDto();
        AuthorityLookupDto authority = new AuthorityLookupDto();
        roleAuthority.setAuthority(authority);
        authority.setCode(AuthorityConstants.USER_MANAGEMENT);
        role.setRoleAuthorities(new HashSet<>(Collections.singletonList(roleAuthority)));
        UserRoleDto userRole = new UserRoleDto();
        userRole.setUser(loggedInUser);
        userRole.setRole(role);
        userRole.setMainRole(true);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        loggedInUser.setPasswordHash(encoder.encode(TEST_USER_PASSWORD));
        loggedInUser.setPassword(TEST_USER_PASSWORD);
        loggedInUser.setNin(new Long(TEST_USER_NIN));
        loggedInUser.setUserRoles(Collections.singleton(userRole));
        loggedInUser.setActivated(true);
        Mockito.when(userService.findByUin(new Long(TEST_USER_NIN))).thenReturn(Optional.of(loggedInUser));
        Mockito.when(userService.hasToken(new Long(TEST_USER_NIN))).thenReturn(true);

        UserPasswordHistoryDto userPasswordHistoryDto = new UserPasswordHistoryDto();
        userPasswordHistoryDto.setCreationDate(Date.from(LocalDateTime.now(UTC).minusSeconds(10).toInstant(UTC)));
        Mockito.when(passwordHistoryService.findLastByUserId(anyLong())).thenReturn(Optional.of(userPasswordHistoryDto));

        Mockito.when(otpService.validateOtp(any(), any())).thenReturn(true);

        doAnswer((Answer<Void>) invocation -> {
            // do nothing
            return null;
        }).when(userService).updateUserLoginInfo(any(Long.class), any(Date.class));
    }

    /**
     * Method which is executed after each test
     */
    @AfterEach
    public void afterEach() {
        tearDown();
    }

    /**
     * Additional set up for the test
     * <p>
     * To be defined in each test
     * </p>
     */
    public abstract void setUp() throws Exception;

    /**
     * Additional closure for the test
     * <p>
     * To be defined in each test
     * </p>
     */
    public abstract void tearDown();
}
