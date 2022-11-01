/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.serviceData;

import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Builder(toBuilder = true)
@ToString
@Setter
public class ValidationResult {
    public boolean isLocalizedContent;
    public ValidatedPattern detectedPattern;
    public String language;
    public String input;
    public Map<SuggestionsType, String> suggestions;
    public Map<ErrorsType, String> errors;

}
