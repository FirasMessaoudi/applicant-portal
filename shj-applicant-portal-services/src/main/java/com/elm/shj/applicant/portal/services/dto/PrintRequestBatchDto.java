/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the print request batch domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrintRequestBatchDto implements Serializable {

    private static final long serialVersionUID = -2689417211511498736L;

    private long id;
    @JsonBackReference
    private PrintRequestDto printRequest;
    private int sequenceNumber;
    private String batchTypeCodes;
    private String batchTypeValues;
    private List<PrintRequestBatchCardDto> printRequestBatchCards;
    private Date creationDate;
}
