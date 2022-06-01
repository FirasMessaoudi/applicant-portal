/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the user supplication domain.
 *
 * @author r.chebbi
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSupplicationDto implements Serializable {


    private static final long serialVersionUID = -3222780724069191509L;
    private long id;
    private String digitalId;
    private String code;
    private String lang;
    private String label;
    private int totalSupplication;
    private int lastSupplicationNumber;
    private boolean Deleted;
    private Date creationDate;
}
