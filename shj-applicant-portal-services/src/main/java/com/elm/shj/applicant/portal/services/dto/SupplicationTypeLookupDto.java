package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplicationTypeLookupDto implements Serializable{

    private long id;
    private String code;
    private String lang;
    private String label;
    private Date creationDate;

}
