/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the role authority domain.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public class RoleAuthorityDto implements Serializable {

    private static final long serialVersionUID = 7882374436190071939L;

    private long id;
    @JsonBackReference
    private RoleDto role;
    private AuthorityLookupDto authority;
    private Date creationDate;
}
