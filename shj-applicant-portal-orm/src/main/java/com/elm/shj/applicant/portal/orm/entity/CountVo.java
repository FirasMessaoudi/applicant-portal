/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value object class for returned count.
 * Label could represent hour, day of week or day of month
 *
 * @author ahmad flaifel
 * @since 1.4.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountVo {

    private String label;
    private int labelNumber;
    private long count;
}
