/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant supplication domain.
 *
 * @author r.chebbi
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantSupplicationDto implements Serializable {


    private static final long serialVersionUID = -3222780724069191509L;
    private long id;
    private String digitalId;
    private String labelAr;
    private String labelEn;
    private int totalSupplication;
    private int lastSupplicationNumber;
    private boolean deleted;
    private Date creationDate;
}
