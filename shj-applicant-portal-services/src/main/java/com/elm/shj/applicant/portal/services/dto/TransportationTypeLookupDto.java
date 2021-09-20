/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the transportation type domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class TransportationTypeLookupDto implements Serializable {

    private static final long serialVersionUID = 4480800943995537857L;

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
