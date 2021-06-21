/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.otp;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Custom Authentication Provider to construct and OtpToken with a fresh generated pin
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private static final String RECAPTCHA_RESPONSE_PARAM_NAME = "grt";

    private final UserService userService;
    private final OtpService otpService;
    private final RecaptchaService recaptchaService;

    @Value("${login.failed.max.attempts}")
    private int allowedFailedLogins;

    @Value("${login.simultaneous.enabled}")
    private boolean simultaneousLoginEnabled;


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(noRollbackFor = {BadCredentialsException.class, RecaptchaException.class})
    public Authentication authenticate(final Authentication authentication) {
        log.debug("starting authentication process");
        long idNumber = Long.parseLong(authentication.getName());
        String password = (String) authentication.getCredentials();

        UserDto user = userService.findByNin(idNumber).orElseThrow(() ->
                // throw RecaptchaException to prevent DOS attack in case of idNumberStr is not exist
                new RecaptchaException("idNumber not found."));
        // check if user is active
        if (!user.isActivated()) {
            log.info("User {} is deactivated.", user.getNin());
            throw new BadCredentialsException("invalid credentials.");
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            userService.updateLoginTries(user);
            if (user.getNumberOfTries() >= allowedFailedLogins) {
                throw new RecaptchaException("Requires captcha.");
            }
            log.debug("wrong password.");
            throw new BadCredentialsException("invalid credentials.");
        }

        // Check captcha
        if (user.getNumberOfTries() > allowedFailedLogins) {
            String recaptchaResponse = request.getParameter(RECAPTCHA_RESPONSE_PARAM_NAME);
            if (StringUtils.isBlank(recaptchaResponse)) {
                throw new RecaptchaException("Invalid captcha.");
            }
            RecaptchaInfo recaptchaInfo;
            try {
                recaptchaInfo = recaptchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaResponse);
            } catch (IllegalArgumentException e) {
                throw new RecaptchaException("Invalid character in captcha response.");
            }
            if (recaptchaInfo == null || !recaptchaInfo.isSuccess()) {
                userService.updateLoginTries(user);
                throw new RecaptchaException("Invalid captcha.");
            }
        }

        // check if user is already logged in if simultaneous login flag is disabled
        if (!simultaneousLoginEnabled) {
            Date oldTokenExpiryDate = user.getTokenExpiryDate();
            if (oldTokenExpiryDate != null) {
                LocalDateTime oldTokenExpiryDateTime = oldTokenExpiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (oldTokenExpiryDateTime.isAfter(LocalDateTime.now())) {
                    // old token is still active, simultaneous login is not allowed
                    throw new BadCredentialsException("User is already logged in");
                }
            }
        }

        // generate OTP for the given principal
        String otp = otpService.createOtp(Long.toString(idNumber), user.getMobileNumber());
        log.debug("###################### OTP for [{}] : {}", idNumber, otp);

        String maskedMobileNumber = user.getMobileNumber() == null ? null : Integer.toString(user.getMobileNumber()).replaceAll("\\b\\d+(\\d{3})", "*******$1");
        String maskedEmail = user.getEmail() == null ? null : user.getEmail().replaceAll("\\b(\\w{2})[^@]+@(\\w{2})\\S+(\\.[^\\s.]+)", "$1***@$2****$3");

        // return the Otp Token
        return new OtpToken(true, otpService.getOtpExpiryMinutes(), authentication.getPrincipal(), user.getFirstName(), user.getFamilyName(), maskedMobileNumber, maskedEmail);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(OtpToken.class);
    }
}
