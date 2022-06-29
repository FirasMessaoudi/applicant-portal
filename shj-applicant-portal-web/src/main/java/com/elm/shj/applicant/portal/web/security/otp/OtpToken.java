/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.otp;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * OTP Token used for Spring Security authentication process based on OTP
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
public class OtpToken extends UsernamePasswordAuthenticationToken {
    private boolean otpRequired;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private int otpExpiryMinutes;
    private Long uin;


    public OtpToken(boolean otpRequired, int otpExpiryMinutes, final Object principal, String firstName, String lastName, String mobileNumber, String email) {
        super(principal, null);
        this.otpRequired = otpRequired;
        this.otpExpiryMinutes = otpExpiryMinutes;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public OtpToken(boolean otpRequired, int otpExpiryMinutes, final Object principal, String firstName, String lastName, String mobileNumber, String email, Long uin) {
        super(principal, null);
        this.otpRequired = otpRequired;
        this.otpExpiryMinutes = otpExpiryMinutes;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.uin = uin;
    }


    public boolean isOtpRequired() {
        return otpRequired;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }


    public int getOtpExpiryMinutes() {
        return otpExpiryMinutes;
    }

    public Long getUin() { return uin; }
}
