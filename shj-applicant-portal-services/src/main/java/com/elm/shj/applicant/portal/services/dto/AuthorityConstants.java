/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

/**
 * Class for authorities constants.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
public final class AuthorityConstants {

    public static final String ADMIN_DASHBOARD = "ADMIN_DASHBOARD";
    public static final String USER_MANAGEMENT = "USER_MANAGEMENT";
    public static final String ADD_USER = "ADD_USER";
    public static final String EDIT_USER = "EDIT_USER";
    public static final String CHANGE_USER_STATUS = "CHANGE_USER_STATUS";
    public static final String RESET_PASSWORD = "RESET_PASSWORD";
    public static final String RESET_USER_PASSWORD = "RESET_USER_PASSWORD";

    private AuthorityConstants() {
        // creating instances is not allowed
    }
}
