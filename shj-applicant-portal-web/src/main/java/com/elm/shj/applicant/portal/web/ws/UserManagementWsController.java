/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserLocationService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.admin.ChangePasswordCmd;
import com.elm.shj.applicant.portal.web.admin.ResetPasswordCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for exposing user Management web services for external party.
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
@RequestMapping(Navigation.API_INTEGRATION_USERS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserManagementWsController {

    public static final String CONFIDENTIAL = "<CONFIDENTIAL>";
    public static final String RESET_PASSWORD_SUCCESS_MSG = "Reset password successfully";
    public static final String CHANGE_PASSWORD_SUCCESS_MSG = "Change password successfully";

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordHistoryService passwordHistoryService;
    private final JwtTokenService jwtTokenService;
    private final OtpService otpService;
    private final UserLocationService userLocationService;

    /**
     * Resets the user password
     *
     * @return the found user or <code>null</code>
     */
    @PostMapping("/reset-password")
    @RolesAllowed(AuthorityConstants.RESET_PASSWORD)
    public ResponseEntity<WsResponse<?>> resetUserPassword(@RequestBody @Valid ResetPasswordCmd command) {

        Optional<UserDto> userDtoOptional = userService.findByUin(command.getIdNumber());

        if (!userDtoOptional.isPresent()) {
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.APPLICANT_NOT_MATCHED.getCode()).referenceNumber(command.getIdNumber() + "").build()).build());
        }

        UserDto user = userDtoOptional.get();

        boolean dateOfBirthMatched;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // decide which date of birth to use
        if (command.getDateOfBirthGregorian() != null) {
            String userDateFormatted = sdf.format(user.getDateOfBirthGregorian());
            String commandDataOfBirthFormatted = sdf.format(command.getDateOfBirthGregorian());
            dateOfBirthMatched = commandDataOfBirthFormatted.equals(userDateFormatted);
        } else {
            dateOfBirthMatched = command.getDateOfBirthHijri() == user.getDateOfBirthHijri();
        }
        if (dateOfBirthMatched) {
            userService.resetPassword(user);
        } else {
            log.debug("invalid data for username {}", command.getIdNumber());
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.APPLICANT_NOT_MATCHED.getCode()).referenceNumber(command.getIdNumber() + "").build()).build());
        }

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(RESET_PASSWORD_SUCCESS_MSG).build());
    }


    /**
     * Change the user password
     *
     * @return the found user or <code>null</code>
     */
    @PostMapping("/change-password")
    public ResponseEntity<WsResponse<?>> changeUserPassword(@RequestBody @Valid ChangePasswordCmd command) throws MethodArgumentNotValidException, NoSuchMethodException {
        log.debug("WS Handler for {}", "Change User Password");

        JwtToken loggedInUser = (JwtToken) SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserIdNumberStr = ((User) loggedInUser.getPrincipal()).getUsername();
        long loggedInUserIdNumber = Long.parseLong(loggedInUserIdNumberStr);

        String oldPasswordHash = userService.retrievePasswordHash(loggedInUserIdNumber);

        if (!BCrypt.checkpw(command.getOldPassword(), oldPasswordHash)) {
            throw new BadCredentialsException(String.valueOf(loggedInUserIdNumber));
        }
        // current password cannot be used as new password
        if (command.getNewPassword().equals(command.getOldPassword())) {
            return generateFailResponse(WsError.EWsError.PWRD_HISTORY_ERROR, String.valueOf(loggedInUserIdNumber));
        }
        // password should not contain the user's username which is id number
        if (command.getNewPassword().contains(loggedInUserIdNumberStr)) {
            return generateFailResponse(WsError.EWsError.PWRD_CONTAINS_USERNAME_ERROR, String.valueOf(loggedInUserIdNumber));
        }

        //retrieve old passwords and compare the new password with the returned list
        Optional<UserPasswordHistoryDto> matchedOldPassword = Optional.empty();
        Optional<Long> userIdFromToken = jwtTokenService.retrieveUserIdFromToken(loggedInUser.getToken());
        if (userIdFromToken.isPresent()) {
            matchedOldPassword = passwordHistoryService.findByUserId(userIdFromToken.get()).stream().filter(passHistory -> BCrypt.checkpw(command.getNewPassword(), passHistory.getOldPasswordHash())).findFirst();
        }

        if (matchedOldPassword.isPresent()) {
            return generateFailResponse(WsError.EWsError.PWRD_HISTORY_ERROR, String.valueOf(loggedInUserIdNumber));
        }
        //no matching, update the password
        userService.updateUserPassword(loggedInUserIdNumber, passwordEncoder.encode(command.getNewPassword()));
        userService.clearToken(loggedInUserIdNumber);
        //keep password history
        userIdFromToken.ifPresent(aLong -> passwordHistoryService.addUserPasswordHistory(aLong, oldPasswordHash));

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(CHANGE_PASSWORD_SUCCESS_MSG).build());

    }

    /**
     * get user main data by uin and applicantPackageId
     */
    @GetMapping("/main-data/{applicantPackageId}")
    public ResponseEntity<WsResponse<?>> findUserMainDataByUin(@PathVariable long applicantPackageId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        Optional<ApplicantMainDataDto> applicantMainDataDto = userService.findUserMainDataByUin(loggedInUserUin, applicantPackageId);
        if (!applicantMainDataDto.isPresent()) {
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, loggedInUserUin);
        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(applicantMainDataDto.get()).build());
    }

    private ResponseEntity<WsResponse<?>> generateFailResponse(WsError.EWsError errorCode, String reference) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(errorCode.getCode()).referenceNumber(reference).build()).build());
    }

    @PostMapping("/otp")
    public ResponseEntity<WsResponse<?>> otpRegistration(@RequestBody @Validated UpdateContactsCmd userContacts, Authentication authentication, HttpServletRequest request) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();

        String mobileNumber = userContacts.getCountryPhonePrefix() + userContacts.getMobileNumber();
        String otp = otpService.createOtp(loggedInUserUin, mobileNumber);
        log.debug("###################### OTP for [{}] : {} in edit contacts", loggedInUserUin, otp);
        String maskedMobileNumber = mobileNumber == null ? null : mobileNumber.replaceAll("\\b\\d+(\\d{3})", "*******$1");
        String maskedEmail = userContacts.getEmail() == null ? null : userContacts.getEmail().replaceAll("\\b(\\w{2})[^@]+@(\\w{2})\\S+(\\.[^\\s.]+)", "$1***@$2****$3");
        // return the Otp Token
        OtpToken token = new OtpToken(true, otpService.getOtpExpiryMinutes(), loggedInUserUin, null, null, maskedMobileNumber, maskedEmail);

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(token).build());
    }

    /**
     * update user loggedin contacts
     */
    @PutMapping("/contacts")
    public ResponseEntity<WsResponse<?>> updateUserContacts(@RequestBody @Validated UpdateContactsCmd userContacts, Authentication authentication) {
        log.debug("Handler for {}", "Update User Contacts");
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();

        if (!otpService.validateOtp(loggedInUserUin, userContacts.getPin())) {
            return generateFailResponse(WsError.EWsError.INVALID_OTP, userContacts.getPin());
        }
        UserDto databaseUser = null;
        try {
            databaseUser = userService.findByUin(Long.parseLong(loggedInUserUin)).orElseThrow(() -> new UsernameNotFoundException("No user found with username " + loggedInUserUin));
        } catch (Exception e) {
            log.error("Error while find user in  updating user contacts.", e);
            return generateFailResponse(WsError.EWsError.APPLICANT_NOT_FOUND, loggedInUserUin);
        }

        UpdateApplicantCmd applicantCmd = new UpdateApplicantCmd(String.valueOf(Long.parseLong(loggedInUserUin)), userContacts.getEmail(), userContacts.getCountryPhonePrefix() + userContacts.getMobileNumber(), userContacts.getCountryCode(), databaseUser.getDateOfBirthHijri());
        ApplicantLiteDto returnedApplicant = userService.updateUserInAdminPortal(applicantCmd);
        if (returnedApplicant == null)
            return generateFailResponse(WsError.EWsError.NOT_FOUND_IN_ADMIN, loggedInUserUin);


        // sets form fields to database user instance
        databaseUser.setEmail(userContacts.getEmail());
        databaseUser.setMobileNumber(userContacts.getMobileNumber());
        databaseUser.setCountryPhonePrefix(userContacts.getCountryPhonePrefix());
        databaseUser.setCountryCode(userContacts.getCountryCode());
        try {
            userService.save(databaseUser);
        } catch (Exception e) {
            log.error("Error while updating user contacts.", e);
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.UPDATE_USER_ERROR.getCode())
                                    .referenceNumber(String.valueOf(databaseUser.getUin()))
                                    .build()).build());
        }
        returnedApplicant.setCountryCode(databaseUser.getCountryPhonePrefix());
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(returnedApplicant).build());
    }


    /**
     * get user Tafweej details by uin and ritual ID
     *
     * @param companyRitualSeasonId       the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/tafweej/{companyRitualSeasonId}")
    public ResponseEntity<WsResponse<?>> findApplicantRitualStepsDetailsByUinAndCompanyRitualSeasonId(@PathVariable Long companyRitualSeasonId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<CompanyRitualStepMainDataDto> ritualSteps = userService.findApplicantRitualStepsDetailsByUinAndCompanyRitualSeasonId(loggedInUserUin, companyRitualSeasonId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(ritualSteps).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<WsResponse<?>> handleBadCredentialsException(
            BadCredentialsException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.BAD_CREDENTIALS.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

    /**
     * finds a user by his ID
     *
     * @param userId the user id to find
     * @return the found user or <code>null</code>
     */
    @GetMapping("/find/{userId}")
    @RolesAllowed(AuthorityConstants.EDIT_USER)
    public ResponseEntity<WsResponse<?>> findUser(@PathVariable long userId) {
        log.debug("Handler for {}", "Find User");
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(maskUserInfo(userService.findOne(userId))).build());
    }

    /**
     * Masking sensitive data from user information
     *
     * @param user the user to filter
     * @return the filtered user
     */
    private static UserDto maskUserInfo(UserDto user) {
        if (user != null) {
            user.setPasswordHash(CONFIDENTIAL);
            user.setPassword(CONFIDENTIAL);
        }
        return user;
    }

    @PostMapping("/store-user-locations")
    public ResponseEntity<WsResponse<?>> storeUserLocations(@RequestBody Map<String, List<UserLocationDto>> location, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();


            Optional<UserDto> databaseUser = userService.findByUin(Long.parseLong(loggedInUserUin));
            List<UserLocationDto> locationsList = location.get("location");
            locationsList.forEach(e -> e.setUserId(databaseUser.get().getId()));
          boolean isSaved = userLocationService.storeUserLocation(locationsList);
          if(!isSaved){
              return ResponseEntity.ok(
                      WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                              .body(WsError.builder().error(WsError.EWsError.INVALID_LOCATION_ENTRIES.getCode()).referenceNumber(loggedInUserUin).build()).build());

          }

          return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(true).build());

    }

    @PutMapping("/language/{lang}")
    public ResponseEntity<Object> updateUserPreferredLanguage(@PathVariable String lang, Authentication authentication) {
        log.debug("Handler for {}", "Update User preferred language");
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        UserDto databaseUser = null;
        try {
            databaseUser = userService.findByUin(Long.parseLong(loggedInUserUin)).orElseThrow(() -> new UsernameNotFoundException("No user found with username " + loggedInUserUin));
        } catch (Exception e) {
            log.error("Error while find user in  updating user preferred language.", e);
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.USER_NOT_FOUND.getCode()).referenceNumber(loggedInUserUin).build()).build());
        }
        // sets form fields to database user instance
        databaseUser.setPreferredLanguage(lang);
        try {
            userService.save(databaseUser);
        } catch (Exception e) {
            log.error("Error while updating user contacts.", e);
            return ResponseEntity.of(Optional.empty());
        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(null).build());
    }

}
