/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DateTimeFormats {
    @JsonProperty("short")
    public String shortened;
    public String medium;
    @JsonProperty("long")
    public String longFormat;
    public String full;
    public AppendItems appendItems;
    @JsonProperty("availableFormats")
    public Map<String, String> availableFormats;
    @JsonProperty("timeFormats")
    public Map<String, String> timeFormats;

    public List<String> getAllDateTimeFormats(){
        return Stream.of(shortened, medium, longFormat, full).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
