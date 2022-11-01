/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.eras;

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
public class Eras {
    public Map<String, String> eraNames;
    public Map<String, String> eraAbbr;
    public Map<String, String> eraNarrow;

    @JsonIgnore
    public List<String> getAllEras() {
        List<String> listToReturn = new ArrayList<>();

        listToReturn.addAll(eraNames.values());
        listToReturn.addAll(eraAbbr.values());
        listToReturn.addAll(eraNarrow.values());

        return listToReturn;
    }
}
