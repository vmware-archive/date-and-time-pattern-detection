/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TimeFormats {
    @JsonProperty("short")
    public String shortened;
    public String medium;
    @JsonProperty("long")
    public String longFormat;
    public String full;

    @JsonIgnore
    public Map<String, String> getAllTimeFormatsAsMap() {
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
