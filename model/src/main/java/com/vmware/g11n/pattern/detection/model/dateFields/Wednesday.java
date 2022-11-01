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
public class Wednesday {
    public String displayName;
    @JsonProperty("relative-type--1")
    public String relativeLastWednesday;
    @JsonProperty("relative-type-0")
    public String relativeThisWednesday;
    @JsonProperty("relative-type-1")
    public String relativeNextWednesday;

    @JsonIgnore
    public List<String> getAllRelativeWednesday() {
        return Arrays.asList(displayName, relativeLastWednesday, relativeThisWednesday, relativeNextWednesday);
    }
}
