package com.elm.shj.applicant.portal.services.notification;

import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
import com.elm.shj.applicant.portal.services.dto.PasswordExpiryNotificationRequest;
import com.elm.shj.applicant.portal.services.dto.PasswordExpiryNotificationRequestParameterValue;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.user.PasswordHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Service handling Applicant Notifications
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class NotificationSchedulerService {
    private final IntegrationService integrationService;
    private final UserRepository userRepository;
    private final PasswordHistoryService passwordHistoryService;
    @Value("${dcc.validation.password.expires.in.months}")
    private int passwordAgeInMonths;
    @Value("${password.expiry.notification.period.in.days}")
    private int passwordExpiryNotificationPeriod;
    private final String NOTIFICATION_TEMPLATE_NAME_CODE = "PASSWORD_EXPIRATION";

    @PostConstruct
    @Scheduled(cron = "${scheduler.password.expiry.notification.cron}")
    @SchedulerLock(name = "notify-password-expiry-users-task")
    void notifyPasswordExpiredUsers() {
        //check if this notification is enabled or not
        log.debug("password Expiry notification scheduler started...");
        List<JpaUser> users = userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        Set<PasswordExpiryNotificationRequestParameterValue> pExpiryNotificationRequestParamValues = new HashSet<>();
        PasswordExpiryNotificationRequest passwordExpiryNotificationRequest = new PasswordExpiryNotificationRequest();
        users.parallelStream().forEach(
                user -> {
                    long result = checkPasswordExpiry(user.getId());
                    if (result > 0 && result <= passwordExpiryNotificationPeriod) {
                        //for each user prepare his object and add this object to list of user requests notification
                        pExpiryNotificationRequestParamValues.add(
                                PasswordExpiryNotificationRequestParameterValue.builder()
                                        .uin(user.getUin())
                                        .userName(user.getPreferredLanguage().equals("ar") ? user.getFullNameAr() : user.getFullNameEn())
                                        .userLang(user.getPreferredLanguage())
                                        .userId(user.getId())
                                        .dayDiff((int) result)
                                        .build()
                        );
                        System.out.print("notifying user with id :" + user.getId());
                    }

                }
        );
        passwordExpiryNotificationRequest.setParameterValueList(pExpiryNotificationRequestParamValues);
        integrationService.sendPasswordExpiryNotificationRequest(passwordExpiryNotificationRequest);
    }

    private long checkPasswordExpiry(long userId) {
        Optional<UserPasswordHistoryDto> userPasswordHistory = passwordHistoryService.findLastByUserId(userId);
        if (userPasswordHistory.isPresent()) {
            //check the date compared with configured password age
            LocalDate passwordCreationDate = userPasswordHistory.get().getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return ChronoUnit.DAYS.between(LocalDate.now(), passwordCreationDate.plusMonths(passwordAgeInMonths));
        }
        return -1;
    }

}
