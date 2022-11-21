/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateTimeFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dayPeriods.DayPeriods;
import com.vmware.g11n.pattern.detection.model.calendar.days.Days;
import com.vmware.g11n.pattern.detection.model.calendar.eras.Eras;
import com.vmware.g11n.pattern.detection.model.calendar.months.Months;
import com.vmware.g11n.pattern.detection.model.calendar.quarters.Quarters;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class GregorianCalendar {
    public Months months;
    public Days days;
    public Quarters quarters;
    public DayPeriods dayPeriods;
    public Eras eras;
    public DateFormats dateFormats;
    public TimeFormats timeFormats;
    public DateTimeFormats dateTimeFormats;

    public Map<String, String> getStandardDateTimeMap(){
        Map<String, String> standardDateTimeFormats = new HashMap<>();

        standardDateTimeFormats.put("short", dateTimeFormats.shortened
                .replace("{1}", dateFormats.shortened).replace("{0}", timeFormats.shortened));
        standardDateTimeFormats.put("medium", dateTimeFormats.medium
                .replace("{1}", dateFormats.medium).replace("{0}", timeFormats.medium));
        standardDateTimeFormats.put("long", dateTimeFormats.longFormat
                .replace("{1}", dateFormats.longFormat).replace("{0}", timeFormats.longFormat));
        standardDateTimeFormats.put("full", dateTimeFormats.full
                .replace("{1}", dateFormats.full).replace("{0}", timeFormats.full));

        return standardDateTimeFormats;
    }

    @JsonIgnore
    public String getStandardDateTimeMapToString() {
        return "[" + "short: '" + getStandardDateTimeMap().get("short") + "', " +
                "medium: '" + getStandardDateTimeMap().get("medium") + "', " +
                "long: '" + getStandardDateTimeMap().get("long") + "', " +
                "full: '" + getStandardDateTimeMap().get("full") + "']";
    }
}
