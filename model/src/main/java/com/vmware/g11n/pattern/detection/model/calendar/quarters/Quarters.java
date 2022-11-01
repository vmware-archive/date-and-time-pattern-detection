/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.quarters;

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
public class Quarters {
    @JsonProperty("format")
    public QuartersFormat quartersFormat;
    @JsonProperty("stand-alone")
    public QuartersStandalone quartersStandalone;

    @JsonIgnore
    public List<String> getAllQuarters() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(quartersFormat.getAllFormattedQuarters());
        listToReturn.addAll(quartersStandalone.getAllStandaloneQuarters());

        return listToReturn;
    }
}