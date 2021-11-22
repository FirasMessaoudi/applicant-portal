/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.notification;

import com.elm.shj.applicant.portal.services.dto.DetailedUserNotificationDto;
import com.elm.shj.applicant.portal.services.dto.UserNotificationCategoryPreferenceDto;
import com.elm.shj.applicant.portal.services.integration.UserNewNotificationsCountVo;
import com.elm.shj.applicant.portal.services.notification.NotificationService;
import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<Integer> markUserNotificationAsRead(@PathVariable Long notificationId) {
        int numberOfRowsAffected = userService.markUserNotificationAsRead(notificationId);
        return ResponseEntity.ok(numberOfRowsAffected);
    }

    /**
     * get all notifications by user's UIN
     *
     * @param authentication the authenticated user
     */
    @GetMapping("/list")
    public ResponseEntity<?> findUserNotificationsByUin(Authentication authentication,
                                                        @RequestParam(required = false) EUserNotificationType type, Pageable pageable) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        if (type != null) {
            return ResponseEntity.ok(userService.findTypedUserNotificationsByUin(loggedInUserUin, type.name(), pageable));
        }
        return ResponseEntity.ok(userService.findUserNotificationsByUin(loggedInUserUin));
    }

    /**
     * Count user new notifications for logged-in user.
     *
     * @param authentication authenticated user
     * @return
     */
    @GetMapping("/new-notifications-count")
    public ResponseEntity<UserNewNotificationsCountVo> countUserNewNotifications(Authentication authentication) {
        // get the logged-in user id from authentication then count the un-read notifications
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        UserNewNotificationsCountVo notificationsCountVo = notificationService.countUserNewNotifications(loggedInUserUin);
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
