/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
/**
 * Dto class for the suggested supplication domain.
 *
 * @author r.chebbi
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestedSupplicationLookupDto implements Serializable {

    private static final long serialVersionUID = -4310171571770951500L;
    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;
}
