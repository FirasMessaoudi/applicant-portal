/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.integration;

import lombok.Builder;
import lombok.Data;

/**
 * Body of login request
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Data
@Builder
public class LoginRequestVo {

    private String username;
    private String password;

}
