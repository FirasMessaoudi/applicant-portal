/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 * class for Password Expiry Notification Requests from the user
 *
 * @author Ahmed Ali
 * @since 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordExpiryNotificationRequest implements Serializable {
    private static final long serialVersionUID = 8663675565915427887L;
    private Set<PasswordExpiryNotificationRequestUserParameters> userParametersList;


}
