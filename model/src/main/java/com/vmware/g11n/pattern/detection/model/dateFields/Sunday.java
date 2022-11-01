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
public class Sunday {
    public String displayName;
    @JsonProperty("relative-type--1")
    public String relativeLastSunday;
    @JsonProperty("relative-type-0")
    public String relativeThisSunday;
    @JsonProperty("relative-type-1")
    public String relativeNextSunday;

    @JsonIgnore
    public List<String> getAllRelativeSundays() {
        return Arrays.asList(displayName, relativeLastSunday, relativeThisSunday, relativeNextSunday);
    }
}
