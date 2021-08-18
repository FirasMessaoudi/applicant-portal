/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.dcc.foundation.commons.validation.SafeFile;
import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.dcc.foundation.providers.recaptcha.model.RecaptchaInfo;
import com.elm.dcc.foundation.providers.recaptcha.service.RecaptchaService;
import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.config.RestTemplateConfig;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Main controller for admin user management page
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_USERS)
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserManagementController {

    public static final String CONFIDENTIAL = "<CONFIDENTIAL>";
    public static final String NEW_PWRD_FIELD_NAME = "newPassword";
    public static final String RESET_PWD_SMS_NOTIFICATION_KEY = "reset.password.sms.notification";
    public static final String CREATE_USER_SMS_NOTIFICATION_KEY = "user.mngt.new.user.sms.notification";
    private static final String PWRD_HISTORY_ERROR_MESSAGE_KEY = "{dcc.commons.validation.constraints.password-history}";
    private static final String PWRD_CONTAINS_USERNAME_ERROR_MESSAGE_KEY = "{dcc.commons.validation.constraints.password-contains-username}";
    private static final String CHANGE_PWRD_METHOD_NAME = "changeUserPassword";
    public static final String RECAPTCHA_TOKEN_NAME = "grt";

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordHistoryService passwordHistoryService;
    private final JwtTokenService jwtTokenService;
    private final RecaptchaService recaptchaService;
    private final RestTemplateConfig restTemplateConfig;


    /**
     * finds users by role id, nin or account status
     *
     * @param roleId    the user role id to find (<=0: all, > 0: valid id)
     * @param nin       the user nin to find (<=0: all, > 0: valid nin)
     * @param activated the user account status (1: true, 0: false, <0: all)
     * @return the found users or <code>null</code>
     */
    @GetMapping("/list/{roleId}/{nin}/{activated}")
    @RolesAllowed(AuthorityConstants.USER_MANAGEMENT)
    public Page<UserDto> search(Pageable pageable, @PathVariable long roleId, @PathVariable String nin, @PathVariable int activated, Authentication authentication) {
        log.debug("Handler for {}", "Search Users");
        if (roleId <= 0 && Long.parseLong(nin) == -1 && activated < 0) {
            return userService.findAllNotDeleted(pageable, jwtTokenService.retrieveUserIdFromToken(((JwtToken) authentication).getToken()).orElse(0L),
                    jwtTokenService.retrieveUserRoleIdsFromToken(((JwtToken) authentication).getToken()).orElse(new HashSet<>()))
                    .map(UserManagementController::maskUserInfo);
        }
        Page<UserDto> usersPage = userService.searchByRoleStatusOrNin(pageable,
                (roleId <= 0 ? null : roleId),
                (Long.parseLong(nin) == -1 ? null : nin.trim()),
                (activated < 0 ? null : BooleanUtils.toBoolean(activated)),
                jwtTokenService.retrieveUserIdFromToken(((JwtToken) authentication).getToken()).orElse(0L),
                jwtTokenService.retrieveUserRoleIdsFromToken(((JwtToken) authentication).getToken()).orElse(new HashSet<>()));
        return usersPage.map(UserManagementController::maskUserInfo);
    }

    /**
     * finds a user by his ID
     *
     * @param userId the user id to find
     * @return the found user or <code>null</code>
     */
    @GetMapping("/find/{userId}")
    @RolesAllowed(AuthorityConstants.EDIT_USER)
    public UserDto findUser(@PathVariable long userId) {
        log.debug("Handler for {}", "Find User");
        return maskUserInfo(userService.findOne(userId));
    }

    /**
     * finds a user by his nin
     *
     * @param nin the nin of the user to find
     * @return the found user or <code>null</code>>
     */
    @GetMapping("/find/nin/{nin}")
    @RolesAllowed(AuthorityConstants.EDIT_USER)
    public UserDto findByNin(@PathVariable Long nin) {
        log.debug("Find by nin {}", nin);
        return maskUserInfo(userService.findByNin(nin).orElse(null));
    }

    /**
     *
     * get user main data by uin and ritualId
     */
    @GetMapping("/main-data/{ritualId}")
    public ApplicantMainDataDto findUserMainDataByUin(@PathVariable long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        return userService.findUserMainDataByUin(loggedInUserUin, ritualId).orElseThrow(() -> new UsernameNotFoundException("No user found with Uin " + loggedInUserUin));

    }

    /**
     * get user ritual seasons by uin
     */
    @GetMapping("/ritual-seasons")
    public List<Integer> findApplicantRitualSeasonsByUin() {
        JwtToken loggedInUser = (JwtToken) SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserUin = ((User) loggedInUser.getPrincipal()).getUsername();
        return userService.findApplicantRitualSeasons(loggedInUserUin);

    }

    /**
     * get user ritual lite by seasons and uin
     */
    @GetMapping("/ritual-lite/{season}")
    public List<ApplicantRitualLiteDto> findApplicantRitualByUinAndSeasons(@PathVariable int season) {
        JwtToken loggedInUser = (JwtToken) SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserUin = ((User) loggedInUser.getPrincipal()).getUsername();
        return userService.findApplicantRitualByUinAndSeasons(loggedInUserUin, season);

    }

    /**
     * get user latest ritual lite by uin
     */
    @GetMapping("/ritual-lite/latest")
    public ApplicantRitualLiteDto findApplicantRitualByUinAndSeasons() {
        JwtToken loggedInUser = (JwtToken) SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserUin = ((User) loggedInUser.getPrincipal()).getUsername();
        return userService.findApplicantRitualLatestByUin(loggedInUserUin);

    }

    /**
     * get user health details by uin and ritual ID
     *
     * @param ritualId the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/health/{ritualId}")
    public ApplicantHealthLiteDto findApplicantHealthDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        return userService.findApplicantHealthDetailsByUinAndRitualId(loggedInUserUin, ritualId);
    }

    /**
     * get user card details by his uin and ritual ID
     *
     * @param ritualId the ID of the selected applicant's ritual
     * @param authentication the authenticated user
     */
    @GetMapping("/details/{ritualId}")
    public ApplicantRitualCardLiteDto findApplicantCardDetailsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        return userService.findApplicantCardDetailsByUinAndRitualId(loggedInUserUin, ritualId);
    }

    /**
     * Reset user password by admin as part of user management
     *
     * @param idNumber
     */
    @GetMapping("/reset-user-password/{idNumber}")
    @RolesAllowed(AuthorityConstants.RESET_USER_PASSWORD)
    public void resetUserPassword(@PathVariable Long idNumber) {
        UserDto user = userService.findByNin(idNumber).orElseThrow(() -> new UsernameNotFoundException("No user found with username " + idNumber));
        userService.resetPassword(user);
    }

    /**
     * Resets the user password
     *
     * @return the found user or <code>null</code>
     */
    @PostMapping("/reset-password")
    @RolesAllowed(AuthorityConstants.RESET_PASSWORD)
    public void resetUserPassword(@RequestBody @Valid ResetPasswordCmd command,
                                  @RequestParam(RECAPTCHA_TOKEN_NAME) String reCaptchaToken) {
        if (StringUtils.isBlank(reCaptchaToken)) {
            log.info("recaptcha response is not provided in the request...");
            throw new RecaptchaException("Invalid Captcha");
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        RecaptchaInfo recaptchaInfo;
        try {
            recaptchaInfo = recaptchaService.verifyRecaptcha(request.getRemoteAddr(), reCaptchaToken);
        } catch (IllegalArgumentException e) {
            throw new RecaptchaException("Invalid character in captcha response.");
        }
        if (recaptchaInfo == null || !recaptchaInfo.isSuccess()) {
            throw new RecaptchaException("Invalid captcha.");
        }

        UserDto user = userService.findByUin(command.getIdNumber()).orElseThrow(() -> new UsernameNotFoundException("No user found with username " + command.getIdNumber()));

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
            throw new BadCredentialsException("invalid credentials.");
        }
    }

    /**
     * Change the user password
     *
     * @return the found user or <code>null</code>
     */
    @PostMapping("/change-password")
    public void changeUserPassword(@RequestBody @Valid ChangePasswordCmd command) throws MethodArgumentNotValidException, NoSuchMethodException {
        log.debug("Handler for {}", "Change User Password");

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof JwtToken)) {
            return;
        }

        JwtToken loggedInUser = (JwtToken) SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserIdNumberStr = ((User) loggedInUser.getPrincipal()).getUsername();
        long loggedInUserIdNumber = Long.parseLong(loggedInUserIdNumberStr);
        String oldPasswordHash = userService.retrievePasswordHash(loggedInUserIdNumber);
        if (!BCrypt.checkpw(command.getOldPassword(), oldPasswordHash)) {
            throw new BadCredentialsException("Old password does not match");
        }
        // current password cannot be used as new password
        if (command.getNewPassword().equals(command.getOldPassword())) {
            //raise an error
            rejectNewPassword(command, PWRD_HISTORY_ERROR_MESSAGE_KEY);
        }
        // password should not contain the user's username which is id number
        if (command.getNewPassword().contains(loggedInUserIdNumberStr)) {
            rejectNewPassword(command, PWRD_CONTAINS_USERNAME_ERROR_MESSAGE_KEY);
        }

        //retrieve old passwords and compare the new password with the returned list
        Optional<UserPasswordHistoryDto> matchedOldPassword = Optional.empty();
        Optional<Long> userIdFromToken = jwtTokenService.retrieveUserIdFromToken(loggedInUser.getToken());
        if (userIdFromToken.isPresent()) {
            matchedOldPassword = passwordHistoryService.findByUserId(userIdFromToken.get()).stream().filter(passHistory -> BCrypt.checkpw(command.getNewPassword(), passHistory.getOldPasswordHash())).findFirst();
        }

        if (matchedOldPassword.isPresent()) {
            //raise an error
            BindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(command, command.getClass().getName());
            beanPropertyBindingResult.rejectValue(NEW_PWRD_FIELD_NAME, StringUtils.EMPTY, null, PWRD_HISTORY_ERROR_MESSAGE_KEY);
            MethodParameter methodParameter = new MethodParameter(this.getClass().getMethod(CHANGE_PWRD_METHOD_NAME,
                    command.getClass()), 0);
            throw new MethodArgumentNotValidException(methodParameter, beanPropertyBindingResult);
        }
        //no matching, update the password
        userService.updateUserPassword(loggedInUserIdNumber, passwordEncoder.encode(command.getNewPassword()));
        userService.clearToken(loggedInUserIdNumber);
        //keep password history
        userIdFromToken.ifPresent(aLong -> passwordHistoryService.addUserPasswordHistory(aLong, oldPasswordHash));
    }

    /**
     * Reject provided password value and throw an exception
     *
     * @param command         {@link ChangePasswordCmd}
     * @param errorMessageKey Error message key defined in localization message file
     * @throws MethodArgumentNotValidException
     * @throws NoSuchMethodException
     */
    private void rejectNewPassword(ChangePasswordCmd command, String errorMessageKey) throws MethodArgumentNotValidException,
            NoSuchMethodException {
        BindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(command, command.getClass().getName());
        beanPropertyBindingResult.rejectValue(NEW_PWRD_FIELD_NAME, StringUtils.EMPTY, null, errorMessageKey);
        MethodParameter methodParameter = new MethodParameter(this.getClass().getMethod(CHANGE_PWRD_METHOD_NAME,
                command.getClass()), 0);
        throw new MethodArgumentNotValidException(methodParameter, beanPropertyBindingResult);
    }

    /**
     * Upload user avatar
     *
     * @param userAvatarFile the user avatar file
     * @param userId         the user id
     * @return the encoded user avatar
     */
    @PostMapping("/avatar/{userId}")
    @RolesAllowed(AuthorityConstants.EDIT_USER)
    public ResponseEntity<String> updateUserAvatar(@RequestParam("avatar") @SafeFile MultipartFile userAvatarFile, @PathVariable Long userId) throws IOException {
        log.debug("Handler for {}", "Update User Avatar");
        String encodedAvatarStr = userService.updateUserAvatar(userId, userAvatarFile.getBytes());
        return ResponseEntity.ok(encodedAvatarStr);
    }

    /**
     * Updates and existing user
     *
     * @param user the user to update
     * @return the updated user
     */
    @PostMapping("/update")
    @RolesAllowed(AuthorityConstants.EDIT_USER)
    public ResponseEntity<UserDto> updateUser(@RequestBody @Validated({UserDto.UpdateUserValidationGroup.class, Default.class}) UserDto user) {
        log.debug("Handler for {}", "Update User");

        UserDto databaseUser = userService.findOne(user.getId());

        // sets form fields to database user instance
        databaseUser.setDateOfBirthGregorian(user.getDateOfBirthGregorian());
        databaseUser.setDateOfBirthHijri(user.getDateOfBirthHijri());
        databaseUser.setActivated(user.isActivated());
        databaseUser.setEmail(user.getEmail());
        databaseUser.setFullNameAr(user.getFullNameAr());
        databaseUser.setGender(user.getGender());
        databaseUser.setFullNameEn(user.getFullNameEn());
        databaseUser.setMobileNumber(user.getMobileNumber());
        databaseUser.setNin(user.getNin());
        databaseUser.setUin(user.getUin());
        databaseUser.setUserRoles(user.getUserRoles());
        UserDto savedUser = maskUserInfo(userService.save(databaseUser));

        return ResponseEntity.ok(Objects.requireNonNull(savedUser));
    }

    /**
     * Updates and existing user
     *
     * @param userContacts the user contacts info to update
     * @return the updated user contacts
     */
    @PutMapping("/contacts")
    public ResponseEntity<ApplicantLiteDto> updateUserContacts(@RequestBody @Validated UpdateContactsCmd userContacts, Authentication authentication) {
        log.debug("Handler for {}", "Update User Contacts");
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        UserDto databaseUser = null;
        try {
            databaseUser = userService.findByUin(Long.parseLong(loggedInUserUin)).orElseThrow(() -> new UsernameNotFoundException("No user found with username " + loggedInUserUin));
        } catch (Exception e) {
            log.error("Error while find user in  updating user contacts.", e);
            return ResponseEntity.notFound().build();
        }

        UpdateApplicantCmd applicantCmd = new UpdateApplicantCmd(String.valueOf(Long.parseLong(loggedInUserUin)), userContacts.getEmail(), userContacts.getCountryPhonePrefix() + userContacts.getMobileNumber(), userContacts.getCountryCode(), databaseUser.getDateOfBirthHijri());
        ApplicantLiteDto returnedApplicant = userService.updateUserInAdminPortal(applicantCmd);
        if (returnedApplicant == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


        // sets form fields to database user instance
        databaseUser.setEmail(userContacts.getEmail());
        databaseUser.setMobileNumber(userContacts.getMobileNumber());
        databaseUser.setCountryPhonePrefix(userContacts.getCountryPhonePrefix());
        databaseUser.setCountryCode(userContacts.getCountryCode());
        try {
            userService.save(databaseUser);
        } catch (Exception e) {
            log.error("Error while updating user contacts.", e);
            return ResponseEntity.of(Optional.empty());
        }
        returnedApplicant.setCountryCode(databaseUser.getCountryPhonePrefix());
        return ResponseEntity.ok(Objects.requireNonNull(returnedApplicant));
    }

    /**
     * Add new user.
     *
     * @param user the user to create
     * @return the created user
     */
    @PostMapping("/create")
    @RolesAllowed(AuthorityConstants.ADD_USER)
    public ResponseEntity<UserDto> createUser(@RequestBody @Validated({UserDto.CreateUserValidationGroup.class, Default.class}) UserDto user) {
        log.debug("Handler for {}", "Create User");
        UserDto savedUser = null;
        try {
            savedUser = userService.createUser(user);
        } catch (Exception e) {
            log.error("Error while creating user.", e);
            return ResponseEntity.of(Optional.empty());
        }
        return ResponseEntity.ok(Objects.requireNonNull(savedUser));
    }

    /**
     * Activate inactive user
     *
     * @param userId
     * @return
     */
    @PostMapping("/activate/{userId}")
    @RolesAllowed(AuthorityConstants.CHANGE_USER_STATUS)
    public ResponseEntity<String> activateUser(@PathVariable long userId) {
        log.debug("Handler for {}", "activate user");
        userService.activateUser(userId);
        return ResponseEntity.ok(StringUtils.EMPTY);
    }

    /**
     * Deactivate active user
     *
     * @param userId
     * @return
     */
    @PostMapping("/deactivate/{userId}")
    @RolesAllowed(AuthorityConstants.CHANGE_USER_STATUS)
    public ResponseEntity<String> deactivateUser(@PathVariable long userId) {
        log.debug("Handler for {}", "deactivate user");
        userService.deactivateUser(userId);
        return ResponseEntity.ok(StringUtils.EMPTY);
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


}
