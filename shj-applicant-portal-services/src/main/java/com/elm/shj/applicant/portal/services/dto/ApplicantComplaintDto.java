
/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for applicant incident domain.
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicantComplaintDto implements Serializable {

    private static final long serialVersionUID = -5654394816918782403L;

    private long id;
    @JsonBackReference(value = "applicantRitual")
    private ApplicantRitualDto applicantRitual;
    private String statusCode;
    private String referenceNumber;
    private String typeCode;
    @NotNull(message = "validation.data.constraints.msg.20001")
    @Size(min = 1, max = 500)
    private String description;
    private Double locationLat;
    private Double locationLng;
    @JsonIgnore
    private String resolutionComment;
    private String city;
    private String campNumber;
    @JsonIgnore
    private String crmTicketNumber;
    private ComplaintAttachmentDto complaintAttachment;
    private Date creationDate;
    private Date updateDate;
}
