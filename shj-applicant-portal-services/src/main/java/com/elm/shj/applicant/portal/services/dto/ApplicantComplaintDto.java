/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for applicant complaint domain.
 *
 * @author salzoubi
 * @since 1.2.4
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicantComplaintDto implements Serializable {

    private static final long serialVersionUID = 5885377938235938843L;

    private long id;
    private ApplicantRitualDto applicantRitual;
    private String statusCode;
    private String referenceNumber;
    private String typeCode;
    @NotNull(message = "validation.data.constraints.msg.20001")
    @Size(min = 1, max = 500)
    private String description;
    private Double locationLat;
    private Double locationLng;
    private String resolutionComment;
    private List<ComplaintAttachmentDto> complaintAttachments;
    private Date creationDate;
    private Date updateDate;
    private String areaCode;
    private String city;
    private String campNumber;
    private String crmTicketNumber;

}
