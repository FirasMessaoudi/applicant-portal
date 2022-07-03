/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.registration;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.util.Optional;

/**
 * Main controller for registration page
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_REGISTRATION)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RegistrationController {

    public static final String RECAPTCHA_TOKEN_NAME = "grt";
    private final RecaptchaService recaptchaService;
    private final UserService userService;
    private final OtpService otpService;
    private static final int USER_ALREADY_REGISTERED_RESPONSE_CODE = 560;
    private static final int USER_NOT_FOUND_IN_ADMIN_PORTAL_RESPONSE_CODE = 561;
    private static final int INVALID_OTP_RESPONSE_CODE = 562;
    private static final int UPDATE_FAILED_IN_ADMIN_PORTAL = 563;
    private static final int INVALID_DATE_OF_BIRTH = 564;
    private static final int INVALID_DATE_OF_BIRTH_COMMAND = 139;


    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user, @RequestParam String pin) {

        if (!otpService.validateOtp(String.valueOf(user.getUin()), pin)) {
            return ResponseEntity.status(INVALID_OTP_RESPONSE_CODE).body(null);
        }
        Optional<UserDto> userInApplicantPortal = userService.findByUin(user.getUin());
        if (userInApplicantPortal.isPresent()) {
            return ResponseEntity.status(USER_ALREADY_REGISTERED_RESPONSE_CODE).body(null);
        }
        UpdateApplicantCmd applicantCmd = new UpdateApplicantCmd(String.valueOf(user.getUin()), user.getEmail(), user.getCountryPhonePrefix() + user.getMobileNumber(), user.getCountryCode(), user.getDateOfBirthGregorian(), user.getDateOfBirthHijri(), EChannel.WEB.name());

        ApplicantLiteDto returnedApplicant = userService.updateUserInAdminPortal(applicantCmd);
        if (returnedApplicant == null) {
            log.debug("Update failed in admin portal for applicant with Uin {}", user.getUin());
            return ResponseEntity.status(UPDATE_FAILED_IN_ADMIN_PORTAL).build();
        }
        user.setNationalityCode(returnedApplicant.getNationalityCode());
        UserDto createdUser = userService.createUser(user);
        log.info("New user has been created with {} Uin number", createdUser.getUin());
        return ResponseEntity.ok(createdUser);
    }


    @PostMapping("/verify")
    public ResponseEntity<ApplicantLiteDto> verify(@RequestBody ValidateApplicantCmd command) {
        Optional<UserDto> userInApplicantPortal = Optional.empty();
        if (command.getType().equals("uin"))
            userInApplicantPortal = userService.findByUin(Long.parseLong(command.getIdentifier()));
        if (command.getType().equals("passport"))
            userInApplicantPortal = userService.findByPassportNumber(command.getIdentifier(), command.getNationalityCode());
        if (command.getType().equals("id"))
            userInApplicantPortal = userService.findByIdNumber(command.getIdentifier());
        if (userInApplicantPortal.isPresent()) {
            return ResponseEntity.status(USER_ALREADY_REGISTERED_RESPONSE_CODE).body(null);
        }
        ApplicantLiteDto userFromAdminPortal = userService.verify(command);
        if (userFromAdminPortal == null) {
            return ResponseEntity.status(USER_NOT_FOUND_IN_ADMIN_PORTAL_RESPONSE_CODE).body(null);
        } else if ( userFromAdminPortal.getApplicantVerifyStatus() != null && userFromAdminPortal.getApplicantVerifyStatus() == INVALID_DATE_OF_BIRTH_COMMAND) {
            return ResponseEntity.status(INVALID_DATE_OF_BIRTH).body(null);
        } else {
            return ResponseEntity.ok(userFromAdminPortal);
        }
    }


    @PostMapping("/otp")
    public ResponseEntity<OtpToken> otpRegistration(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user,
                                                    @RequestParam(RECAPTCHA_TOKEN_NAME) String reCaptchaToken, HttpServletRequest request) {
        // check recaptcha
        RecaptchaInfo recaptchaInfo;
        if (StringUtils.isBlank(reCaptchaToken)) {
            log.info("recaptcha response is not provided in the request...");
            return null;
        }
        try {
            recaptchaInfo = recaptchaService.verifyRecaptcha(request.getRemoteAddr(),
                    reCaptchaToken, true);
        } catch (RecaptchaException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        if (recaptchaInfo == null || !recaptchaInfo.isSuccess()) {
            log.info("Captcha validation was not successful!");
            return null;
        }
        String otp = otpService.createOtp(Long.toString(user.getUin()),Integer.valueOf(user.getCountryPhonePrefix()), user.getMobileNumber());
        log.debug("###################### OTP for [{}] : {} in Registration", user.getUin(), otp);
        String maskedMobileNumber = user.getMobileNumber() == null ? null : user.getMobileNumber().replaceAll("\\b\\d+(\\d{3})", "*******$1");
        String maskedEmail = user.getEmail() == null ? null : user.getEmail().replaceAll("\\b(\\w{2})[^@]+@(\\w{2})\\S+(\\.[^\\s.]+)", "$1***@$2****$3");
        // return the Otp Token
        OtpToken token = new OtpToken(true, otpService.getOtpExpiryMinutes(), user.getUin(), user.getFullNameEn(), user.getFullNameAr(), maskedMobileNumber, maskedEmail);

        return ResponseEntity.ok(token);
    }


}
