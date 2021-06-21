/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the card status domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public class CardStatusLookupDto implements Serializable {

    private static final long serialVersionUID = -3108661600526285247L;

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
