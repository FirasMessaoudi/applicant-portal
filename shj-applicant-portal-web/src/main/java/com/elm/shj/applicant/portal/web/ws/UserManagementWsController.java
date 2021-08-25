package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.AuthorityConstants;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.admin.ChangePasswordCmd;
import com.elm.shj.applicant.portal.web.admin.ResetPasswordCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
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
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.TOKEN_COOKIE_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_USERS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserManagementWsController {

    public static final String RESET_PASSWORD_SUCCESS_MSG = "Reset password successfully";
    public static final String CHANGE_PASSWORD_SUCCESS_MSG = "Change password successfully";


    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordHistoryService passwordHistoryService;
    private final JwtTokenService jwtTokenService;

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

    private ResponseEntity<WsResponse<?>> generateFailResponse(WsError.EWsError errorCode, String reference) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(errorCode.getCode()).referenceNumber(reference).build()).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<WsResponse<?>> handleBadCredentialsException(
            BadCredentialsException ex) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                        .body(WsError.builder().error(WsError.EWsError.BAD_CREDENTIALS.getCode()).referenceNumber(ex.getMessage()).build()).build());
    }

}