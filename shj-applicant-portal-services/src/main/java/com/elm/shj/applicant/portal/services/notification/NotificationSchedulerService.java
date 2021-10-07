package com.elm.shj.applicant.portal.services.notification;

import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.UserRepository;
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
import java.util.List;
import java.util.Optional;

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

    @PostConstruct
    @Scheduled(cron = "${scheduler.password.expiry.notification.cron}")
    @SchedulerLock(name = "notify-password-expiry-users-task")
    void notifyPasswordExpiredUsers() {
        List<JpaUser> users = userRepository.findDistinctByDeletedFalseAndActivatedTrueAndBlockedFalse();
        users.parallelStream().forEach(
                user -> {
                    if (checkPasswordExpiry(user.getId())) {
                        //send request to admin to get Template then sen notification for this user
                        System.out.println("notifying user with id :" + user.getId());
                    }

                }
        );


    }

    private boolean checkPasswordExpiry(long userId) {
        Optional<UserPasswordHistoryDto> userPasswordHistory = passwordHistoryService.findLastByUserId(userId);
        if (userPasswordHistory.isPresent()) {
            //check the date compared with configured password age
            LocalDate passwordCreationDate = userPasswordHistory.get().getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long result = ChronoUnit.DAYS.between(LocalDate.now(), passwordCreationDate.plusMonths(passwordAgeInMonths));
            return result > 0 && result <= passwordExpiryNotificationPeriod;
        }
        return false;
    }

}
