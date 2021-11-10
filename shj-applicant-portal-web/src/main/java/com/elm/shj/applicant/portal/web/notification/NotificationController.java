/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.notification;

import com.elm.shj.applicant.portal.services.dto.UserNotificationCategoryPreferenceDto;
import com.elm.shj.applicant.portal.services.integration.UserNewNotificationsCountVo;
import com.elm.shj.applicant.portal.services.notification.NotificationService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Main controller for user notifications
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Navigation.API_NOTIFICATION)
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<Integer> markUserNotificationAsRead(@PathVariable Long notificationId) {
        int numberOfRowsAffected = userService.markUserNotificationAsRead(notificationId);
        return ResponseEntity.ok(numberOfRowsAffected);
    }

    /**
     * Count user new notifications for logged-in user.
     *
     * @param authentication
     * @return
     */
    @GetMapping("/new-notifications-count")
    public ResponseEntity<UserNewNotificationsCountVo> countUserNewNotifications(Authentication authentication) {
        // get the logged-in user id from authentication then count the un-read notifications
        long loggedInUserId = jwtTokenService.retrieveUserIdFromToken(((JwtToken) authentication).getToken()).orElse(0L);
        UserNewNotificationsCountVo notificationsCountVo = notificationService.countUserNewNotifications(loggedInUserId);
        return ResponseEntity.ok(notificationsCountVo);
    }

    /**
     * Update user notification category preference.
     *
     * @param userNotificationCategoryPreference
     * @return updated userNotificationCategoryPreference
     */
    @PostMapping("/update-user-notification-category-preference")
    public ResponseEntity<UserNotificationCategoryPreferenceDto> updateUserNotificationCategoryPreference(@RequestBody UserNotificationCategoryPreferenceDto userNotificationCategoryPreference) {
        UserNotificationCategoryPreferenceDto userNotificationCategoryPreferenceUpdated = notificationService.save(userNotificationCategoryPreference);
        return ResponseEntity.ok(userNotificationCategoryPreferenceUpdated);
    }
}
