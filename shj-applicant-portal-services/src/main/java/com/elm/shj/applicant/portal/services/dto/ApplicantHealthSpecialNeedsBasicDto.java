/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant health special needs domain.
 *
 * @author f.messaoudi
 * @since 1.3.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantHealthSpecialNeedsBasicDto implements Serializable {


    private static final long serialVersionUID = -6478100142867842513L;
    private long id;
    @JsonBackReference
    private ApplicantHealthBasicDto applicantHealth;
    private String specialNeedTypeCode;
    private Date creationDate;
}
