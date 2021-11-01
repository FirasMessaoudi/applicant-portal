/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.notification;

import com.elm.shj.applicant.portal.services.dto.UserNotificationCategoryPreferenceDto;
import com.elm.shj.applicant.portal.services.integration.IntegrationService;
import com.elm.shj.applicant.portal.services.integration.UserNewNotificationsCountVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling user notification operations
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class NotificationService {

    private final IntegrationService integrationService;

    /**
     * Count user new notifications.
     *
     * @param userId user id
     * @return number of un-read notifications
     */
    public UserNewNotificationsCountVo countUserNewNotifications(long userId) {
        return this.integrationService.countUserNewNotifications(userId);
    }

    /**
     * Find user notification category preference.
     *
     * @param userId
     * @return
     */
    public List<UserNotificationCategoryPreferenceDto> findUserNotificationCategoryPreference(long userId) {
        return this.integrationService.findUserNotificationCategoryPreference(userId);
    }

}
