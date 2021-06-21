/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the print request batch card domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrintRequestBatchCardDto implements Serializable {

    private static final long serialVersionUID = 3116078220007424027L;

    private long id;
    @JsonBackReference
    private PrintRequestBatchDto printRequestBatch;
    private ApplicantCardDto card;
    private Date creationDate;
}
