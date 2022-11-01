/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.dateFields;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class Day {
    public String displayName;
    @JsonProperty("relative-type--1")
    public String relativeYesterday;
    @JsonProperty("relative-type--2")
    public String relativeOtherDay;
    @JsonProperty("relative-type-0")
    public String relativeToday;
    @JsonProperty("relative-type-1")
    public String relativeTomorrow;
    @JsonProperty("relative-type-2")
    public String relativeDayAfter;

    @JsonIgnore
    public List<String> getAllRelativeDays() {
        return Arrays.asList(displayName, relativeYesterday, relativeToday, relativeTomorrow, relativeOtherDay, relativeDayAfter);
    }
}