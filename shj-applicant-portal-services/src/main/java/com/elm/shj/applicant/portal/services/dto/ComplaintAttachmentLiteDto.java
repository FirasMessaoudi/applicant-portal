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

/**
 * Dto class for incident attachment domain.
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ComplaintAttachmentLiteDto implements Serializable {

    private static final long serialVersionUID = 8743710249950641326L;

    private long id;
    private String filePath;
    @JsonBackReference(value = "applicantComplaint")
    private ApplicantComplaintLiteDto applicantComplaint;
    private Date creationDate;
}
