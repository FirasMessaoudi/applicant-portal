/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.notification;

import com.elm.shj.applicant.portal.services.user.UserService;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<Integer> markUserNotificationAsRead(@PathVariable Long notificationId) {
        int numberOfRowsAffected = userService.markUserNotificationAsRead(notificationId);
        return ResponseEntity.ok(numberOfRowsAffected);
    }
}
