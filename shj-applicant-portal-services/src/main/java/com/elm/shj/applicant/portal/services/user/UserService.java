/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.user;

import com.elm.dcc.foundation.providers.email.service.EmailService;
import com.elm.dcc.foundation.providers.sms.service.SmsGatewayService;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.RoleRepository;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.generic.GenericService;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsAuthenticationException;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * Service handling user management operations
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService extends GenericService<JpaUser, UserDto, Long> {

    public static final String CREATE_USER_SMS_NOTIFICATION_KEY = "user.mngt.new.user.sms.notification";
    public static final String REGISTRATION_EMAIL_SUBJECT = "Welcome to ELM Product";
    public static final String REGISTRATION_EMAIL_TPL_NAME = "email-registration.ftl";
    public static final String RESET_PASSWORD_EMAIL_TPL_NAME = "email-reset-password.ftl";
    public static final String RESET_PASSWORD_SMS_NOTIFICATION_KEY = "reset.password.sms.notification";
    public static final String RESET_PASSWORD_EMAIL_SUBJECT = "Reset User Password إعادة تعيين كلمة السر";
    private static final long APPLICANT_ROLE_ID = 1L;


    @Value("${admin.portal.url}")
    private String adminPortalUrl;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final SmsGatewayService smsGatewayService;
    private final EmailService emailService;
    private final IntegrationService integrationService;
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Finds all non deleted users.
     *
     * @param pageable
     * @param loggedInUserId
     * @param loggedInUserRoleIds
     * @return
     */
    public Page<UserDto> findAllNotDeleted(Pageable pageable, long loggedInUserId, Set loggedInUserRoleIds) {
        if (loggedInUserRoleIds.contains(RoleRepository.SYSTEM_ADMIN_USER_ROLE_ID))
            return mapPage(userRepository.findDistinctByDeletedFalseAndIdNot(pageable, loggedInUserId));
        // exclude system users in returned list
        return mapPage(userRepository.findDistinctByDeletedFalseAndIdNotAndUserRolesRoleIdNot(pageable, loggedInUserId, RoleRepository.SYSTEM_ADMIN_ROLE_ID));
    }

    /**
     * Finds a user by his nin
     *
     * @param nin the nin of the user to find
     * @return the founded user or empty structure
     */
    public Optional<UserDto> findByNin(long nin) {
        JpaUser user = userRepository.findByNinAndDeletedFalseAndActivatedTrueAndUserRolesRoleDeletedFalseAndUserRolesRoleActivatedTrue(nin);
        return (user != null) ? Optional.of(getMapper().fromEntity(user, mappingContext)) : Optional.empty();
    }

    /**
     * Finds a user by his nin
     *
     * @param uin the uin of the user to find
     * @return the founded user or empty structure
     */
    public Optional<UserDto> findByUin(long uin) {
        JpaUser user = userRepository.findByUinAndDeletedFalseAndActivatedTrue(uin);
        return (user != null) ? Optional.of(getMapper().fromEntity(user, mappingContext)) : Optional.empty();
    }


    public Optional<UserDto> findByUinAndDateOfBirth(long uin, Date dateOfBirth) {
        JpaUser user = userRepository.findDistinctByDeletedFalseAndUinEqualsAndDateOfBirthGregorianEquals(uin, dateOfBirth);
        return (user != null) ? Optional.of(getMapper().fromEntity(user, mappingContext)) : Optional.empty();
    }


    /**
     * finds users by role id, nin or account status
     *
     * @param roleId              the user role id to find
     * @param nin                 the user nin to find
     * @param activated           the user account status
     * @param loggedInUserRoleIds
     * @return the found users or <code>null</code>
     */
    public Page<UserDto> searchByRoleStatusOrNin(Pageable pageable, Long roleId, String nin, Boolean activated, long loggedInUserId, Set loggedInUserRoleIds) {
        //TODO: need to review the exclude part.
        return mapPage(userRepository.findByRoleOrNinOrStatus(pageable, roleId, (nin == null ? null : "%" + nin + "%"), activated, loggedInUserId,
                // exclude system users in returned list
                (loggedInUserRoleIds.contains(RoleRepository.SYSTEM_ADMIN_USER_ROLE_ID)) ? null : RoleRepository.SYSTEM_ADMIN_ROLE_ID));
    }

    /**
     * Deletes a user from the system
     *
     * @param userId the id of the user to delete
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void deleteUser(long userId) {
        userRepository.markDeleted(userId);
    }

    /**
     * Activate inactive user
     *
     * @param userId
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void activateUser(long userId) {
        userRepository.activate(userId);
    }

    /**
     * Deactivate active user
     *
     * @param userId
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void deactivateUser(long userId) {
        userRepository.deactivate(userId);
    }

    /**
     * Retrieves PasswordHash from DB
     *
     * @param idNumber the id number of the user to retrieve
     * @return the user's PasswordHash
     */
    public String retrievePasswordHash(long idNumber) {
        return userRepository.retrievePasswordHash(idNumber);
    }

    /**
     * Clears user token from the system
     *
     * @param userId      the user id to update
     * @param tokenExpiry the token expiry
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void updateUserLoginInfo(long userId, Date tokenExpiry) {
        userRepository.updateLoginInfo(0, new Date(), new Date(), tokenExpiry, userId);
    }

    /**
     * Updates user in the system
     *
     * @param idNumber the id number of the user to update
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void updateUserPassword(long idNumber, String passwordHash) {
        userRepository.updatePassword(idNumber, passwordHash);
    }

    /**
     * Clears user token from the system
     *
     * @param idNumber the id number of the user to delete
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void clearToken(long idNumber) {
        userRepository.clearToken(idNumber);
    }

    /**
     * Checks if user has a token in the system
     *
     * @param idNumber the id number of the user
     * @return whether user has a token in the system
     */
    public boolean hasToken(long idNumber) {
        Date tokenExpiryDate = userRepository.retrieveTokenExpiryDate(idNumber);
        log.info("Token Expiry Date {} found for {}", tokenExpiryDate, idNumber);
        return tokenExpiryDate != null;
    }

    /**
     * Update user avatar
     *
     * @param userId the user id
     * @param avatar the user avatar
     * @return Encoded avatar string
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public String updateUserAvatar(long userId, byte[] avatar) {
        String encodedAvatarStr = Base64.getEncoder().encodeToString(avatar);
        userRepository.updateAvatar(userId, encodedAvatarStr);
        return encodedAvatarStr;
    }

    /**
     * Creates a new user
     *
     * @param user the user information to save
     * @return UserDto saved one
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public UserDto createUser(UserDto user) {

        // encode the password
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        // set default values for new user
        user.setId(0);
        user.setBlockDate(null);
        user.setBlocked(false);
        user.setChangePasswordRequired(false);
        user.setDeleted(false);
        user.setActivated(true);
        user.setLastLoginDate(null);
        user.setNumberOfTries(0);
        user.setUpdateDate(null);
        user.setCreationDate(new Date());
        //update UserRole objects
        RoleDto rDTO = new RoleDto();
        rDTO.setId(APPLICANT_ROLE_ID);
        UserRoleDto userRoleDto = constructNewUserRoleDTO(user, rDTO);
        Set userRoles = new HashSet<UserRoleDto>();
        userRoles.add(userRoleDto);
        user.setUserRoles(userRoles);
        // save user information
        UserDto savedUser = save(user);
        // user created successfully, send SMS notification which contains the temporary password
        boolean smsSent = notifyRegisteredUser(savedUser);
        log.debug("SMS notification status: {}", smsSent);

        return savedUser;
    }


    protected UserRoleDto constructNewUserRoleDTO(UserDto user, RoleDto rDTO) {
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setUser(user);
        userRoleDto.setRole(rDTO);
        userRoleDto.setMainRole(true);

        return userRoleDto;
    }

    /**
     * Updates the number of login tries, count failed login
     *
     * @param user the user to update
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void updateLoginTries(UserDto user) {
        userRepository.updateLoginTries(user.getId(), user.getNumberOfTries() + 1, new Date());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public UserDto save(UserDto user) {
        // convert avatar file to encoded bytes
        if (user.getAvatarFile() != null && !user.getAvatarFile().isEmpty()) {
            try {
                user.setAvatar(Base64.getEncoder().encodeToString(user.getAvatarFile().getBytes()));
            } catch (IOException e) {
                log.error("Error while setting avatar bytes for user.", e);
            }
        }
        return super.save(user);
    }

    /**
     * Generate new password for the user
     *
     * @return generated password
     */
    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%&*-_+|";
        return RandomStringUtils.random(8, characters);
    }

    /**
     * Check if Saudi or not
     *
     * @param idNumber
     * @return
     */
    private boolean isCitizen(long idNumber) {
        return Long.toString(idNumber).startsWith("1");
    }

    /**
     * Reset user password.
     *
     * @param user
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void resetPassword(UserDto user) {
        String newPassword = generatePassword();
        user.setPassword(newPassword);
        log.info("Password reset for user {} , new password is {} .", user.getUin(), newPassword);
        // first notify user with the new password, if notification failed, password will not be changed
        if (!notifyUserOnPasswordReset(user)) {
            log.error("Password reset cannot be done, unable to notify user with the new password.");
            return;
        }

        String updatedPwd = passwordEncoder.encode(newPassword);
        userRepository.resetPwd(user.getId(), updatedPwd, true);
    }


    /**
     * Send SMS and Email for user on password reset.
     *
     * @param user
     * @return
     */
    public boolean notifyUserOnPasswordReset(UserDto user) {
        // Send SMS notification
        String[] smsNotificationArgs = new String[]{user.getPassword()};
        String locale = (user.getNin() != null && isCitizen(user.getNin())) ? "ar" : "en";


        String createdUserSms = messageSource.getMessage(RESET_PASSWORD_SMS_NOTIFICATION_KEY, smsNotificationArgs, Locale.forLanguageTag(locale));
        boolean smsSent = smsGatewayService.sendMessage(Long.parseLong(user.getMobileNumber()), createdUserSms);
        log.debug("SMS notification status: {}", smsSent);

        // Send Email notification
        boolean emailSent = emailService.sendMailFromTemplate(Arrays.asList(user.getEmail()), null,
                RESET_PASSWORD_EMAIL_SUBJECT, RESET_PASSWORD_EMAIL_TPL_NAME, ImmutableMap.of("user", user));
        log.debug("Email notification status: {}", emailSent);

        return smsSent || emailSent;
    }

    /**
     * Send SMS and Email for registered user.
     *
     * @param user
     * @return
     */
    public boolean notifyRegisteredUser(UserDto user) {
        String[] smsNotificationArgs = new String[]{user.getPassword()};
        // if nin  is there and start by 1 then locale ar otherwise locale is en
        String locale = (user.getNin() != null && isCitizen(user.getNin())) ? "ar" : "en";

        String createdUserSms = messageSource.getMessage(CREATE_USER_SMS_NOTIFICATION_KEY, smsNotificationArgs, Locale.forLanguageTag(locale));

        // Send Email notification which contains the username
        boolean emailSent = emailService.sendMailFromTemplate(Arrays.asList(user.getEmail()), null,
                REGISTRATION_EMAIL_SUBJECT, REGISTRATION_EMAIL_TPL_NAME, ImmutableMap.of("user", user));
        log.debug("Email notification status: {}", emailSent);
        //TODO:TO CHECK SMSGETWAY EXPECTED NUMBER FORMAT
        boolean smsSent = smsGatewayService.sendMessage(Long.parseLong(user.getMobileNumber()), createdUserSms);

        return smsSent || emailSent;
    }

    public Optional<ApplicantMainDataDto> findUserMainDataByUin(String uin, long ritualId) {
        return Optional.ofNullable(integrationService.loadUserMainData(uin, ritualId));
    }

    public List<Integer> findApplicantRitualSeasons(String uin) {
        return integrationService.loadRitualSeasonByUin(uin);
    }

    public List<ApplicantRitualLiteDto> findApplicantRitualByUinAndSeasons(String uin, int season) {
        return integrationService.loadApplicantRitualByUinAndSeasons(uin, season);
    }


    /**
     * verify user in  command portal.
     * * @param ValidateApplicantCmd (uin,dateOfBirthGregorian,dateOfBirthHijri )
     *
     * @return applicantLiteDto
     */
    public ApplicantLiteDto verify(ValidateApplicantCmd command) {
        WsResponse<ApplicantLiteDto> wsResponse = null;
        final String url = "/ws/verify";
        try {
            wsResponse = integrationService.callIntegrationWs(url, HttpMethod.POST, command, new ParameterizedTypeReference<WsResponse<ApplicantLiteDto>>() {
            });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to verify applicant.", e);
            return null;
        } catch (Exception ex) {
            return null;
        }
        return wsResponse.getBody();
    }

    /**
     * update user in  command portal.
     * * @param UpdateApplicantCmd (uin,email,mobileNumber,countryCode,dateOfBirthHijri )
     *
     * @return applicantLiteDto
     */
    public ApplicantLiteDto updateUserInAdminPortal(UpdateApplicantCmd applicantCmd) {

        WsResponse<ApplicantLiteDto> wsResponse = null;
        final String url = "/ws/update";
        try {
            wsResponse = integrationService.callIntegrationWs(url, HttpMethod.POST, applicantCmd, new ParameterizedTypeReference<WsResponse<ApplicantLiteDto>>() {
            });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to update applicant.", e);
            return null;
        } catch (Exception ex) {
            return null;
        }

        return wsResponse.getBody();


    }

    private HttpHeaders preCallAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("CALLER-TYPE", "WEB-SERVICE");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    public ApplicantRitualLiteDto findApplicantRitualLatestByUin(String uin) {
        return integrationService.loadApplicantRitualLatestByUin(uin);
    }

    public ApplicantHealthLiteDto findApplicantHealthDetailsByUinAndRitualId(String uin, Long ritualId) {
        return integrationService.loadApplicantHealthDetails(uin, ritualId);
    }

    public ApplicantRitualCardLiteDto findApplicantCardDetailsByUinAndRitualId(String uin, Long ritualId) {
        return integrationService.loadApplicantCardDetails(uin, ritualId);
    }
}

