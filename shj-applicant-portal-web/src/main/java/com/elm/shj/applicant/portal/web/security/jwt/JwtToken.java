/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.security.jwt;

import com.elm.shj.applicant.portal.services.dto.UserRoleDto;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

/**
 * JWT Token used for Spring Security authentication process
 *
 * @author Aymen Dhaoui <adhaoui@elm.sa>
 * @since 1.0.0
 */
public class JwtToken extends UsernamePasswordAuthenticationToken {
    private String token;
    private long tokenExpirationDate;
    private boolean passwordExpired;
    private Collection<GrantedAuthority> authorities;
    private Set<UserRoleDto> userRoles;
    private String firstName;
    private String lastName;
    private long id;

    public JwtToken(final String token, final Object principal,
                    final Collection<? extends GrantedAuthority> grantedAuthorities,
                    boolean passwordExpired) {
        super(principal, token, grantedAuthorities);
        this.authorities = (Collection<GrantedAuthority>)grantedAuthorities;
        this.token = token;
        this.passwordExpired = passwordExpired;
    }

    public JwtToken(final String token, final Object principal,
                    final Collection<? extends GrantedAuthority> grantedAuthorities,
                    boolean passwordExpired, String firstName, String lastName, long id, final Set<UserRoleDto> userRoles) {
        super(principal, token, grantedAuthorities);
        this.authorities = (Collection<GrantedAuthority>)grantedAuthorities;
        this.token = token;
        this.passwordExpired = passwordExpired;
        this.userRoles = userRoles;
        // TODO: check why this line of code is needed.
        this.userRoles.forEach(userRoleDto -> userRoleDto.getRole().setRoleAuthorities(null));
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setTokenExpirationDate(long tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public long getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public boolean isPasswordExpired() { return passwordExpired; }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Set<UserRoleDto> getUserRoles() {
        return this.userRoles;
    }

    public long getId() {
        return id;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtToken)) return false;
        if (!super.equals(o)) return false;
        JwtToken jwtToken = (JwtToken) o;
        return getTokenExpirationDate() == jwtToken.getTokenExpirationDate() &&
                Objects.equal(getToken(), jwtToken.getToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getToken(), getTokenExpirationDate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
