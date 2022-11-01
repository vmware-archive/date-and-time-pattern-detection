/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.dayPeriods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class DayPeriods {
    @JsonProperty("format")
    public DayPeriodsFormat dayPeriodsFormat;
    @JsonProperty("stand-alone")
    public DayPeriodsStandalone dayPeriodsStandalone;

    @JsonIgnore
    public List<String> getAllDayPeriods() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(dayPeriodsFormat.getAllFormattedDayPeriods());
        listToReturn.addAll(dayPeriodsStandalone.getAllStandaloneDayPeriods());

        return listToReturn;
    }
}