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
public class RelativeData {
    public String displayName;
    @JsonProperty("relative-type--1")
    public String valueRelativeLast;
    @JsonProperty("relative-type-0")
    public String valueRelativeThis;
    @JsonProperty("relative-type-1")
    public String valueRelativeNext;

    @JsonIgnore
    public List<String> getAllRelativeData() {
        return Arrays.asList(displayName, valueRelativeLast, valueRelativeThis, valueRelativeNext);
    }
}