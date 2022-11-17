/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.data;

import com.vmware.g11n.pattern.detection.model.serviceData.*;

import java.util.Map;

public class Builders {

    public static ValidationResult buildValidationResult(String input, String language, boolean isLocalizedContent, ValidatedPattern detectedPattern) {
        return ValidationResult.builder()
                .input(input)
                .language(language)
                .isLocalizedContent(isLocalizedContent)
                .detectedPattern(detectedPattern)
                .build();
    }

    public static ConversionResult buildConversionResult(ValidationResult sourceValidation,
                                                         ValidationResult targetValidation,
                                                         Map<ErrorsType, String> errors,
                                                         Map<SuggestionsType, String> suggestions) {
        return ConversionResult.builder()
                .isSourceContentLocalized(sourceValidation.isLocalizedContent)
                .isTargetContentLocalized(targetValidation.isLocalizedContent)
                .sourcePattern(sourceValidation.detectedPattern)
                .targetPattern(targetValidation.detectedPattern)
                .targetLocalizedOutput(targetValidation.input)
                .sourceLocalizedInput(sourceValidation.input)
                .sourceLanguage(sourceValidation.language)
                .targetLanguage(targetValidation.language)
                .conversionErrors(errors)
                .conversionSuggestions(suggestions)
                .build();
    }
}
