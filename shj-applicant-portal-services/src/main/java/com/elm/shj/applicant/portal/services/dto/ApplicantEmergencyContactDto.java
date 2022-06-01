/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

/**
 * Dto class for the user emergency contact.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantEmergencyContactDto {
    private String emergencyContactName;
    private String emergencyContactMobileNumber;
}
