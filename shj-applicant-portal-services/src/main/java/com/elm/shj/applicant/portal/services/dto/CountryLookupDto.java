/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the country domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CountryLookupDto implements Serializable {

    private static final long serialVersionUID = -3015368529250122708L;

    private long id;
    private String code;
    private String countryNamePrefix;
    private String lang;
    private String label;
    private String countryPhonePrefix;
    private Date creationDate;
}
