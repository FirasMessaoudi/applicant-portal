/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.registration;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.shj.applicant.portal.services.dto.RegistrationTokenDto;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.user.RegistrationTokenService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.admin.ValidateApplicantCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

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
    private final RegistrationTokenService registrationTokenService;
     @PostMapping
    public void registerUser(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user,
                             @RequestParam(RECAPTCHA_TOKEN_NAME) String reCaptchaToken, HttpServletRequest request) {
        log.info("Handler for {}", "registerUser");

        // check recaptcha
        RecaptchaInfo recaptchaInfo;
        if (StringUtils.isBlank(reCaptchaToken)) {
            log.info("recaptcha response is not provided in the request...");
            return;
        }
        try {
            recaptchaInfo = recaptchaService.verifyRecaptcha(request.getRemoteAddr(),
                    reCaptchaToken, true);
        } catch (RecaptchaException e) {
            log.error(e.getMessage(), e);
            return;
        }
        if (recaptchaInfo == null || !recaptchaInfo.isSuccess()) {
            log.info("Captcha validation was not successful!");
            return;
        }

        // otherwise, create the user
        UserDto createdUser = userService.createUser(user, true);
        log.info("New user has been created with {} id number", createdUser.getNin());
    }

    @PostMapping("/verify")
    public ResponseEntity<UserDto> verify(@RequestBody ValidateApplicantCmd command) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final String url = "http://localhost:8089/applicant/verify";
        HttpEntity<String> request =
                new HttpEntity<String>(command.toString(), headers);
        RestTemplate restTemplate =new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request,
                String.class);
        System.out.println(response.getBody());

        UserDto user = new UserDto();
        user.setEmail("ahmed.nazer@gmail.com");
        user.setMobileNumber(555359286);
        user.setNin(1234567897l);
        user.setFirstName("ahmed");
        user.setFatherName("mohammed");
        user.setGrandFatherName("ahmed");
        user.setFamilyName("ali");
        user.setGender("M");
        RegistrationTokenDto createdRegistrationTokenForUser = registrationTokenService.createRegistrationTokenForUser(user);
        return ResponseEntity.ok(user);
    }


}
