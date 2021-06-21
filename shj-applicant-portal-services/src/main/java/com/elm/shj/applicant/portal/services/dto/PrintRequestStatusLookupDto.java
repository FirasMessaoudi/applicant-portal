/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the print request status domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class PrintRequestStatusLookupDto implements Serializable {

    private static final long serialVersionUID = -5285406270559206854L;

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
