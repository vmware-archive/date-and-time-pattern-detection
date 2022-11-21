/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DateFormats {
    @JsonProperty("short")
    public String shortened;
    public String medium;
    @JsonProperty("long")
    public String longFormat;
    public String full;

    @JsonIgnore
    public Map<String, String> getAllDateFormatsAsMap() {
        return ImmutableMap.of(
                "short", shortened,
                "medium", medium,
                "long", longFormat,
                "full", full
        );
    }

    @Override
    public String toString() {
        return "[" + "short: '" + shortened + "', " +
                "medium: '" + medium + "', " +
                "long: '" + longFormat + "', " +
                "full: '" + full + "']";
    }
}
