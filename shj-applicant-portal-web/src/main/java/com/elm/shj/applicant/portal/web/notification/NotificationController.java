/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.notification;

import com.elm.shj.applicant.portal.web.navigation.Navigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

}
