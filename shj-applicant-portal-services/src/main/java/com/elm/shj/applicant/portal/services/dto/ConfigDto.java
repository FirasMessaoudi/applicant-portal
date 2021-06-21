/*
 *  Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the Config domain.
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class ConfigDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String confKey;
    private String confValue;
    private Date creationDate;
    private Date updateDate;
}
