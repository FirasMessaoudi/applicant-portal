/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.registration;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.admin.ValidateApplicantCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @PostMapping
    public ResponseEntity<OtpToken> registerUser(@RequestBody UserDto user,
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

        String otp = otpService.createOtp(Long.toString(user.getNin()), user.getMobileNumber());
        log.debug("###################### OTP for [{}] : {} in Registration", user.getNin(), otp);
        String maskedMobileNumber = user.getMobileNumber() == null ? null : Integer.toString(user.getMobileNumber()).replaceAll("\\b\\d+(\\d{3})", "*******$1");
        String maskedEmail = user.getEmail() == null ? null : user.getEmail().replaceAll("\\b(\\w{2})[^@]+@(\\w{2})\\S+(\\.[^\\s.]+)", "$1***@$2****$3");
        // return the Otp Token
        //TODO:change getNin to getUin
        OtpToken token = new OtpToken(true, otpService.getOtpExpiryMinutes(), user.getUin(), user.getFullNameEn(), user.getFullNameAr(), maskedMobileNumber, maskedEmail);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/verify")
    public ResponseEntity<UserDto> verify(@RequestBody ValidateApplicantCmd command) throws JSONException, ParseException {

        Date formatedGregorianDate = new SimpleDateFormat("yyyy-MM-dd").parse(command.getDateOfBirthGregorian());
        Optional<UserDto> userInApplicantPortal = userService.findByUinAndDateOfBirth(Long.parseLong(command.getUin()), formatedGregorianDate);
        if (userInApplicantPortal.isPresent()) {
            return ResponseEntity.status(USER_ALREADY_REGISTERED_RESPONSE_CODE).body(null);
        }
        JSONObject commandJsonObject = new JSONObject();
        commandJsonObject.put("dateOfBirthGregorian", command.getDateOfBirthGregorian());
        commandJsonObject.put("uin", command.getUin());
        commandJsonObject.put("dateOfBirthHijri", command.getDateOfBirthHijri());
        UserDto userFromAdminPortal= userService.verifyUser(commandJsonObject);
        if (userFromAdminPortal==null){
            return  ResponseEntity.status(USER_NOT_FOUND_IN_ADMIN_PORTAL_RESPONSE_CODE).body(null);
        }
        return ResponseEntity.ok(userFromAdminPortal);

    }


    @PostMapping("/otp-for-registration")
    public ResponseEntity<UserDto> otpForRegistration(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user, String OTP, HttpServletRequest request) throws JSONException, ParseException {
        //validate OTP
        //TODO: cahnge nin to Uin
        if (!otpService.validateOtp(Long.toString(user.getUin()), OTP)) {
            return  ResponseEntity.status(INVALID_OTP_RESPONSE_CODE).body(null);
        }

         UserDto createdUser = userService.createUser(user, true);
        log.info("New user has been created with {} Uin number", createdUser.getUin());
        return ResponseEntity.ok(createdUser);
    }


    @PostMapping("/update-user-in-admin")
    public ResponseEntity<UserDto> updateUserInAdminPortal(@RequestBody UserDto user) throws JSONException, ParseException {
           return ResponseEntity.ok(userService.updateUserInAdminPortal(user));
    }



}
