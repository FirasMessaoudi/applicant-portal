/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto for user notification category preference.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserNotificationCategoryPreferenceDto implements Serializable {

    private static final long serialVersionUID = -3674938432773107508L;

    private long id;
    private long userId;
    private String categoryCode;
    private boolean enabled;
    private Date creationDate;
    private Date updateDate;
}
