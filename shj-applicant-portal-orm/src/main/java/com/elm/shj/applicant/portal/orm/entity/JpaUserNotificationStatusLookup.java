/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the sha_user_notification_status_lk database table.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Entity
@Table(name = "sha_user_notification_status_lk")
@NamedQuery(name = "JpaUserNotificationStatusLookup.findAll", query = "SELECT j FROM JpaUserNotificationStatusLookup j")
public class JpaUserNotificationStatusLookup extends JpaLocalizedLookup {

    private static final long serialVersionUID = 91615181060572919L;
}
