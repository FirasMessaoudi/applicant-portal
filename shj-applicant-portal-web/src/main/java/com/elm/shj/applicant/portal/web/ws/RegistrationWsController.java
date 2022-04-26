/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
import com.elm.shj.applicant.portal.services.dto.UpdateApplicantCmd;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.ValidateApplicantCmd;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.util.Optional;

/**
 * Controller for exposing Registration web services for external party.
 *
 * @author ahmad elsayed
 * @since 1.0.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_REGISTRATION)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RegistrationWsController {

    private final UserService userService;
    private final OtpService otpService;
    private final IntegrationService integrationService;


    @PostMapping("/verify")
    public ResponseEntity<WsResponse<?>> verify(@RequestBody ValidateApplicantCmd command) {
        Optional<UserDto> userInApplicantPortal = userService.findByUin(Long.parseLong(command.getUin()));
        if (userInApplicantPortal.isPresent()) {

            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.ALREADY_REGISTERED.getCode()).referenceNumber(command.getUin()).build()).build());

        }

        ApplicantLiteDto userFromAdminPortal = userService.verify(command);
        if (userFromAdminPortal == null) {

            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.NOT_FOUND_IN_ADMIN.getCode()).referenceNumber(command.getUin()).build()).build());

        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(userFromAdminPortal).build());

    }

    @PostMapping("/otp")
    public ResponseEntity<WsResponse<?>> otpRegistration(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user
            , HttpServletRequest request) {

        String mobileNumber = user.getCountryPhonePrefix() + user.getMobileNumber();
        String otp = otpService.createOtp(Long.toString(user.getUin()), mobileNumber);
        log.debug("###################### OTP for [{}] : {} in Registration", user.getUin(), otp);

        String maskedMobileNumber = user.getMobileNumber() == null ? null : mobileNumber.replaceAll("\\b\\d+(\\d{3})", "*******$1");
        String maskedEmail = user.getEmail() == null ? null : user.getEmail().replaceAll("\\b(\\w{2})[^@]+@(\\w{2})\\S+(\\.[^\\s.]+)", "$1***@$2****$3");
        // return the Otp Token
        OtpToken token = new OtpToken(true, otpService.getOtpExpiryMinutes(), user.getUin(), user.getFullNameEn(), user.getFullNameAr(), maskedMobileNumber, maskedEmail);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(token).build());

    }

    @PostMapping("/{needToUpdate}")
    public ResponseEntity<WsResponse<?>> register(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user, @PathVariable("needToUpdate") boolean needToUpdateInAdminPortal, @RequestParam String pin) throws JSONException {

        if (!otpService.validateOtp(String.valueOf(user.getUin()), pin)) {

            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.INVALID_OTP.getCode()).referenceNumber(user.getUin()+"").build()).build());
        }
        Optional<UserDto> userInApplicantPortal = userService.findByUin(user.getUin());
        if (userInApplicantPortal.isPresent()) {
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.ALREADY_REGISTERED.getCode()).build()).build());

        }
        if (needToUpdateInAdminPortal) {
            UpdateApplicantCmd applicantCmd = new UpdateApplicantCmd(String.valueOf(user.getUin()), user.getEmail(), user.getCountryPhonePrefix() + user.getMobileNumber(), user.getCountryCode(), user.getDateOfBirthHijri());

            ApplicantLiteDto returnedApplicant = userService.updateUserInAdminPortal(applicantCmd);
            if (returnedApplicant == null)
                return ResponseEntity.ok(
                        WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                                .body(WsError.builder().error(WsError.EWsError.APPLICANT_NOT_MATCHED.getCode()).referenceNumber(user.getUin() + "").build()).build());

        }else{
            userService.markAsRegistered(String.valueOf(user.getUin()));
        }


        UserDto createdUser = userService.createUser(user);
        log.info("New user has been created with {} Uin number", createdUser.getUin());
        // store the action in mobile audit log
        integrationService.storeSignupAction(createdUser.getUin());

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(createdUser).build());
    }


}
