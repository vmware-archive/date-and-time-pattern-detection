/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.serviceData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidatedPattern {
    public String pattern;
    @JsonIgnore
    public LocalizedType localizedType;
    @JsonIgnore
    public String cldrDataKeyName;
    public boolean isStandardFormat;
    public boolean isValidDate;
    public String patternInfoMessage;
}
