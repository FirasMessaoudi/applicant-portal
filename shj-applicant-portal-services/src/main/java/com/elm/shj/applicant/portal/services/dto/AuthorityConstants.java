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
    public static final String DELETE_USER = "DELETE_USER";
    public static final String ROLE_MANAGEMENT = "ROLE_MANAGEMENT";
    public static final String ADD_ROLE = "ADD_ROLE";
    public static final String EDIT_ROLE = "EDIT_ROLE";
    public static final String DELETE_ROLE = "DELETE_ROLE";
    public static final String CHANGE_ROLE_STATUS = "CHANGE_ROLE_STATUS";
    public static final String RESET_USER_PASSWORD = "RESET_USER_PASSWORD";

    //Printing Request Management
    public static final String PRINTING_REQUEST_MANAGEMENT = "PRINTING_REQUEST_MANAGEMENT";
    public static final String VIEW_PRINTING_REQUEST_DETAILS = "VIEW_PRINTING_REQUEST_DETAILS";
    public static final String ADD_PRINTING_REQUEST = "ADD_PRINTING_REQUEST";

    //Card Management
    public static final String CARD_MANAGEMENT = "CARD_MANAGEMENT";
    public static final String VIEW_CARD_DETAILS = "VIEW_CARD_DETAILS";
    public static final String UPDATE_CARD = "UPDATE_CARD";
    public static final String ACTIVATE_CARD = "ACTIVATE_CARD";
    public static final String CANCEL_CARD = "CANCEL_CARD";
    public static final String SUSPEND_CARD = "SUSPEND_CARD";
    public static final String REISSUE_CARD = "REISSUE_CARD";
    public static final String ADD_CARD = "ADD_CARD";

    //Upload Data Management
    public static final String MANAGE_REQUESTS = "MANAGE_REQUESTS";
    public static final String VIEW_REQUEST_DETAILS = "VIEW_REQUEST_DETAILS";
    public static final String CREATE_NEW_REQUEST = "CREATE_NEW_REQUEST";

    private AuthorityConstants() {
        // creating instances is not allowed
    }
}
