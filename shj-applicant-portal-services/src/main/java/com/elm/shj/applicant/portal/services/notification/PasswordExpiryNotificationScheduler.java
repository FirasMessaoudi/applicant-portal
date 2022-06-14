/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.notification;

import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.PasswordExpiryNotificationRequest;
import com.elm.shj.applicant.portal.services.dto.PasswordExpiryNotificationRequestUserParameters;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Handling User Notifications
 *
 * @author Ahmed Ali
 * @since 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PasswordExpiryNotificationScheduler {
    private final IntegrationService integrationService;
    private final UserRepository userRepository;
    private final PasswordHistoryService passwordHistoryService;
    @Value("${dcc.validation.password.expires.in.months}")
    private int passwordAgeInMonths;
    @Value("${password.expiry.notification.period.in.days}")
    private int passwordExpiryNotificationPeriod;

//    @PostConstruct
//    @Scheduled(cron = "${scheduler.password.expiry.notification.cron}")
//    @SchedulerLock(name = "notify-password-expiry-users-task")
    void notifyPasswordExpiredUsers() {
        log.debug("password Expiry notification scheduler started...");
        List<JpaUser> users = userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        Set<PasswordExpiryNotificationRequestUserParameters> passwordExpiryNotificationRequestUserParameters = new HashSet<>();
        PasswordExpiryNotificationRequest passwordExpiryNotificationRequest = new PasswordExpiryNotificationRequest();
        users.parallelStream().forEach(
                user -> {
                    long result = checkPasswordExpiry(user.getId());
                    if (result > 0 && result <= passwordExpiryNotificationPeriod) {
                        //for each user prepare his parameters and add to list of users request notification
                        passwordExpiryNotificationRequestUserParameters.add(
                                PasswordExpiryNotificationRequestUserParameters.builder()
                                        .userLang(user.getPreferredLanguage())
                                        .userId(String.valueOf(user.getUin()))
                                        .daysToExpiry((int) result)
                                        .build()
                        );
                        log.debug("notifying user with id :" + user.getId());
                    }

                }
        );
        passwordExpiryNotificationRequest.setUserParametersList(passwordExpiryNotificationRequestUserParameters);
        if (passwordExpiryNotificationRequestUserParameters.size() > 0) {
            integrationService.sendPasswordExpiryNotificationRequest(passwordExpiryNotificationRequest);
        } else {
            log.debug("no user has password will expire in {} days", passwordExpiryNotificationPeriod);
        }
    }

    private long checkPasswordExpiry(long userId) {
        Optional<UserPasswordHistoryDto> userPasswordHistory = passwordHistoryService.findLastByUserId(userId);
        if (userPasswordHistory.isPresent()) {
            //check today date compared with configured password age
            LocalDate passwordCreationDate = userPasswordHistory.get().getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return ChronoUnit.DAYS.between(LocalDate.now(), passwordCreationDate.plusMonths(passwordAgeInMonths));
        }
        return -1;
    }

}
