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
public class ConversionResult {
    public boolean isSourceContentLocalized;
    public boolean isTargetContentLocalized;
    public String sourceLocalizedInput;
    public String targetLocalizedOutput;
    public ValidatedPattern sourcePattern;
    public ValidatedPattern targetPattern;
    public String sourceLanguage;
    public String targetLanguage;
    public Map<SuggestionsType, String> conversionSuggestions;
    public Map<ErrorsType, String> conversionErrors;
}
