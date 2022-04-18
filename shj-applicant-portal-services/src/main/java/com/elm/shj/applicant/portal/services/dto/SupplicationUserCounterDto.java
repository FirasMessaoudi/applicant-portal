/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
/**
 * Dto class for the  supplication user counter domain.
 *
 * @author r.chebbi
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplicationUserCounterDto implements Serializable {
    private static final long serialVersionUID = -7365367821511565081L;
    private long id;
    private String code;
    private String digitalId;
    private boolean suggested;
    private int supplicationTotalCount;
    private int supplicationLastCount;
    private Date creationDate;
    private Date updateDate;
}
