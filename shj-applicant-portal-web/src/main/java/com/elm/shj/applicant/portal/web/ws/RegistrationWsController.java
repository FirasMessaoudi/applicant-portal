/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.*;
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
    private static final int INVALID_DATE_OF_BIRTH = 564;
    private static final int INVALID_DATE_OF_BIRTH_COMMAND = 139;

    @PostMapping("/verify")
    public ResponseEntity<WsResponse<?>> verify(@RequestBody ValidateApplicantCmd command) {
        log.info(command.getType());
        Optional<UserDto> userInApplicantPortal = Optional.empty();
        if (command.getType().equals(ELoginType.uin.name()))
            userInApplicantPortal = userService.findByUin(Long.parseLong(command.getIdentifier()));
        if (command.getType().equals(ELoginType.passport.name()))
            userInApplicantPortal = userService.findByPassportNumber(command.getIdentifier(), command.getNationalityCode());
        if (command.getType().equals(ELoginType.id.name()))
            userInApplicantPortal = userService.findByIdNumber(command.getIdentifier());

        if (userInApplicantPortal.isPresent()) {

            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.ALREADY_REGISTERED.getCode()).referenceNumber(command.getIdentifier()).build()).build());

        }

        ApplicantLiteDto userFromAdminPortal = userService.verify(command);
        if (userFromAdminPortal == null) {

            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.NOT_FOUND_IN_ADMIN.getCode()).referenceNumber(command.getIdentifier()).build()).build());

        } else if ( userFromAdminPortal.getApplicantVerifyStatus() != null && userFromAdminPortal.getApplicantVerifyStatus() == INVALID_DATE_OF_BIRTH_COMMAND) {
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(INVALID_DATE_OF_BIRTH).referenceNumber(command.getIdentifier()).build()).build());
        } else {
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(userFromAdminPortal).build());
        }

    }

    @PostMapping("/otp")
    public ResponseEntity<WsResponse<?>> otpRegistration(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user
            , HttpServletRequest request) {

        String mobileNumber = user.getCountryPhonePrefix() + user.getMobileNumber();
        String otp = otpService.createOtp(Long.toString(user.getUin()),Integer.valueOf(user.getCountryPhonePrefix()), user.getMobileNumber());
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
        log.debug("registerAccount Started ::: with user uin: {}", user.getUin());
        if (!otpService.validateOtp(String.valueOf(user.getUin()), pin)) {
            log.debug("registerAccount Finished ::: invalid otp with user uin: {}", user.getUin());
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.INVALID_OTP.getCode()).referenceNumber(user.getUin()+"").build()).build());
        }
        Optional<UserDto> userInApplicantPortal = userService.findByUinWithoutDeleted(user.getUin());
        if (userInApplicantPortal.isPresent() && userInApplicantPortal.get().isDeleted()==false) {
            log.debug("registerAccount Finished ::: ALREADY_REGISTERED with user uin: {}", user.getUin());
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.ALREADY_REGISTERED.getCode()).build()).build());

        }
        if (needToUpdateInAdminPortal) {

            UpdateApplicantCmd applicantCmd = new UpdateApplicantCmd(String.valueOf(user.getUin()), user.getEmail(), user.getCountryPhonePrefix() + user.getMobileNumber(), user.getCountryCode(), user.getDateOfBirthGregorian(), user.getDateOfBirthHijri(), EChannel.MOBILE.name());

            ApplicantLiteDto returnedApplicant = userService.updateUserInAdminPortal(applicantCmd);
            if (returnedApplicant == null) {
                log.debug("registerAccount Finished ::: APPLICANT_NOT_MATCHED with user uin: {}", user.getUin());
                return ResponseEntity.ok(
                        WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                                .body(WsError.builder().error(WsError.EWsError.APPLICANT_NOT_MATCHED.getCode()).referenceNumber(user.getUin() + "").build()).build());
            }
        }else{
            userService.markAsRegistered(String.valueOf(user.getUin()), EChannel.MOBILE.name());
        }

        if (userInApplicantPortal.isPresent() && userInApplicantPortal.get().isDeleted()==true){
            Long uin = userInApplicantPortal.get().getUin();
          int affectedRows=  userService.markAccountAsDeleted(uin, false);
            if (affectedRows == 1) {
                log.debug("registerAccount Finished ::: SUCCESS only update delete flag with user uin: {}", uin);
                userInApplicantPortal.get().setDeleted(false);
                return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(userInApplicantPortal.get()).build());
            } else {
                log.debug("registerAccount Finished ::: FAILURE only update delete flag with user uin: {}", uin);
                return ResponseEntity.ok(WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode()).body(WsError.builder().error(WsError.EWsError.INVALID_INPUT.getCode()).build()).build());
            }

        }else {
            UserDto createdUser = userService.createUser(user);
            log.info("New user has been created with {} Uin number", createdUser.getUin());
            // store the action in mobile audit log
            integrationService.storeSignupAction(createdUser.getUin());
            log.debug("registerAccount Finished ::: SUCCESS with user uin: {}", user.getUin());
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(createdUser).build());
        }
    }


}
