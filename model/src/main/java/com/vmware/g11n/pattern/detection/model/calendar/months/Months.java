/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.months;

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
public class Months {
    @JsonProperty("format")
    public MonthsFormat monthsFormat;
    @JsonProperty("stand-alone")
    public MonthsStandalone monthsStandalone;

    @JsonIgnore
    public List<String> getAllMonths() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(monthsFormat.getAllFormattedMonths());
        listToReturn.addAll(monthsStandalone.getAllStandaloneMonths());

        return listToReturn;
    }
}
