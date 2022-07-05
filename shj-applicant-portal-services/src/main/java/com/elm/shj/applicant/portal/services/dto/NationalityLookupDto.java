/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the country domain.
 *
 * @author ahmad flaifel
 * @since 1.2.4
 */
@Data
@NoArgsConstructor
public class NationalityLookupDto implements Serializable {

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
