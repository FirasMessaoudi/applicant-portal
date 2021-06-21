/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the print request domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrintRequestDto implements Serializable {

    private static final long serialVersionUID = -5860349238491983581L;

    private long id;
    private String referenceNumber;
    private String statusCode;
    private List<PrintRequestCardDto> printRequestCards = new ArrayList<>();
    private List<PrintRequestBatchDto> printRequestBatches = new ArrayList<>();
    private Date creationDate;
    private Date updateDate;
    private Date confirmationDate;
}
