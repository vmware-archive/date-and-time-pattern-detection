/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.days;

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
public class Days {

    @JsonProperty("format")
    public DaysFormat daysFormat;
    @JsonProperty("stand-alone")
    public DaysStandalone daysStandalone;

    @JsonIgnore
    public List<String> getAllDays() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(daysFormat.getAllFormattedDays());
        listToReturn.addAll(daysStandalone.getAllStandaloneDays());

        return listToReturn;
    }
}