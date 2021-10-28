/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

import lombok.*;

/**
 * User new notifications count value object
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNewNotificationsCountVo {

    private int userSpecificNewNotificationsCount;
    private int userNotSpecificNewNotificationsCount;
}
