/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the company ritual step domain.
 *
 * @author salzoubi
 * @since 1.1.0
 */
@NoArgsConstructor
@Data
public class CompanyRitualStepLookupDto implements Serializable {


    private static final long serialVersionUID = -3969187209097372542L;
    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
