/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the housing type domain.
 *
 * @author Ahmed Elsayed
 * @since 1.1.0
 */
@NoArgsConstructor
@Data
public class HousingTypeLookupDto implements Serializable {

    private static final long serialVersionUID = -2728154493925509543L;

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
