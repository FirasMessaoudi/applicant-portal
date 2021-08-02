/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.otp;

import com.elm.dcc.foundation.providers.sms.service.SmsGatewayService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing class for service {@link UserService}
 *
 * @author Ahmed Elnazer
 * @since 1.0.0
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
class OtpServiceTest {

  private static final String TEST_UIN = "1234567899";
  private static final int TEST_MOBILE = 12345678;
  private static final String TEST_OTP = "1234";
  private static final String TEST_WRONG_OTP = "4321";
  @InjectMocks
  private OtpService serviceToTest;


  private LoadingCache<String, String> otpCache;

  @Mock
  private OtpGenerator otpGenerator;

  @Mock
  private SmsGatewayService smsService;

  @Mock
  private MessageSource messageSource;

  @BeforeEach
  void initService() throws Exception {
    Field otpCacheField = OtpService.class.getDeclaredField("otpCache");
    otpCacheField.setAccessible(true);

    otpCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
              @Override
              public String load(String s) {
                return StringUtils.EMPTY;
              }
            });
    otpCacheField.set(serviceToTest, otpCache);

  }


  @Test
  void test_create_otp() throws Exception {

    when(otpGenerator.generateOtp(TEST_UIN)).thenReturn("1234");
    when(messageSource.getMessage(any(), any(), any())).thenReturn("message sent");
    when(smsService.sendMessage(any(), any())).thenReturn(true);
    serviceToTest.createOtp(TEST_UIN, TEST_MOBILE);
    verify(otpGenerator).generateOtp(TEST_UIN);
    verify(messageSource).getMessage(any(), any(), any());
    verify(smsService).sendMessage(any(), any());
    assertEquals("1234", serviceToTest.createOtp(TEST_UIN, TEST_MOBILE));
  }

  @Test
  void test_validate_otp_success() throws Exception {
    //mark mock enabled as false
    Field mockEnabled = OtpService.class.getDeclaredField("mockEnabled");
    mockEnabled.setAccessible(true);
    mockEnabled.setBoolean(serviceToTest, false);

    otpCache.put(TEST_UIN, TEST_OTP);
    assertTrue(serviceToTest.validateOtp(TEST_UIN, TEST_OTP));

  }

  @Test
  void test_validate_otp_fail() throws Exception {
    //mark mock enabled as false
    Field mockEnabled = OtpService.class.getDeclaredField("mockEnabled");
    mockEnabled.setAccessible(true);
    mockEnabled.setBoolean(serviceToTest, false);
    otpCache.put(TEST_UIN, TEST_OTP);

    assertFalse(serviceToTest.validateOtp(TEST_UIN, TEST_WRONG_OTP));

  }

  @Test
  void test_validate_otp_success_mock_enabled() throws Exception {
    //mark mock enabled as true
    Field mockEnabled = OtpService.class.getDeclaredField("mockEnabled");
    mockEnabled.setAccessible(true);
    mockEnabled.setBoolean(serviceToTest, true);
    assertTrue(serviceToTest.validateOtp(TEST_UIN, TEST_OTP));

  }

}
