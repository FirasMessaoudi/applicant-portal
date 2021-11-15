/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

/**
 * class for  Password Expiry Notification Request Parameter Values for the user
 *
 * @author Ahmed Ali
 * @since 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordExpiryNotificationRequestUserParameters {

    private String userId;
    private String userLang;
    private int daysToExpiry;
}
