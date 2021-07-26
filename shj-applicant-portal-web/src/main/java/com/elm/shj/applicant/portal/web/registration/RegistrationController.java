/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.registration;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
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
    @Value("${admin.portal.url}")
    private String adminPortalUrl;
    private static final int USER_ALREADY_REGISTERED_RESPONSE_CODE = 560;
    private static final int USER_NOT_FOUND_IN_ADMIN_PORTAL_RESPONSE_CODE = 561;
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
        OtpToken token = new OtpToken(true, otpService.getOtpExpiryMinutes(), user.getNin(), user.getFullNameEn(), user.getFullNameAr(), maskedMobileNumber, maskedEmail);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/verify")
    public ResponseEntity<UserDto> verify(@RequestBody ValidateApplicantCmd command) throws JSONException, ParseException {

        Date formatedGregorianDate = new SimpleDateFormat("yyyy-MM-dd").parse(command.getDateOfBirthGregorian());
        Optional<UserDto> foundUser = userService.findByUinAndDateOfBirth(Long.parseLong(command.getUin()), formatedGregorianDate);
        if (foundUser.isPresent()) {
            return ResponseEntity.status(USER_ALREADY_REGISTERED_RESPONSE_CODE).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("CALLER-TYPE", "WEB-SERVICE");
        final String url = adminPortalUrl + "/applicants/verify";
        JSONObject commandJsonObject = new JSONObject();
        commandJsonObject.put("dateOfBirthGregorian", command.getDateOfBirthGregorian());
        commandJsonObject.put("uin", command.getUin());
        commandJsonObject.put("dateOfBirthHijri", command.getDateOfBirthHijri());
        ApplicantLiteDto returnedApplicant = callAdminPortal(headers, url, commandJsonObject.toString());

        if (returnedApplicant==null){
           return ResponseEntity.status(USER_NOT_FOUND_IN_ADMIN_PORTAL_RESPONSE_CODE).body(null);

        }

        UserDto constructedUser = constructUserFromApplicant(returnedApplicant);
        constructedUser.setUin(Long.parseLong(command.getUin()));

        return ResponseEntity.ok(constructedUser);

    }


    @PostMapping("/otp-for-registration")
    public ResponseEntity<UserDto> otpForRegistration(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user, String OTP, HttpServletRequest request) {

        //validate OTP
        //TODO: cahnge nin to Uin
        if (!otpService.validateOtp(Long.toString(user.getNin()), OTP)) {
            throw new BadCredentialsException("Invalid OTP");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("CALLER-TYPE", "WEB-SERVICE");
//        headers.setContentType(MediaType.APPLICATION_JSON);
        final String url = adminPortalUrl + "/" + user.getUin() + "/update";

//        callAdminPortal(headers, url, user.toString());

        UserDto createdUser = userService.createUser(user, true);

        // otherwise, create the user
        log.info("New user has been created with {} id number", createdUser.getNin());
        return ResponseEntity.ok(createdUser);
    }


    private ApplicantLiteDto callAdminPortal(HttpHeaders headers, String url, String body) {
        HttpEntity<String> request =
                new HttpEntity<String>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ApplicantLiteDto response = restTemplate.postForObject(url, request,
                ApplicantLiteDto.class);

        return response;
    }


    private UserDto constructUserFromApplicant(ApplicantLiteDto applicant) {

        UserDto user = new UserDto();
        user.setFullNameEn(applicant.getFullNameEn());
        user.setFullNameAr(applicant.getFullNameAr());
        user.setEmail(applicant.getEmail());
        user.setDateOfBirthGregorian(applicant.getDateOfBirthGregorian());
        user.setDateOfBirthHijri(applicant.getDateOfBirthHijri().intValue());
        user.setMobileNumber(Integer.parseInt(applicant.getLocalMobileNumber()));

        return user;
    }


}
