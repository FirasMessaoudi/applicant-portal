/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the sha_user_notification database table.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Entity
@Table(name = "sha_user_notification")
@NamedQuery(name = "JpaUserNotification.findAll", query = "SELECT j FROM JpaUserNotification j")
@Data
@NoArgsConstructor
public class JpaUserNotification implements Serializable {

    private static final long serialVersionUID = 5278838751694591343L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name = "notification_template_name_code")
    private long notificationTemplateNameCode;

    @Column(name = "user_id")
    private long userId;


    @Column(name = "resolved_body")
    private String resolvedBody;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private JpaUserNotificationStatusLookup notificationStatus;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @PrePersist
    public void prePersist() {
        creationDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = new Date();
    }
}

