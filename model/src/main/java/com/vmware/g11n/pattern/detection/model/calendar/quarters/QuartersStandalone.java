/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.quarters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//I've removed the short version as it's basically numbers and cannot be detected in any way
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class QuartersStandalone {
    public Map<String, String> abbreviated;
    public Map<String, String> wide;

    @JsonIgnore
    public List<String> getAllStandaloneQuarters() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(abbreviated.values());
        listToReturn.addAll(wide.values());

        return listToReturn;
    }
}