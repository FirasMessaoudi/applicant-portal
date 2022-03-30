/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the User Survey domain.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSurveyDto implements Serializable {

    private static final long serialVersionUID = -5627101457310315686L;
    private long id;

    String digitalId;

    private String surveyType;

    private Date creationDate;

}
