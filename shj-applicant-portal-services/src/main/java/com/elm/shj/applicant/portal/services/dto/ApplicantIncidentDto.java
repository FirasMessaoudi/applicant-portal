
/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for applicant incident domain.
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicantIncidentDto implements Serializable {

    private static final long serialVersionUID = -5654394816918782403L;

    private long id;
    @JsonBackReference(value = "applicantRitual")
    private ApplicantRitualDto applicantRitual;
    private String statusCode;
    private String referenceNumber;
    private String typeCode;
    private String description;
    private Double locationLat;
    private Double locationLng;
    private String resolutionComment;
    private String city;
    private String campNumber;
    private String crmTicketNumber;
    private List<IncidentAttachmentDto> incidentAttachments;
    private Date creationDate;
    private Date updateDate;
}
