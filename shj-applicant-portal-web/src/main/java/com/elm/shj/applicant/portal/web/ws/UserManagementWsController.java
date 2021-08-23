package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.AuthorityConstants;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.admin.ResetPasswordCmd;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private final UserService userService;

    public static final String RESET_PASSWORD_SUCCESS_MSG = "Reset password successfully";

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
                            .body(WsError.builder().error(WsError.EWsError.APPLICANT_NOT_MATCHED.getCode()).referenceNumber(command.getIdNumber()+"").build()).build());
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
                            .body(WsError.builder().error(WsError.EWsError.APPLICANT_NOT_MATCHED.getCode()).referenceNumber(command.getIdNumber()+"").build()).build());
        }

        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode()).body(RESET_PASSWORD_SUCCESS_MSG).build());
    }
}
