/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.navigation;

/**
 * This class centralizes navigation urls.
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public class Navigation {

    // Authentication
    public static final String API_AUTH = "/api/auth";
    // Users
    public static final String API_USERS = "/api/users";
    // Dashboard
    public static final String API_DASHBOARD = "/api/dashboard";
    // Password Change
    public static final String API_USERS_CHANGE_PWRD = API_USERS + "/change-password";
    // Password Reset
    public static final String API_USERS_RESET_PWRD = API_USERS + "/reset-password";
    // Lookup
    public static final String API_LOOKUP = "/api/lookup";
    // Registration
    public static final String API_REGISTRATION = "/api/register";
    // Integration web service call
    public static final String API_INTEGRATION = "/api/ws";

    private Navigation() {
        // private constructor to prevent construction
    }

}
