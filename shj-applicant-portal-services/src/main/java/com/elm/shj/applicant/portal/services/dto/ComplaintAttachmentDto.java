/*
 * Copyright (c) 2022 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for complaint attachment domain.
 *
 * @author salzoubi
 * @since 1.2.4
 */
@Getter
@Setter
@NoArgsConstructor
public class ComplaintAttachmentDto implements Serializable {

    private static final long serialVersionUID = 8743710249950641326L;

    private long id;
    private String filePath;
    @JsonBackReference(value = "applicantComplaint")
    private ApplicantComplaintDto applicantComplaint;
    private Date creationDate;
}
