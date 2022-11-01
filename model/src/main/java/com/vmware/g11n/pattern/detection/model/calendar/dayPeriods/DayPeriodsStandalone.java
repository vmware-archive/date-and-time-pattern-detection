/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.dayPeriods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class DayPeriodsStandalone {
    public Map<String, String> narrow;
    public Map<String, String> abbreviated;
    public Map<String, String> wide;

    @JsonIgnore
    public List<String> getAllStandaloneDayPeriods() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(narrow.values());
        listToReturn.addAll(abbreviated.values());
        listToReturn.addAll(wide.values());

        return listToReturn;
    }
}
