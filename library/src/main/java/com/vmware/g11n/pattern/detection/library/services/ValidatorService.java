/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.services;

import com.vmware.g11n.pattern.detection.library.patterns.PatternBuilders;
import com.vmware.g11n.pattern.detection.model.CldrData;
import com.vmware.g11n.pattern.detection.model.serviceData.*;

import java.util.*;

import static com.vmware.g11n.pattern.detection.library.data.Builders.buildValidationResult;
import static com.vmware.g11n.pattern.detection.library.utils.CldrProcessors.getValidatedPatternFromCldrFormats;
import static com.vmware.g11n.pattern.detection.library.utils.IsoProcessors.detectPatternFromPredefinedIsoFormats;
import static com.vmware.g11n.pattern.detection.library.utils.ResourceLoaders.loadCldrData;
import static com.vmware.g11n.pattern.detection.library.utils.UserInputProcessors.*;

public class ValidatorService {

    String[] supportedLocales;

    public static Map<SuggestionsType, String> suggestions = new HashMap<>();
    public static Map<ErrorsType, String> errors = new HashMap<>();
    private PatternBuilders patternBuilders;

    public ValidatorService() {
        patternBuilders = new PatternBuilders();
    }

    public ValidationResult validateInput(String input, String locale) {
        // First process the provided locale
        Locale providedLocale = verifyAndLoadLocale(locale);

        // Trim the input if needed
        input = trimInput(input);

        // Load the needed CLDR data
        CldrData cldrData = loadCldrData(providedLocale);

        // Try to match the input as a whole to a CLDR pattern
        ValidatedPattern detectedPattern = getValidatedPatternFromCldrFormats(cldrData, input, providedLocale);
        if (!detectedPattern.pattern.isEmpty()) {
            return buildValidationResult(input, providedLocale.getDisplayName(), true, detectedPattern)
                    .toBuilder().suggestions(generateSuggestions()).errors(getErrors()).build();
        }

        // Try to match the input as a whole to ISO locale-independent patterns
        detectedPattern = detectPatternFromPredefinedIsoFormats(input);
        if (!detectedPattern.pattern.isEmpty()) {
            return buildValidationResult(input, providedLocale.getDisplayName(), true, detectedPattern)
                    .toBuilder().suggestions(generateSuggestions()).errors(getErrors()).build();
        }

        // Split the input to components and modify the user input if needed
        LinkedList<String> listOfDateElements = splitInputString(input, cldrData);

        // Generate pattern by traversing each component and mapping it to a skeleton pattern
        ValidatedPattern generatedPattern = patternBuilders.generatePatternFromComponents(listOfDateElements, providedLocale, cldrData);
        return buildValidationResult(input, providedLocale.getDisplayName(), generatedPattern != null, generatedPattern)
                .toBuilder().suggestions(generateSuggestions()).errors(getErrors()).build();
    }

    public List<ValidationResult> validateInput(String input) {
        List<ValidationResult> responseList = new ArrayList<>();

        for (String potentialLocale : supportedLocales) {
            ValidationResult responseForCurrentLocale = validateInput(input, potentialLocale);
            if (responseForCurrentLocale.isLocalizedContent) {
                responseList.add(responseForCurrentLocale);
            }
        }

        return responseList;
    }

    public List<ValidationResult> bulkValidateLocalizedInput(List<BatchValidationRequest> batchValidationRequests) {
        List<ValidationResult> validationResults = new LinkedList<>();

        batchValidationRequests.forEach(
                request -> request.getInputs().forEach(
                        input -> validationResults.add(validateInput(input, request.getLocale()))
                )
        );

        return validationResults;
    }

    private Map<SuggestionsType, String> generateSuggestions() {
        Map<SuggestionsType, String> suggestionsToReturn = new HashMap<>(suggestions);

        // Remove suggestions for non-standard date/time if suggestion for dateTime is present.
        if (suggestionsToReturn.containsKey(SuggestionsType.NON_STANDARD_DATE_TIME)) {
            suggestionsToReturn.remove(SuggestionsType.NON_STANDARD_TIME);
            suggestionsToReturn.remove(SuggestionsType.NON_STANDARD_DATE);
        }

        suggestions.clear();
        return suggestionsToReturn;
    }

    public static Map<ErrorsType, String> getErrors() {
        Map<ErrorsType, String> errorsToReturn = new HashMap<>(errors);
        errors.clear();
        return errorsToReturn;
    }
}