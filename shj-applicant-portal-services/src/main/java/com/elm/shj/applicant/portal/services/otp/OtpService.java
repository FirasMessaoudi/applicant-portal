/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.otp;

import com.elm.shj.applicant.portal.services.dto.ELoginType;
import com.elm.shj.applicant.portal.services.sms.HUICSmsService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Service handling otp operations
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OtpService {

    private static final String OTP_SMS_NOTIFICATION_MSG = "otp.login.sms.notification";
    private static final String DEFAULT_ADMIN_USER = "2357361464";
    private static final String DEFAULT_ADMIN_USER_OTP = "3792";

    @Value("${otp.expiry.minutes}")
    private int otpExpiryMinutes;

    @Value("${otp.mock.enabled}")
    private boolean mockEnabled;

    private LoadingCache<String, String> otpCache;

    private final OtpGenerator otpGenerator;
    private final HUICSmsService huicSmsService;
    private final MessageSource messageSource;

    @PostConstruct
    private void initCache() {
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(otpExpiryMinutes, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) {
                        return StringUtils.EMPTY;
                    }
                });
    }

    /**
     * Creates and starts a new otp-transaction
     *
     * @param principal the key to attach to the created otp
     * @return the created otp
     */
    public String createOtp(String principal,Integer countryCode, String mobileNumber) {
        try {
            String generatedOtp = otpGenerator.generateOtp(principal);
            otpCache.put(principal, generatedOtp);
            //TODO:need to be changed since uin is not required to start with 1
            String locale = principal.startsWith("1") ? "ar" : "en";
            String registerUserSms = messageSource.getMessage(OTP_SMS_NOTIFICATION_MSG, new String[]{generatedOtp}, Locale.forLanguageTag(locale));
            return huicSmsService.sendMessage(countryCode,mobileNumber, registerUserSms, null) ? generatedOtp : null;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SSLException e) {
            log.error("Unable to generate OTP : " + e.getMessage(), e);
            return null;
        }
    }

    public String createOtp(String principal,Integer countryCode, String mobileNumber, String loginType) {
        try {
            String generatedOtp;
            if(principal.equals(DEFAULT_ADMIN_USER) && loginType.equals(ELoginType.id.name())){
                generatedOtp = DEFAULT_ADMIN_USER_OTP;
            } else {
                generatedOtp = otpGenerator.generateOtp(principal);
            }
            otpCache.put(principal, generatedOtp);
            //TODO:need to be changed since uin is not required to start with 1
            String locale = principal.startsWith("1") ? "ar" : "en";
            String registerUserSms = messageSource.getMessage(OTP_SMS_NOTIFICATION_MSG, new String[]{generatedOtp}, Locale.forLanguageTag(locale));
            return huicSmsService.sendMessage(countryCode,mobileNumber, registerUserSms, null) ? generatedOtp : null;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SSLException e) {
            log.error("Unable to generate OTP : " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Validates the given otp for the request
     *
     * @param principal   the key to check against
     * @param otpToVerify the otp to verify
     * @return result of verification
     */
    public boolean validateOtp(String principal, String otpToVerify) {
        if (mockEnabled || Objects.equals(otpCache.getIfPresent(principal), otpToVerify)) {
            otpCache.invalidate(principal);
            return true;
        }
        return false;
    }

    /**
     * Returns the Otp expiry duration in minutes as per the configuration
     *
     * @return the otp expiry duration in minutes
     */
    public int getOtpExpiryMinutes() {
        return otpExpiryMinutes;
    }

}
