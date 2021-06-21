/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.registration;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}
