/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

import com.elm.shj.applicant.portal.services.dto.ApplicantIncidentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Class to group incident and attachment
 *
 * @author f.messaoudi
 * @since 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class IncidentAttachmentVo {
    private ApplicantIncidentDto applicantIncidentDto;
    private MultipartFile attachment;
}
