/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.dto.DetailedUserNotificationDto;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *  Controller for exposing notification web services for external party.
 *
 * @author f.messaoudi
 * @since 1.0.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_USERS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class NotificationWsController {
    private final UserService userService;
    private final NotificationService notificationService;

    /**
     * get all notifications by user ID
     *
     * @param authentication the authenticated user
     */

    @GetMapping("/notifications/list")
    public ResponseEntity<WsResponse<?>> findUserNotificationsById(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<DetailedUserNotificationDto> detailedUserNotificationDtos = userService.findUserNotificationsById(userService.findByUin(Long.parseLong(loggedInUserUin)).get().getId());
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(detailedUserNotificationDtos).build());
    }

    /**
     * mark notification as read
     *
     * @param notificationId
     */
    @PostMapping("/notifications/mark-as-read/{notificationId}")
    public ResponseEntity<WsResponse<?>> markUserNotificationAsRead(@PathVariable Long notificationId) {
        int numberOfRowsAffected = userService.markUserNotificationAsRead(notificationId);
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(numberOfRowsAffected).build());
    }

    /**
     * get user notification category preference
     *
     * @param authentication the authenticated user
     */
    @GetMapping("/notifications/category-preference")
    public ResponseEntity<WsResponse<?>> findUserNotificationCategoryPreference( Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<UserNotificationCategoryPreferenceDto> userNotificationCategoryPreferenceDtos = notificationService.findUserNotificationCategoryPreference(userService.findByUin(Long.parseLong(loggedInUserUin)).get().getId());
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(userNotificationCategoryPreferenceDtos).build());
    }

    /**
     * update user notification category preference
     *
     * @param  userNotificationCategoryPreference to update
     */
    @PutMapping("/notifications/update-user-notification-category-preference")
    public ResponseEntity<WsResponse<?>> updateUserNotificationCategoryPreference( @RequestBody UserNotificationCategoryPreferenceDto userNotificationCategoryPreference) {
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body( notificationService.save(userNotificationCategoryPreference)).build());
    }

    /**
     * Count user new notifications for logged-in user.
     *
     * @param authentication
     * @return
     */
    @GetMapping("/notifications/new-notifications-count")
    public ResponseEntity<WsResponse<?>> countUserNewNotifications(Authentication authentication) {
        // get the logged-in user id from authentication then count the un-read notifications
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        UserNewNotificationsCountVo notificationsCountVo = notificationService.countUserNewNotifications(userService.findByUin(Long.parseLong(loggedInUserUin)).get().getId());
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(notificationsCountVo).build());    }
}
