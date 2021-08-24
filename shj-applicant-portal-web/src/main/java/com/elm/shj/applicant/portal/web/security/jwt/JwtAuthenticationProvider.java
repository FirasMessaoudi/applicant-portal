/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.jwt;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * JWT based Authentication provider used for Spring Security authentication process
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final OtpService otpService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final PasswordHistoryService passwordHistoryService;

    @Value("${dcc.validation.password.expires.in.months}")
    private int passwordAgeInMonths;

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (!otpService.validateOtp(authentication.getName(), (String)authentication.getCredentials())) {
            throw new BadCredentialsException("Invalid OTP");
        }

        boolean isFromWebService = false;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String callerType = request.getHeader(JwtTokenService.CALLER_TYPE_HEADER_NAME);
        if (callerType != null && callerType.equals(JwtTokenService.WEB_SERVICE_CALLER_TYPE)) {
            isFromWebService = true;
        }


        long idNumber = Long.parseLong(authentication.getName());

        Optional<UserDto> userDtoOptional = userService.findByUin(idNumber);

        if (!userDtoOptional.isPresent()) {
            if (isFromWebService) {
                throw new UserNotFoundException(String.valueOf(idNumber));
            } else {
                // throw RecaptchaException to prevent DOS attack in case of idNumberStr is not exist
                new RecaptchaException("idNumber not found.");
            }
        }

        UserDto user = userDtoOptional.get();

        // check if the password expired
        boolean passwordExpiredFlag = true;
        //each user should have a password history, for newly registered user, change password is required in the first login
        Optional<UserPasswordHistoryDto> userPasswordHistory = passwordHistoryService.findLastByUserId(user.getId());
        if (userPasswordHistory.isPresent()) {
            //check the date compared with configured password age
            LocalDate passwordCreationDate = userPasswordHistory.get().getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            passwordExpiredFlag = passwordCreationDate.plusMonths(passwordAgeInMonths).isBefore(LocalDate.now()) || user.isChangePasswordRequired();
        }

        List<String> grantedAuthorities = new ArrayList<>();
        Set<Long> userRoleIds = new HashSet<>();
        user.getUserRoles().forEach(userRoleDto -> {
            userRoleIds.add(userRoleDto.getId());
            userRoleDto.getRole().getRoleAuthorities().forEach(roleAuthorityDto -> {
                grantedAuthorities.add(roleAuthorityDto.getAuthority().getCode());
            });
        });
        // generate the token
        String token = jwtTokenService.generateToken(idNumber, grantedAuthorities, user.getId(), passwordExpiredFlag, userRoleIds, request);
        log.debug("generated token for {} is {}", idNumber, token);
        // save user login info
        userService.updateUserLoginInfo(user.getId(), jwtTokenService.retrieveExpirationDateFromToken(token).orElse(new Date()));

        return new JwtToken(token, authentication, AuthorityUtils.createAuthorityList(grantedAuthorities.toArray(new String[]{})), passwordExpiredFlag, user.getFullNameEn(), user.getFullNameAr(), user.getId(), user.getUserRoles());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> aClass) {
        return JwtToken.class.isAssignableFrom(aClass);
    }

}
