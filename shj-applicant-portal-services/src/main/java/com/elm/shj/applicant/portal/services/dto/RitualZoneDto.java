/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the ritual zone domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RitualZoneDto implements Serializable {

    private static final long serialVersionUID = -4162136915855923660L;

    private long id;
    private String code;
    private String lang;
    private String label;
    private long seasonId;
    private Date creationDate;
}
