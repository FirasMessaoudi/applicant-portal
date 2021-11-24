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
 * Dto class for the Religious Occasions Day domain.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ReligiousOccasionsDayLookupDto implements Serializable {

    private static final long serialVersionUID = -6003649867994722938L;

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
