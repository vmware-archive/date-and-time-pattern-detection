/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.services;

import com.ibm.icu.text.SimpleDateFormat;
import com.vmware.g11n.pattern.detection.library.exceptions.DateTimePatternDetectionException;
import com.vmware.g11n.pattern.detection.library.data.Builders;
import com.vmware.g11n.pattern.detection.model.serviceData.*;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType.INVALID_TEMPORAL;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.NON_CONVERTABLE_COMPONENTS;
import static com.vmware.g11n.pattern.detection.library.utils.UserInputProcessors.verifyAndLoadLocale;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static java.util.Collections.disjoint;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ConvertorService {

    private final ValidatorService validatorService;
    private final CldrService cldrService;

    public ConvertorService() {
        validatorService = new ValidatorService();
        cldrService = new CldrService();
    }

    public ConversionResult convertLocalizeInput(String input, String sourceLocale, String targetLocale) {
        // Get the pattern and validation response for the source input. Verify if it's eligible for convert (full date and no errors)
        ValidationResult sourceInputValidation = validatorService.validateInput(input, sourceLocale);
        verifyInputEligibleForConvert(sourceInputValidation);

        Locale loadedSourceLocale = verifyAndLoadLocale(sourceLocale);
        Locale loadedTargetLocale = verifyAndLoadLocale(targetLocale);

        // If standard get corresponding pattern from standard CLDR formats. If not, use the same(source) pattern
        String targetPattern = getCorrespondingTargetPattern(sourceInputValidation, targetLocale);
        String targetLocalizedOutput = getTargetLocalizedOutput(sourceInputValidation, loadedSourceLocale, loadedTargetLocale, targetPattern);

        // Verify the new generated output using original endpoint - get info whether it's valid localized after conversion
        ValidationResult convertedOutputValidation = validatorService.validateInput(targetLocalizedOutput, targetLocale);

        // Build the suggestions and get response
        return buildConversionResponse(sourceInputValidation, convertedOutputValidation, loadedSourceLocale, loadedTargetLocale);
    }

    private void verifyInputEligibleForConvert(ValidationResult sourceInputValidation) {
        if (isNull(sourceInputValidation.detectedPattern)) {
            throw new DateTimePatternDetectionException(NON_CONVERTABLE_INPUT);
        }

        if (!sourceInputValidation.detectedPattern.isValidDate && sourceInputValidation.detectedPattern.localizedType.equals(INVALID_TEMPORAL)) {
            throw new DateTimePatternDetectionException(NON_CONVERTABLE_INPUT);
        }
    }

    private ConversionResult buildConversionResponse(ValidationResult sourceValidation,
                                                     ValidationResult targetValidation,
                                                     Locale sourceLocale, Locale targetLocale) {

        if (isNull(targetValidation.detectedPattern)) {
            targetValidation.setDetectedPattern(ValidatedPattern.builder()
                    .pattern(sourceValidation.detectedPattern.pattern)
                    .patternInfoMessage(NON_APPLICABLE_SOURCE_PATTERN)
                    .build());
        }

        Map<ErrorsType, String> errors = new HashMap<>(targetValidation.errors);
        Map<SuggestionsType, String> suggestions = generateSuggestions(sourceValidation, sourceLocale, targetLocale);

        return Builders.buildConversionResult(sourceValidation, targetValidation, errors, suggestions);

    }

    private String getCorrespondingTargetPattern(ValidationResult validationResult, String targetLocale) {
        if (validationResult.detectedPattern.isStandardFormat) {
            switch (validationResult.detectedPattern.localizedType) {
                case DATE -> {
                    return cldrService.getStandardCldrDatePatterns(targetLocale).getAllDateFormatsAsMap()
                            .get(validationResult.detectedPattern.cldrDataKeyName);
                }
                case TIME -> {
                    return cldrService.getStandardCldrTimePatterns(targetLocale).getAllTimeFormatsAsMap()
                            .get(validationResult.detectedPattern.cldrDataKeyName);
                }
                case DATE_TIME -> {
                    return cldrService.getStandardCldrDateTimePatterns(targetLocale)
                            .get(validationResult.detectedPattern.cldrDataKeyName);
                }
            }
        }
        return validationResult.detectedPattern.pattern;
    }

    private String getTargetLocalizedOutput(ValidationResult validationResult, Locale sourceLocale, Locale targetLocale, String targetPattern) {
        switch (validationResult.detectedPattern.localizedType) {
            case DATE -> {
                long sourceDateTimestamp = LocalDate.parse(validationResult.input,
                        ofPattern(validationResult.detectedPattern.pattern, sourceLocale)).toEpochDay();
                return LocalDate.ofEpochDay(sourceDateTimestamp).format(ofPattern(targetPattern, targetLocale));
            }
            case TIME -> {
                LocalTime parsedTime = LocalTime.parse(validationResult.input,
                        ofPattern(validationResult.detectedPattern.pattern, sourceLocale));
                try {
                    return ZonedDateTime.of(LocalDate.now(), parsedTime, ZoneId.systemDefault()).format(
                            DateTimeFormatter.ofPattern(targetPattern, targetLocale));
                } catch (IllegalArgumentException e) {
                    return new SimpleDateFormat(targetPattern, targetLocale).format(new Date(ZonedDateTime.of(LocalDate.now(), parsedTime, ZoneId.systemDefault()).toEpochSecond()));
                }
            }
            case DATE_TIME -> {
                try {
                    return ZonedDateTime.parse(validationResult.input,
                                    ofPattern(validationResult.detectedPattern.pattern, sourceLocale))
                            .format(ofPattern(targetPattern, targetLocale));
                } catch (DateTimeException exception) {
                    return LocalDateTime.parse(validationResult.input,
                                    ofPattern(validationResult.detectedPattern.pattern, sourceLocale))
                            .format(ofPattern(targetPattern, targetLocale));
                } catch (Exception exception) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(validationResult.detectedPattern.pattern, sourceLocale);
                    try {
                        return new SimpleDateFormat(targetPattern, targetLocale).format(simpleDateFormat.parse(validationResult.input));
                    } catch (ParseException ignored) {
                    }
                }
            }

            case ISO_TIME, ISO_DATE_TIME, ISO_DATE -> {
                return validationResult.input;
            }
        }
        return EMPTY;
    }

    private Map<SuggestionsType, String> generateSuggestions(ValidationResult sourceValidation,
                                                             Locale sourceLocale, Locale targetLocale) {
        Map<SuggestionsType, String> suggestions = new LinkedHashMap<>();

        if (!disjoint(asList(sourceLocale.getLanguage(), targetLocale.getLanguage()), ASIAN_LANGUAGES_LIST)
                && !sourceValidation.detectedPattern.isStandardFormat) {
            suggestions.put(ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION, ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION_SUGGESTION);
        }

        Matcher hardcodeComponentsMatcher = Pattern.compile("'(.*?)'").matcher(sourceValidation.detectedPattern.pattern);
        if (hardcodeComponentsMatcher.find()) {
            suggestions.put(NON_CONVERTABLE_COMPONENTS,
                    NON_CONVERTABLE_COMPONENTS_SUGGESTION + hardcodeComponentsMatcher.group());
        }

        return suggestions;
    }

}