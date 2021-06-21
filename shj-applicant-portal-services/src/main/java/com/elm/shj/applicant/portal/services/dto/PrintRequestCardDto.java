/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the print request card domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrintRequestCardDto implements Serializable {

    private static final long serialVersionUID = -5235991770935109142L;

    private long id;
    @JsonBackReference
    private PrintRequestDto printRequest;
    private ApplicantCardDto card;
    private Date creationDate;
}
