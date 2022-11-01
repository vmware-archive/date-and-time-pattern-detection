/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.timezones;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimezoneNames {
    String hourFormat;
    String gmtFormat;
    String gmtZeroFormat;
    String regionFormat;
    @JsonProperty(value = "regionFormat-type-daylight")
    String regionFormatDaylightType;
    @JsonProperty(value = "regionFormat-type-standard")
    String regionFormatStandardType;
    String fallbackFormat;
    @JsonProperty(value = "zone")
    Map<String, Map<String, SubZone>> zones;
    @JsonProperty(value = "metazone")
    Map<String, MetaZoneFields> metazone;
}

