/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.user;

import com.elm.dcc.foundation.providers.email.service.EmailService;
import com.elm.dcc.foundation.providers.filescan.model.FileScanInfo;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.RoleRepository;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.*;
import com.elm.shj.applicant.portal.services.generic.GenericService;
import com.elm.shj.applicant.portal.services.integration.ApplicantRitualPackageVo;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.WsAuthenticationException;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.services.sms.HUICSmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    public static final String REGISTRATION_EMAIL_SUBJECT = "Welcome to Hajj App";
    public static final String REGISTRATION_EMAIL_TPL_NAME = "email-registration.ftl";
    public static final String RESET_PASSWORD_EMAIL_TPL_NAME = "email-reset-password.ftl";
    public static final String RESET_PASSWORD_SMS_NOTIFICATION_KEY = "reset.password.sms.notification";
    public static final String RESET_PASSWORD_EMAIL_SUBJECT = "Reset User Password إعادة تعيين كلمة السر";
    private static final long APPLICANT_ROLE_ID = 1L;
    private static final String DEFFAULT_HISTORY_PASSWORD = "$2a$10$A81/FuMFJWcxaJhUcL8isuVeKKa.hk7GVzTVTyf7xe/XoMVWuKckK";
    private static final int INVALID_DATE_OF_BIRTH = 139;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final HUICSmsService huicSmsService;
    private final EmailService emailService;
    private final IntegrationService integrationService;
    private final PasswordHistoryService passwordHistoryService;
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

    /**
     * Finds a user by his passport number
     *
     * @param passportNumber the uin of the user to find
     * @return the founded user or empty structure
     */
    public Optional<UserDto> findByPassportNumber(String passportNumber, String nationalityCode) {
        JpaUser user = userRepository.findByPassportNumberAndNationalityCodeAndDeletedFalseAndActivatedTrue(passportNumber, nationalityCode);
        return (user != null) ? Optional.of(getMapper().fromEntity(user, mappingContext)) : Optional.empty();
    }

    public Optional<UserDto> findByIdNumber(String idNumber) {
        JpaUser user = userRepository.findByIdNumberAndDeletedFalseAndActivatedTrue(idNumber);
        return (user != null) ? Optional.of(getMapper().fromEntity(user, mappingContext)) : Optional.empty();
    }

    /**
     * Finds a user by his UIN
     *
     * @param uin the UIN of the user to find
     * @return the found user or empty structure
     */
    public Optional<UserDto> findByUin(String uin) {
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
     * Update user preferred language
     *
     * @param uin the user uin
     * @param lang the user preferred langauge
     * @return Encoded avatar string
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void updatePreferredLanguage(long uin, String lang) {
        userRepository.updatePreferredLanguage(uin, lang);
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
        //  add relatives as contacts if not found
        createApplicantRelativesChatContacts(user.getUin());
        passwordHistoryService.addUserPasswordHistory(savedUser.getId(), DEFFAULT_HISTORY_PASSWORD);
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
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%&*-_+";
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
     * Reset user password and reset trials counter to 0.
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
        boolean smsSent = false;
        try {
            smsSent = huicSmsService.sendMessage(user.getCountryPhonePrefix() == null || user.getCountryPhonePrefix().isEmpty() ? 966 : Integer.valueOf(user.getCountryPhonePrefix()), String.valueOf(user.getMobileNumber()), createdUserSms, "comments");
        } catch (SSLException e) {
            log.error("Unable to send SMS for {}", user.getMobileNumber(), e);
        }
        log.debug("SMS notification status: {}", smsSent);

        // Send Email notification
        boolean emailSent = false;
        try {
            emailSent = emailService.sendMailFromTemplate(Arrays.asList(user.getEmail()), null,
                    RESET_PASSWORD_EMAIL_SUBJECT, RESET_PASSWORD_EMAIL_TPL_NAME, ImmutableMap.of("user", user));
        } catch (Exception e) {
            log.error("Unable to send email for {}", user.getEmail(), e);
        }
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
        boolean smsSent = false;
        try {
            smsSent = huicSmsService.sendMessage(user.getCountryPhonePrefix() == null || user.getCountryPhonePrefix().isEmpty() ? 966 : Integer.valueOf(user.getCountryPhonePrefix()), String.valueOf(user.getMobileNumber()), createdUserSms, "comments");
        } catch (SSLException e) {
            log.error("Unable to send SMS for {}", user.getMobileNumber(), e);
        }
        log.debug("SMS notification status: {}", smsSent);

        return smsSent || emailSent;
    }

    public Optional<ApplicantMainDataDto> findUserMainDataByUin(String uin, long applicantPackageId) {
        log.debug("Handler for findUserMainDataByUin {}", uin);
        return Optional.ofNullable(integrationService.loadUserMainData(uin, applicantPackageId));
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
        WsResponse<ApplicantLiteDto> wsResponse;
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
        if (wsResponse.getStatus() == INVALID_DATE_OF_BIRTH) {
            ApplicantLiteDto applicantLiteDto = new ApplicantLiteDto();
            applicantLiteDto.setApplicantVerifyStatus(INVALID_DATE_OF_BIRTH);
            return applicantLiteDto;
        } else if (WsResponse.EWsResponseStatus.FAILURE.getCode() == wsResponse.getStatus()) {
            return null;
        } else {
            return wsResponse.getBody();
        }
    }

    void createApplicantRelativesChatContacts(Long applicantUin) {
        WsResponse<ApplicantLiteDto> wsResponse = null;
        final String url = "/ws/create-relatives-chat-contact";
        try {
            wsResponse = integrationService.callIntegrationWs(url, HttpMethod.POST, String.valueOf(applicantUin), new ParameterizedTypeReference<WsResponse<Object>>() {
            });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to verify applicant.", e);

        } catch (Exception ex) {
            log.error("error while trying to create Applicant Relatives As Chat Contacts .", ex);
        }
        if (WsResponse.EWsResponseStatus.FAILURE.getCode() == wsResponse.getStatus()) {
            log.error("error while trying to create Applicant Relatives As Chat Contacts .");
        }
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
        if (WsResponse.EWsResponseStatus.FAILURE.getCode() == wsResponse.getStatus()) {
            return null;
        } else {
            return wsResponse.getBody();
        }
    }
    public Integer markAsRegistered(String  uin, String channel) {

        WsResponse<Integer> wsResponse = null;
        final String url = "/ws/mark-as-registered/" + channel;
        try {
            wsResponse = integrationService.callIntegrationWs(url, HttpMethod.POST, uin, new ParameterizedTypeReference<WsResponse<Integer>>() {
            });
        } catch (WsAuthenticationException e) {
            log.error("Cannot authenticate to update applicant.", e);
            return null;
        } catch (Exception ex) {
            return null;
        }
        if (WsResponse.EWsResponseStatus.FAILURE.getCode() == wsResponse.getStatus()) {
            return null;
        } else {
            return wsResponse.getBody();
        }
    }

    public ApplicantRitualLiteDto findApplicantRitualLatestByUin(String uin) {
        return integrationService.loadApplicantRitualLatestByUin(uin);
    }

    public Optional<ApplicantHealthLiteDto> findApplicantHealthDetailsByUinAndApplicantPackageId(String uin, Long ritualId) {
        return Optional.ofNullable(integrationService.loadApplicantHealthDetails(uin, ritualId));
    }

    public ApplicantRitualCardLiteDto findApplicantCardDetailsByUinAndApplicantPackageId(String uin, Long applicantPackageId) {
        return integrationService.loadApplicantCardDetails(uin, applicantPackageId);
    }

    public ApplicantPackageDetailsDto findApplicantPackageDetails(String uin, long applicantPackageId) {
        ApplicantPackageDetailsDto applicantPackageDetails =  integrationService.loadApplicantPackageDetails(uin, applicantPackageId);
        if(applicantPackageDetails.getCompanyLite() != null)
            applicantPackageDetails.getCompanyLite().setCode(applicantPackageDetails.getCompanyLite().getCode().contains("_") ?
                    applicantPackageDetails.getCompanyLite().getCode().substring(0, applicantPackageDetails.getCompanyLite().getCode().indexOf("_")) : applicantPackageDetails.getCompanyLite().getCode());
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Collections.sort(applicantPackageDetails.getApplicantPackageCaterings(), new Comparator<ApplicantPackageCateringDto>() {
            @Override
            public int compare(ApplicantPackageCateringDto o1, ApplicantPackageCateringDto o2) {
                 String date1 = simpleDateFormat.format(o1.getPackageCatering().getPackageHousing().getValidityStart());
                 String date2 = simpleDateFormat.format(o2.getPackageCatering().getPackageHousing().getValidityStart());
                int res1 = date1.compareTo(date2);
                if(res1!=0)
                    return res1;
                return o1.getPackageCatering().getMealTime().compareTo(o2.getPackageCatering().getMealTime());
            }
        });

        return applicantPackageDetails;
    }

    public List<ApplicantPackageCateringDto> findPackageCatering(String uin, long applicantPackageId) {
        return integrationService.loadPackageCatering(uin, applicantPackageId);
    }

    public List<CompanyRitualStepMainDataDto> findApplicantRitualStepsDetailsByUin(String uin) {
       return  integrationService.loadApplicantTafweejDetails(uin);
    }

    public List<CompanyStaffDto> findRelatedEmployeesByApplicantUinAndCompanyRitualSeasonId(String uin, Long companyRitualSeasonId) {
        return Collections.singletonList(integrationService.loadApplicantRelatedEmployeesDetails(uin, companyRitualSeasonId));
    }

    public CompanyRitualSeasonLiteDto findLatestApplicantRitualSeasonByUin(String uin) {
        return integrationService.loadLatestApplicantRitualSeasonByUin(uin);
    }

    public List<CompanyRitualSeasonLiteDto> findAllApplicantRitualSeasonByUin(String uin) {
        return integrationService.loadAllApplicantRitualSeasonByUin(uin);
    }

    public List<DetailedUserNotificationDto> findUserNotificationsByUin(String uin) {
        return integrationService.findUserNotificationsByUin(uin);
    }

    public Page<DetailedUserNotificationDto> findTypedUserNotificationsByUin(String uin, String type, Pageable pageable) {
        return integrationService.findTypedUserNotificationsByUin(uin, type, pageable);
    }

    public int markUserNotificationAsRead(long notificationId) {
        return integrationService.markUserNotificationAsRead(notificationId);
    }

    public CompanyLiteDto findCompanyDetailsByUinAndCompanyRitualSeasonId(String uin, long companyRitualSeasonId) {
        return integrationService.loadCompanyDetails(uin, companyRitualSeasonId);
    }

    public PackageHousingDto findHousingDetailsByUinAndApplicantPackageId(String uin, long applicantPackageId) {
        return integrationService.loadHousingDetails(uin, applicantPackageId);
    }

    public ApplicantRitualDto findApplicantRitual(String uin) {
        ApplicantRitualPackageVo applicantRitualPackageVo = findLatestApplicantRitualSeason(Long.parseLong(uin));
        return integrationService.findApplicantRitual(uin, applicantRitualPackageVo.getApplicantPackageId());
    }

    public Long findIdApplicantRitualId(String uin) {
        ApplicantRitualPackageVo applicantRitualPackageVo = findLatestApplicantRitualSeason(Long.parseLong(uin));
        return integrationService.findIdApplicantRitual(uin, applicantRitualPackageVo.getApplicantPackageId());
    }

    public ApplicantLiteDto findApplicantBasicDetailsByUin(String uin) {
        return integrationService.findApplicantBasicDetailsByUin(uin);
    }

    public void updatePreferredLanguageInAdminPortal(long uin, String lang) {
        integrationService.updatePreferredLanguage(uin, lang);
    }

    public List<ApplicantRitualPackageVo> findApplicantPackageAndRitualSeasonByUin(long uin) {
        return integrationService.findApplicantRitualPackageByUin(uin);
    }

    public ApplicantRitualPackageVo findLatestApplicantRitualSeason(long uin) {
        return integrationService.findLatestApplicantRitualPackageByUin(uin);
    }

    public WsResponse findGroupLeaderByUinAndSeasonId(String uin) {
        return integrationService.loadGroupLeaderByUinAndSeasonId(uin);
    }

    public BadgeVO findApplicantBadge(String loggedInUserUin, boolean withQr) {
        return integrationService.findApplicantBadge(loggedInUserUin,withQr);
    }



    public WsResponse findApplicantEmergencyContactByUin(String uin) {
       return integrationService.findApplicantEmergencyContactByUin(uin);
    }

    public WsResponse updateApplicantEmergencyContactByUin(ApplicantEmergencyContactDto applicantEmergencyNumber, String uin) {
        return integrationService.updateApplicantEmergencyContactByUin(uin, applicantEmergencyNumber);
    }

    public String findMobileNumber(long loggedInUserId) {
        return userRepository.findMobileNumber(loggedInUserId);
    }

    public List<BadgeVO> findApplicantBadges(String loggedInUserUin) {
        return integrationService.findApplicantBadges(loggedInUserUin);
    }

    public ApplicantHealthBasicDto updateHealth(ApplicantHealthBasicDto applicantHealthBasicDto) {
        return integrationService.updateApplicantHealth(applicantHealthBasicDto);
    }

}

