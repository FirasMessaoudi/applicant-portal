package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.ApplicantLiteDto;
import com.elm.shj.applicant.portal.services.dto.UpdateApplicantCmd;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.ValidateApplicantCmd;
import com.elm.shj.applicant.portal.services.otp.OtpService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import com.elm.shj.applicant.portal.web.security.otp.OtpToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
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
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.TOKEN_COOKIE_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_REGISTRATION)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RegistrationWsController {

    private final UserService userService;
    private final OtpService otpService;

    public static final String UPDATE_ADMIN_TOKEN_NAME = "uadmin";

    @PostMapping("/verify")
    public ResponseEntity<WsResponse<?>> verify(@RequestBody ValidateApplicantCmd command) {
        Optional<UserDto> userInApplicantPortal = userService.findByUin(Long.parseLong(command.getUin()));
        if (userInApplicantPortal.isPresent()) {

            return ResponseEntity.status(WsResponse.EWsResponseStatus.ALREADY_REGISTERED.getCode()).body(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.ALREADY_REGISTERED).body("User is already register").build());

        }

        ApplicantLiteDto userFromAdminPortal = userService.verify(command);
        if (userFromAdminPortal == null) {
            return ResponseEntity.status(WsResponse.EWsResponseStatus.NOT_FOUND_IN_ADMIN.getCode()).body(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.NOT_FOUND_IN_ADMIN).body("User Not found").build());
        }
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS).body(userFromAdminPortal).build());

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
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS).body(token).build());

    }

    @PostMapping
    public ResponseEntity<WsResponse<?>> register(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user, @RequestParam(UPDATE_ADMIN_TOKEN_NAME) boolean needToUpdateInAdminPortal, @RequestParam String pin) throws JSONException {

        if (!otpService.validateOtp(String.valueOf(user.getUin()), pin)) {

            return ResponseEntity.status(WsResponse.EWsResponseStatus.INVALID_OTP.getCode()).body(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.INVALID_OTP).body(null).build());
        }
        Optional<UserDto> userInApplicantPortal = userService.findByUin(user.getUin());
        if (userInApplicantPortal.isPresent()) {
            return ResponseEntity.status(WsResponse.EWsResponseStatus.ALREADY_REGISTERED.getCode()).body(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.ALREADY_REGISTERED).body("User is already register").build());

        }
        if (needToUpdateInAdminPortal) {
            UpdateApplicantCmd applicantCmd = new UpdateApplicantCmd(String.valueOf(user.getUin()), user.getEmail(), user.getCountryPhonePrefix() + user.getMobileNumber(), user.getCountryCode(), user.getDateOfBirthHijri());

            ApplicantLiteDto returnedApplicant = userService.updateUserInAdminPortal(applicantCmd);
            if (returnedApplicant == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE).body(null).build());

        }


        UserDto createdUser = userService.createUser(user);
        log.info("New user has been created with {} Uin number", createdUser.getUin());
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS).body(createdUser).build());
    }


}
