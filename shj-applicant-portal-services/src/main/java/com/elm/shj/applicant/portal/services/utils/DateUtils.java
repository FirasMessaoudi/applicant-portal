/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * Date utility functions
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public class DateUtils {

    private DateUtils() {
        // prevent creation
    }

    /**
     * Transforms a gregorian date to hijri date based on Umm AlQura calendar
     *
     * @param date the date to transform
     * @return the resulted hijri date
     */
    public static long toHijri(Date date) {
        HijrahDate islamicDate = HijrahDate.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return islamicDate == null ? -1 : Long.parseLong(islamicDate.get(ChronoField.YEAR) + StringUtils.leftPad("" + islamicDate.get(ChronoField.MONTH_OF_YEAR), 2, "0") + StringUtils.leftPad("" + islamicDate.get(ChronoField.DAY_OF_MONTH), 2, "0"));
    }

    /**
     * Transforms a hijri date to gregorian date
     *
     * @param hijri the hijri date to transform
     * @return the resulted gregorian date
     */
    public static Date toGregorian(long hijri) {
        HijrahDate hijrahDate = null;
        String hijriStr = "" + hijri;
        if (StringUtils.length(hijriStr) == 8) {
            int year = Integer.parseInt(hijriStr.substring(0, 4));
            int month = Integer.parseInt(hijriStr.substring(4, 6));
            int day = Integer.parseInt(hijriStr.substring(6, 8));
            try {
                hijrahDate = HijrahDate.now()
                        .with(ChronoField.YEAR, year)
                        .with(ChronoField.MONTH_OF_YEAR, month)
                        .with(ChronoField.DAY_OF_MONTH, day);
            } catch (DateTimeException dte) {
                // ignore
            }
        }
        return hijrahDate == null ? null : Date.from(IsoChronology.INSTANCE.date(hijrahDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

}
