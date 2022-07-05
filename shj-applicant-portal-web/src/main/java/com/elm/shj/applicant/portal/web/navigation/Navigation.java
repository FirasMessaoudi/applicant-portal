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
    // Lookup Integration
    public static final String API_INTEGRATION_LOOKUP = "/api/ws/lookup";
    // Registration
    public static final String API_REGISTRATION = "/api/register";
    // Integration web service call
    public static final String API_INTEGRATION = "/api/ws";
    // Integration registration web service call
    public static final String API_INTEGRATION_REGISTRATION = "/api/ws/register";
    // Users
    public static final String API_INTEGRATION_USERS = "/api/ws/users";
    // Integration Authentication
    public static final String API_INTEGRATION_AUTH = "/api/ws/auth";
    // User Notification
    public static final String API_NOTIFICATION = "/api/notification";
    // Applicant
    public static final String API_INTEGRATION_APPLICANT = "/api/ws/applicant";
    // Configuration web service call
    public static final String API_CONFIGURATION = "/api/ws/configuration";
    public static final String API_INTEGRATION_USERS_CHANGE_PWRD = API_INTEGRATION_USERS + "/change-password";
    public static final String API_INTEGRATION_AUTH_LOGOUT = API_INTEGRATION_AUTH + "/logout";
    // Logout
    public static final String API_AUTH_LOGOUT = API_AUTH + "/logout";
    // Incidents Integration
    public static final String API_INTEGRATION_INCIDENTS = "/api/ws/incidents";
    // Incidents
    public static final String API_INCIDENTS = "/api/incidents";
    // Complaints Integration
    public static final String API_INTEGRATION_COMPLAINTS = "/api/ws/complaints";
    // Complaints
    public static final String API_COMPLAINTS = "/api/complaints";
    // Chat contacts
    public static final String API_INTEGRATION_CHAT_CONTACTS = "/api/ws/chat-contact";
    public static final String API_INTEGRATION_SURVEY = "/api/ws/survey";
    public static final String API_INTEGRATION_SUPPLICATION= "/api/ws/supplications";
    public static final String API_INTEGRATION_ROSARY = "/api/ws/rosary";
    public static final String API_SURVEY = "/api/survey";
    // chatbot Integration
    public static final String API_INTEGRATION_CHATBOT = "/api/ws/chatbot";

    private Navigation() {
        // private constructor to prevent construction
    }

}
