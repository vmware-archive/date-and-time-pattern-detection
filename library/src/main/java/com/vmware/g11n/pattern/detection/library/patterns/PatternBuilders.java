/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.patterns;

import com.google.common.collect.ImmutableMap;
import com.vmware.g11n.pattern.detection.library.services.ValidatorService;
import com.vmware.g11n.pattern.detection.library.data.CldrMappers;
import com.vmware.g11n.pattern.detection.model.CldrData;
import com.vmware.g11n.pattern.detection.model.dateFields.DateFields;
import com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidatedPattern;
import com.vmware.g11n.pattern.detection.model.timezones.TimezoneNames;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.vmware.g11n.pattern.detection.library.data.CldrMappers.getAllSubsidiaryElements;
import static com.vmware.g11n.pattern.detection.library.data.CldrMappers.getPatternFromDateField;
import static com.vmware.g11n.pattern.detection.library.data.DateFieldSymbols.*;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static com.vmware.g11n.pattern.detection.model.serviceData.ErrorsType.UNDETECTED_COMPONENTS;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.INCORRECT_FORMAT_SEPARATOR;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.RELATIVE_TIME;
import static com.vmware.g11n.pattern.detection.library.patterns.PatternValidators.isValidPattern;
import static com.vmware.g11n.pattern.detection.library.patterns.PatternValidators.verifyAndBuildPattern;
import static com.vmware.g11n.pattern.detection.library.utils.CldrProcessors.*;
import static com.vmware.g11n.pattern.detection.library.utils.IsoProcessors.detectPatternFromPredefinedIsoFormats;
import static com.vmware.g11n.pattern.detection.library.utils.Matchers.*;
import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.chop;

public class PatternBuilders {

    public ValidatedPattern generatePatternFromComponents(LinkedList<String> userInputComponents, Locale locale, CldrData cldrData) {
        String expectedFormat = "";
        List<String> unmatchedElements = new LinkedList<>();

        for (String el : userInputComponents) {
            // Trim the current component and remove undetectable chars. Then add them to the final pattern as pre/postfix
            String trimmedInput = getTrimmedComponent(el);
            int prefixEndIndex = el.indexOf(trimmedInput);
            int postfixStartIndex = prefixEndIndex + trimmedInput.length();
            String prefix = prefixEndIndex != 0 ? el.substring(0, prefixEndIndex) : "";
            String postfix = el.substring(postfixStartIndex) + " ";

            // Check each component with manual logic first
            String patternForElement = processComponentAndGetPattern(cldrData, trimmedInput, locale, false);

            // If component cannot be detected manually, try to detect it using Standard cldr data
            if (patternForElement.isBlank()) {
                patternForElement = getValidatedPatternFromCldrFormats(cldrData, trimmedInput, locale).pattern + " ";
            }

            // Check component against ISO standard data
            if (patternForElement.isBlank()) {
                patternForElement = detectPatternFromPredefinedIsoFormats(trimmedInput).pattern + " ";
            }

            // If CLDR formats And manual logic couldn't detect format, add to unmatched component.
            // Else, append to the full format keeping the pre/postfix.
            //TODO: replace second expression with better logic - check for any of the suffixes possible.
            if (patternForElement.isBlank() || patternForElement.trim().equals(",")) {
                unmatchedElements.add(el);
            } else {
                expectedFormat = expectedFormat.concat(prefix + patternForElement.trim() + postfix);
            }
        }

        if (!unmatchedElements.isEmpty()) {
            ValidatorService.errors.put(UNDETECTED_COMPONENTS, UNDETECTED_COMPONENTS_ERROR + unmatchedElements);
            return null;
        }

        return verifyAndBuildPattern(expectedFormat.trim(), join(" ", userInputComponents), locale, cldrData);
    }

    private String processComponentAndGetPattern(CldrData cldrData, String component, Locale locale, boolean skipCombinedElementsCheck) {
        String pattern;

        String detectedDayOfMonth = buildDayOfMonthPattern(component);
        if (!detectedDayOfMonth.isEmpty()) {
            return detectedDayOfMonth;
        }

        String detectedYear = buildYearPattern(component);
        if (!detectedYear.isEmpty()) {
            return detectedYear;
        }

        // Check if the current element is valid time format
        String detectedTimeFromCldr = detectAnyCldrTimePattern(cldrData, component, locale);
        if (!detectedTimeFromCldr.isBlank()) {
            return detectedTimeFromCldr;
        }
        String detectedNonCldrTime = generatePatternForAnyTimeFormat(cldrData, component);
        if (!detectedNonCldrTime.isBlank()) {
            return detectedNonCldrTime;
        }

        // Check if the current element is valid localized String
        String patternForOriginalElement = generatePatternForLocalizedString(cldrData, component, false);
        String patternForChoppedElement = generatePatternForLocalizedString(cldrData, component, true);
        pattern = patternForOriginalElement.isEmpty() ? patternForChoppedElement : patternForOriginalElement;

        //Detect timezone
        String matchedTimeZone = detectAndBuildTimeZonePattern(cldrData, component);
        if (!matchedTimeZone.isEmpty()) {
            return matchedTimeZone;
        }

        // Check if current element is subsidiary element - localized element to be ignored. (Example: 'at', 'de', 'г.').
        String subsidiaryElementPattern = detectSubsidiaryElementAndGetPattern(cldrData, component);
        if (!subsidiaryElementPattern.isEmpty()) {
            return subsidiaryElementPattern;
        }

        // Check if current element is relative period - localized element to be ignored (Example: 'yesterday', 'last friday')
        String relativePeriod = detectRelativePeriod(cldrData, component);
        if (!relativePeriod.isEmpty()) {
            ValidatorService.suggestions.put(RELATIVE_TIME, RELATIVE_TIME_DETECTED_SUGGESTION + relativePeriod);
            return relativePeriod;
        }

        String cldrDateWithDifferentCharSeparators = detectStandardCldrDateWithDifferentCharacters(cldrData, component, locale);
        if (!cldrDateWithDifferentCharSeparators.isEmpty()) {
            return cldrDateWithDifferentCharSeparators;
        }

        // Check if the current element is valid asian word with no spacings (could be date, time or localized string)
        if (ASIAN_LANGUAGES_LIST.contains(locale.getLanguage()) && pattern.isBlank() && !skipCombinedElementsCheck && !component.contains(" ")) {
            return generatePatternAsianWordsWithNoSpacing(component, cldrData, locale);
        }

        return pattern;
    }

    /**
     * Detect numbers which are valid day of month up to 4 digits. (example: 1, 01, 001, 0001, 31, 031, 0031)
     * 31 and 1 will be matched as d (as 31 can be counted both as dd and d)
     * 031 will be matched as ddd (as it has 3 digits)
     */
    public static String buildDayOfMonthPattern(String input) {
        String matchedGroup = detectDayOfMonthAndGetMatchedGroup(input);
        if (matchedGroup.isEmpty()) {
            return EMPTY;
        }

        int padding = StringUtils.countMatches(matchedGroup, '0');
        // Cover cases like 021 and 031. to have 2 paddings instead of 1
        if (padding > 0 && matchedGroup.replaceAll("0", "").matches("^\\d{2}\\.?$")) {
            padding++;
        }

        return matchedGroup.replaceAll("\\d+", PADDING_LEADING_ZEROES_TO_DAY_OF_MONTH_SYMBOLS_MAP.get(padding));
    }

    public static String buildYearPattern(String input) {
        String matchedGroup = detectYearAndGetMatchedGroup(input);
        return matchedGroup.isEmpty() ? EMPTY : matchedGroup.replaceAll("\\d+", YEAR_4_DIGITS);
    }

    public static String buildMonthPattern(String input) {
        String matchedGroup = detectMonthAndGetMatchedGroup(input);
        return matchedGroup.isEmpty() ? EMPTY : matchedGroup.replaceAll("\\d+", MONTH_1_DIGIT);
    }

    private static String detectSubsidiaryElementAndGetPattern(CldrData cldrData, String element) {
        List<String> elementsToBeIgnored = getAllSubsidiaryElements(cldrData);
        return elementsToBeIgnored.stream()
                .filter(subsidiaryEl -> subsidiaryEl.replaceAll("'", "").equalsIgnoreCase(element))
                .findFirst().orElse(EMPTY);
    }

    private static String generatePatternForLocalizedString(CldrData cldrData, String input, boolean removeTrailingPeriod) {
        Map<String, List<String>> mappedData = CldrMappers.mapAllDatesData(cldrData);

        boolean isInputChopped = false;
        if (input.endsWith(".") && removeTrailingPeriod) {
            input = chop(input);
            isInputChopped = true;
        }
        String finalInput = input;

        String patternToReturn = mappedData.entrySet().stream().filter(
                        entry -> entry.getValue().stream().anyMatch(finalInput.trim()::equalsIgnoreCase))
                .findFirst()
                .map(entry -> FORMATS_TO_PATTERNS_MAP.get(entry.getKey())).orElse("");

        if (!patternToReturn.isEmpty() && isInputChopped) {
            return patternToReturn + ".";
        } else {
            return patternToReturn;
        }
    }

    private static String generatePatternForAnyTimeFormat(CldrData cldrData, String input) {
        // Try to match 24hour time format and don't expect dayPeriod before or after
        String matched24HourTime = getMatchedTimeOrPattern(input, standaloneTimePatternsToRegexMap, false);
        if (input.replace(matched24HourTime, "").isBlank()) {
            return getMatchedTimeOrPattern(input, standaloneTimePatternsToRegexMap, true);
        }

        // Try to match 12hour time and expect dayPeriod before or after
        String matched12HourTime = getMatchedTimeOrPattern(input, formattedTimePatternsToRegexMap, false);
        String periodData = input.replace(matched12HourTime, "");
        String[] splitInputArrayByTimeMatched = input.split(matched12HourTime);

        boolean isTimeBeforeDayPeriod = splitInputArrayByTimeMatched[0].isBlank();
        String timeDayPeriodDelimiter = periodData.startsWith(" ") || periodData.endsWith(" ") ? " " : EMPTY;

        String matchedDayPeriodPattern = generatePatternForLocalizedString(cldrData, periodData, false);

        if (!matchedDayPeriodPattern.isBlank()) {
            String matched12HourTimePattern = getMatchedTimeOrPattern(input, formattedTimePatternsToRegexMap, true);
            return isTimeBeforeDayPeriod ? matched12HourTimePattern + timeDayPeriodDelimiter + matchedDayPeriodPattern :
                    matchedDayPeriodPattern + timeDayPeriodDelimiter + matched12HourTimePattern;
        }

        return EMPTY;
    }

    private String generatePatternAsianWordsWithNoSpacing(String input, CldrData cldrData, Locale locale) {
        // To assert correct order without using specific positions, create duplicate of the input which will replace the matched parts
        String patternToReturn = input;

        //Check if the input contains localized dayPeriod (for time) or time
        String detectedTime = getAsianTimeSubstring(input, cldrData);
        String matchedTimePattern = processComponentAndGetPattern(cldrData, detectedTime, locale, true);
        if (!matchedTimePattern.isBlank()) {
            patternToReturn = patternToReturn.replace(detectedTime, processComponentAndGetPattern(cldrData, detectedTime, locale, true));
            input = input.replace(detectedTime, "");
        }

        //Check if the input contains localized asian dates and remove it from the input
        Map<String, String> detectedPatternForDatesMap = getMatchedSubstringAndPatternForAsianDates(cldrData, input);
        String detectedPatternForDates = detectedPatternForDatesMap.entrySet().stream().findFirst().get().getKey();
        patternToReturn = patternToReturn.replace(detectedPatternForDatesMap.get(detectedPatternForDates), detectedPatternForDates);
        input = input.replace(detectedPatternForDatesMap.get(detectedPatternForDates), "");

        //Check if the input contains localized string, generate pattern and remove it from the input so logic can proceed with other sub-parts
        String longestLocalizedInputSubstring = getLongestLocalizedSubstring(input, cldrData);
        if (!longestLocalizedInputSubstring.isEmpty()) {
            patternToReturn = patternToReturn.replace(longestLocalizedInputSubstring, processComponentAndGetPattern(cldrData, longestLocalizedInputSubstring, locale, true));
        }
        input = input.replace(longestLocalizedInputSubstring, "");

        //Check the leftover input for any match
        String detectedLeftoverSubstring = processComponentAndGetPattern(cldrData, input, locale, true);
        patternToReturn = patternToReturn.replace(input, detectedLeftoverSubstring);

        //If the left-over string has not matched anything AND the rest of the input has not been matched, return EMPTY string.
        if (detectedLeftoverSubstring.isEmpty() && !input.isEmpty()) {
            return EMPTY;
        } else {
            return patternToReturn.replace(" ", "");
        }
    }

    private Map<String, String> getMatchedSubstringAndPatternForAsianDates(CldrData cldrData, String input) {
        DateFields dateFields = cldrData.getDateFields();
        Map<String, String> mappedDateFields = CldrMappers.mapAllDateFieldsData(dateFields);

        String initialUserInput = input;
        String patternDetectedFromInput = "";

        //Check input for months, days, years symbols and match (for example entry = 年) and check if it's contained in the input
        for (Map.Entry<String, String> entry : mappedDateFields.entrySet()) {
            if (input.contains(entry.getValue())) {
                int indexOfMatchedValue = input.indexOf(entry.getValue());
                String subStringFromStartToIndex;

                //Add check if the symbol not at beginning (not error or time)
                if (indexOfMatchedValue != 0) {
                    subStringFromStartToIndex = input.substring(0, indexOfMatchedValue);

                    String detectedPatternFromField = getPatternFromDateField(entry.getKey(), subStringFromStartToIndex);

                    if (!detectedPatternFromField.isEmpty()) {
                        patternDetectedFromInput = patternDetectedFromInput.concat(detectedPatternFromField + entry.getValue());
                        input = input.replace(subStringFromStartToIndex + entry.getValue(), "");
                    } else {
                        return ImmutableMap.of(EMPTY, input);
                    }
                }
            }
        }

        return ImmutableMap.of(patternDetectedFromInput, initialUserInput.replace(input, ""));
    }

    private static String getLongestLocalizedSubstring(String input, CldrData cldrData) {
        Map<String, List<String>> mappedData = CldrMappers.mapAllDatesData(cldrData);

        // Get list of all matching elements which are NOT a number (For input February2022, we will get list of [Feb, February]
        List<String> matchedLocalizedElements = new LinkedList<>();
        mappedData.forEach((key, value) -> matchedLocalizedElements.addAll(value.stream()
                .filter(el -> (input.contains(el) && !el.matches(".*[0-9].*") && el.length() > 1)).toList()));

        return matchedLocalizedElements.stream().max(Comparator.comparingInt(String::length)).orElse("");
    }

    private static String getAsianTimeSubstring(String input, CldrData cldrData) {
        String matchedDayPeriod = cldrData.gregorianCalendar.dayPeriods.getAllDayPeriods().stream().filter(input::contains).findFirst().orElse("");

        String matchedTime;

        if (!matchedDayPeriod.isBlank()) {
            matchedTime = getMatchedTimeOrPattern(input, formattedTimePatternsToRegexMap, false);
        } else {
            matchedTime = getMatchedTimeOrPattern(input, standaloneTimePatternsToRegexMap, false);
        }

        return matchedDayPeriod + matchedTime;
    }

    private static String detectAndBuildTimeZonePattern(CldrData cldrData, String input) {
        TimezoneNames timezoneNames = cldrData.getTimezoneNames();

        List<String> detectedTimezones = new ArrayList<>();
        detectedTimezones.add(detectLongSpecificNonLocationPattern(input, timezoneNames));
        detectedTimezones.add(detectShortSpecificNonLocationPattern(input, timezoneNames));
        detectedTimezones.add(detectLongGenericNonLocationPattern(input, timezoneNames));
        detectedTimezones.add(detectShortGenericNonLocationPattern(input, timezoneNames));
        detectedTimezones.add(detectBasicHmsPattern(input));
        detectedTimezones.add(detectExtendedHmsPattern(input));
        detectedTimezones.add(detectLongLocalizedGmtPattern(input));
        detectedTimezones.add(detectShortLocalizedGmtPattern(input));
        detectedTimezones.add(detectTimeZoneExemplarCityPattern(input, timezoneNames));
        detectedTimezones.add(detectLongTimeZoneIdPattern(input));
        detectedTimezones.add(detectShortTimeZoneIdPattern(input));

        return detectedTimezones.stream().filter(pattern -> !pattern.isEmpty()).findFirst().orElse("");
    }

    private static String detectRelativePeriod(CldrData cldrData, String input) {
        return cldrData.dateFields.getAllDateFieldsValues().stream()
                .filter(input::equalsIgnoreCase).map(s -> "'" + s + "'")
                .findAny().orElse(EMPTY);
    }

    /*
    Some elements are valid dates with correct ordering, but different separators - for example dd.m.y may be standard CLDR format,
    but dd/m/y should still be parsed. This method replaces the standard separators and if match is found, returns a pattern.
     */
    private static String detectStandardCldrDateWithDifferentCharacters(CldrData cldrData, String input, Locale locale) {
        String shortStandardCldrDateFormat = cldrData.gregorianCalendar.dateFormats.shortened;
        List<String> commonSeparatorsChars = new ArrayList<>(List.of(".", "/", ",", "-", "|", "\\"));
        String standardCldrDataSeparatorChar = detectDateCharacterSeparatorAndGetMatchedGroup(shortStandardCldrDateFormat);

        commonSeparatorsChars.removeIf(standardCldrDataSeparatorChar::contains);

        for (String separator : commonSeparatorsChars) {
            String changedSeparatorCldrDateFormat = shortStandardCldrDateFormat.replace(standardCldrDataSeparatorChar, separator);
            if (isValidPattern(changedSeparatorCldrDateFormat, input, locale, LocalizedType.DATE)) {
                ValidatorService.suggestions.put(INCORRECT_FORMAT_SEPARATOR, format(INCORRECT_FORMAT_SEPARATOR_SUGGESTION, input, standardCldrDataSeparatorChar));
                return changedSeparatorCldrDateFormat;
            }
        }

        return EMPTY;
    }
}
