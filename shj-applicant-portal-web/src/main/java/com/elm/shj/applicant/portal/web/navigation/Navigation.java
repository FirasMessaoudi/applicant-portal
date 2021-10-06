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

    // Logout
    public static final String API_AUTH_LOGOUT = API_AUTH + "/logout";
    // Password Reset
    public static final String API_USERS_RESET_PWRD = API_USERS + "/reset-password";
    // Lookup
    public static final String API_LOOKUP = "/api/lookup";
    // Registration
    public static final String API_REGISTRATION = "/api/register";
    // Integration web service call
    public static final String API_INTEGRATION = "/api/ws";

    // Integration registration web service call
    public static final String API_INTEGRATION_REGISTRATION = "/api/ws/register";

    // Users
    public static final String API_INTEGRATION_USERS = "/api/ws/users";

    public static final String API_INTEGRATION_USERS_CHANGE_PWRD = API_INTEGRATION_USERS + "/change-password";

    // Integration Authentication
    public static final String API_INTEGRATION_AUTH = "/api/ws/auth";

    public static final String API_INTEGRATION_AUTH_LOGOUT = API_INTEGRATION_AUTH + "/logout";

    private Navigation() {
        // private constructor to prevent construction
    }

}
