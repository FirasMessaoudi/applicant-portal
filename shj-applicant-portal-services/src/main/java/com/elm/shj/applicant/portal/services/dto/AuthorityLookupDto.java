/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Dto class for the authority domain.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public class AuthorityLookupDto implements Serializable {

    private static final long serialVersionUID = 4794323197461223130L;

    private long id;
    private String labelAr;
    private String labelEn;
    private String code;
    private Date creationDate;
    private Long parentId;
    @JsonBackReference
    @ToString.Exclude
    private AuthorityLookupDto parent;
    @EqualsAndHashCode.Exclude
    private Set<AuthorityLookupDto> children;
}
