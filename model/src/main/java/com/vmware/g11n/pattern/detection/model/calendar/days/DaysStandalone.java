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
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class DaysStandalone {
    public Map<String, String> narrow;
    public Map<String, String> abbreviated;
    public Map<String, String> wide;
    @JsonProperty("short")
    public Map<String, String> shortened;

    @JsonIgnore
    public List<String> getAllStandaloneDays() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(narrow.values());
        listToReturn.addAll(abbreviated.values());
        listToReturn.addAll(wide.values());
        listToReturn.addAll(shortened.values());

        return listToReturn;
    }
}