/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.vmware.g11n.pattern.detection.library.patterns.PatternValidators;
import com.vmware.g11n.pattern.detection.library.services.ValidatorService;
import com.vmware.g11n.pattern.detection.model.CldrData;
import com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidatedPattern;
import com.vmware.g11n.pattern.detection.model.timezones.SubZone;
import com.vmware.g11n.pattern.detection.model.timezones.TimezoneNames;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static com.vmware.g11n.pattern.detection.library.data.DateFieldSymbols.*;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.NON_STANDARD_DATE;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.NON_STANDARD_TIME;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CldrProcessors {

    // Try to match the input from any CLDR full pattern
    public static ValidatedPattern getValidatedPatternFromCldrFormats(CldrData cldrData, String input, Locale locale) {
        ValidatedPattern validatedPattern = detectStandardDateCldrPattern(cldrData, input, locale);
        if (nonNull(validatedPattern)) {
            return validatedPattern;
        }

        validatedPattern = detectStandardTimeCldrPattern(cldrData, input, locale);
        if (nonNull(validatedPattern)) {
            return validatedPattern;
        }

        validatedPattern = detectStandardDateTimeCldrPattern(cldrData, input, locale);
        if (nonNull(validatedPattern)) {
            return validatedPattern;
        }

        validatedPattern = detectNonStandardDateCldrPattern(cldrData, input, locale);
        if (nonNull(validatedPattern)) {
            return validatedPattern;
        }

        validatedPattern = detectNonStandardTimeCldrPattern(cldrData, input, locale);
        if (nonNull(validatedPattern)) {
            return validatedPattern;
        }

        validatedPattern = detectAnyTemporalCldrPattern(cldrData, input, locale);
        if (nonNull(validatedPattern)) {
            return validatedPattern;
        }

        return ValidatedPattern.builder().pattern(EMPTY).build();
    }

    private static ValidatedPattern detectStandardDateCldrPattern(CldrData cldrData, String input, Locale locale) {
        Map<String, String> standardDateFormats = new LinkedHashMap<>(cldrData.gregorianCalendar.dateFormats.getAllDateFormatsAsMap());

        for (Map.Entry<String, String> entry : standardDateFormats.entrySet()) {
            if (PatternValidators.isValidPattern(entry.getValue(), input, locale, LocalizedType.DATE)) {
                return ValidatedPattern.builder().pattern(entry.getValue()).localizedType(LocalizedType.DATE).cldrDataKeyName(entry.getKey())
                        .isStandardFormat(true).isValidDate(true).patternInfoMessage(VALID_STANDARD_DATE_MESSAGE).build();
            }
        }
        return null;
    }

    private static ValidatedPattern detectNonStandardDateCldrPattern(CldrData cldrData, String input, Locale locale) {
        Map<String, String> availableFormats = new LinkedHashMap<>(cldrData.gregorianCalendar.dateTimeFormats.availableFormats);
        for (Map.Entry<String, String> entry : availableFormats.entrySet()) {
            if (PatternValidators.isValidPattern(entry.getValue(), input, locale, LocalizedType.DATE)) {
                ValidatorService.suggestions.put(NON_STANDARD_DATE, NON_STANDARD_DATE_SUGGESTION
                        + " Standard date formats: " + cldrData.gregorianCalendar.dateFormats.getAllDateFormatsAsMap().values());
                return ValidatedPattern.builder().pattern(entry.getValue()).localizedType(LocalizedType.DATE).cldrDataKeyName(entry.getKey())
                        .isStandardFormat(false).isValidDate(true).patternInfoMessage(VALID_NON_STANDARD_DATE_MESSAGE).build();
            }
        }
        return null;
    }

    private static ValidatedPattern detectAnyTemporalCldrPattern(CldrData cldrData, String input, Locale locale) {
        for (Map.Entry<String, String> entry : cldrData.gregorianCalendar.dateTimeFormats.availableFormats.entrySet()) {
            if (PatternValidators.isValidPattern(entry.getValue(), input, locale, LocalizedType.TEMPORAL)) {
                return ValidatedPattern.builder().pattern(entry.getValue()).localizedType(LocalizedType.TEMPORAL).cldrDataKeyName(entry.getKey())
                        .isStandardFormat(false).isValidDate(false).patternInfoMessage(VALID_TEMPORAL_MESSAGE).build();
            }
        }
        return null;
    }

    private static ValidatedPattern detectStandardDateTimeCldrPattern(CldrData cldrData, String input, Locale locale) {
        Map<String, String> standardDateTimeFormats = new LinkedHashMap<>(cldrData.gregorianCalendar.getStandardDateTimeMap());

        for (Map.Entry<String, String> entry : standardDateTimeFormats.entrySet()) {
            if (PatternValidators.isValidPattern(entry.getValue(), input, locale, LocalizedType.DATE_TIME)) {
                return ValidatedPattern.builder().pattern(entry.getValue()).localizedType(LocalizedType.DATE_TIME).cldrDataKeyName(entry.getKey())
                        .isStandardFormat(true).isValidDate(true).patternInfoMessage(VALID_STANDARD_DATE_TIME_MESSAGE).build();
            }
        }
        return null;
    }

    private static ValidatedPattern detectStandardTimeCldrPattern(CldrData cldrData, String input, Locale locale) {
        Map<String, String> standardTimeFormats = new LinkedHashMap<>(cldrData.gregorianCalendar.timeFormats.getAllTimeFormatsAsMap());

        for (Map.Entry<String, String> entry : standardTimeFormats.entrySet()) {
            if (PatternValidators.isValidPattern(entry.getValue(), input, locale, LocalizedType.TIME)) {
                return ValidatedPattern.builder().pattern(entry.getValue()).localizedType(LocalizedType.TIME).cldrDataKeyName(entry.getKey())
                        .isStandardFormat(true).isValidDate(false).patternInfoMessage(VALID_STANDARD_TIME_MESSAGE).build();
            }
        }
        return null;
    }

    private static ValidatedPattern detectNonStandardTimeCldrPattern(CldrData cldrData, String input, Locale locale) {
        Map<String, String> availableFormats = new LinkedHashMap<>(cldrData.gregorianCalendar.dateTimeFormats.availableFormats);
        for (Map.Entry<String, String> entry : availableFormats.entrySet()) {
            if (PatternValidators.isValidPattern(entry.getValue(), input, locale, LocalizedType.TIME)) {
                ValidatorService.suggestions.put(NON_STANDARD_TIME, NON_STANDARD_TIME_SUGGESTION + " " +
                        cldrData.gregorianCalendar.timeFormats.toString());
                return ValidatedPattern.builder().pattern(entry.getValue()).localizedType(LocalizedType.TIME).cldrDataKeyName(entry.getKey())
                        .isStandardFormat(false).isValidDate(false).patternInfoMessage(VALID_NON_STANDARD_TIME_MESSAGE).build();
            }
        }
        return null;
    }

    //TODO: Revisit
    public static String detectAnyCldrTimePattern(CldrData cldrData, String el, Locale locale) {
        ValidatedPattern patternForDetectedStandardTime = detectStandardTimeCldrPattern(cldrData, el, locale);
        ValidatedPattern patternForDetectedNonStandardTime = detectStandardTimeCldrPattern(cldrData, el, locale);

        String detectedStandardTime = nonNull(patternForDetectedStandardTime) ? patternForDetectedStandardTime.pattern : EMPTY;
        String detectedNonStandardTime = nonNull(patternForDetectedNonStandardTime) ? patternForDetectedNonStandardTime.pattern : EMPTY;

        return detectedStandardTime.isEmpty() ? detectedNonStandardTime : detectedStandardTime;
    }

    public static String detectShortTimeZoneIdPattern(String input) {
        return ZoneId.SHORT_IDS.keySet().stream().anyMatch(input::equalsIgnoreCase) ? SHORT_TIME_ZONE_ID_SYMBOL : EMPTY;
    }

    public static String detectLongTimeZoneIdPattern(String input) {
        Set<String> longTimeZoneIds = ZoneId.getAvailableZoneIds();
        longTimeZoneIds.removeAll(ZoneId.SHORT_IDS.keySet());

        return longTimeZoneIds.stream().anyMatch(input::equalsIgnoreCase) ? LONG_TIME_ZONE_ID_SYMBOL : EMPTY;
    }

    public static String detectTimeZoneExemplarCityPattern(String input, TimezoneNames zones) {
        List<String> exemplarCities = zones.getZones().values().stream()
                .flatMap(zone -> zone.values().stream())
                .map(SubZone::getExemplarCity).toList();

        return exemplarCities.stream()
                .filter(Objects::nonNull)
                .anyMatch(input::equalsIgnoreCase) ? TIME_ZONE_EXEMPLAR_CITY_SYMBOL : EMPTY;
    }

    public static String detectLongGenericNonLocationPattern(String input, TimezoneNames zones) {
        return zones.getMetazone().values().stream()
                .filter(longMetaZones -> nonNull(longMetaZones.getLongMetaZone()))
                .map(metaZone -> metaZone.getLongMetaZone().getGeneric())
                .toList()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(input::equalsIgnoreCase) ? LONG_GENERIC_NON_LOCATION_TIME_ZONE_SYMBOL : EMPTY;
    }

    public static String detectShortGenericNonLocationPattern(String input, TimezoneNames zones) {
        return zones.getMetazone().values().stream()
                .filter(shortMetaZones -> nonNull(shortMetaZones.getShortMetaZone()))
                .map(metaZone -> metaZone.getShortMetaZone().getGeneric())
                .toList()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(input::equals) ? SHORT_GENERIC_NON_LOCATION_TIME_ZONE_SYMBOL : EMPTY; // We have equals here as 'AT' is valid zone and 'at' is subsidiary element
    }

    public static String detectLongSpecificNonLocationPattern(String input, TimezoneNames zones) {
        return zones.getMetazone().values().stream()
                .filter(longMetaZones -> nonNull(longMetaZones.getLongMetaZone()))
                .flatMap(metaZone -> Stream.of(metaZone.getLongMetaZone().getDaylight(), metaZone.getLongMetaZone().getStandard()))
                .toList()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(input::equalsIgnoreCase) ? LONG_SPECIFIC_NON_LOCATION_TIME_ZONE_SYMBOL : EMPTY;
    }

    public static String detectShortSpecificNonLocationPattern(String input, TimezoneNames zones) {
        return zones.getMetazone().values().stream()
                .filter(shortMetaZones -> nonNull(shortMetaZones.getShortMetaZone()))
                .flatMap(metaZone -> Stream.of(metaZone.getShortMetaZone().getDaylight(), metaZone.getShortMetaZone().getStandard()))
                .toList()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(input::equalsIgnoreCase) ? SHORT_SPECIFIC_NON_LOCATION_TIME_ZONE_SYMBOL : EMPTY;
    }

    public static String detectShortLocalizedGmtPattern(String input) {
        return Matchers.detectShortGmtFormat(input) ? SHORT_LOCALIZED_GMT_SYMBOL : EMPTY;
    }

    public static String detectLongLocalizedGmtPattern(String input) {
        return Matchers.detectLongGmtFormat(input) ? LONG_LOCALIZED_GMT_SYMBOL : EMPTY;
    }

    public static String detectBasicHmsPattern(String input) {
        return Matchers.detectBasicHmsFormat(input) ? BASIC_HMS_SYMBOL : EMPTY;
    }

    public static String detectExtendedHmsPattern(String input) {
        return Matchers.detectExtendedHmsFormat(input) ? EXTENDED_HMS_SYMBOL : EMPTY;
    }

    public static String detectTimezonesWithSpacings(String input, CldrData cldrData) {
        List<String> timeZonesWithSpacing = new ArrayList<>();

        List<String> exemplarCities = cldrData.timezoneNames.getZones().values().stream()
                .flatMap(zone -> zone.values().stream())
                .map(SubZone::getExemplarCity).toList();

        List<String> longGenericNonLocations = cldrData.timezoneNames.getMetazone().values().stream()
                .filter(longMetaZones -> nonNull(longMetaZones.getLongMetaZone()))
                .map(metaZone -> metaZone.getLongMetaZone().getGeneric())
                .toList();

        List<String> longSpecificNonLocations = cldrData.timezoneNames.getMetazone().values().stream()
                .filter(longMetaZones -> nonNull(longMetaZones.getLongMetaZone()))
                .flatMap(metaZone -> Stream.of(metaZone.getLongMetaZone().getDaylight(), metaZone.getLongMetaZone().getStandard()))
                .toList();

        timeZonesWithSpacing.addAll(exemplarCities);
        timeZonesWithSpacing.addAll(longGenericNonLocations);
        timeZonesWithSpacing.addAll(longSpecificNonLocations);

        return timeZonesWithSpacing.stream()
                .filter(Objects::nonNull)
                .filter(zone -> zone.contains(" ") && input.contains(zone))
                .findFirst()
                .orElse(EMPTY);
    }

    public static String detectAnyDayPeriod(String input, CldrData cldrData) {
        return cldrData.gregorianCalendar.dayPeriods.getAllDayPeriods().stream()
                .filter(Objects::nonNull)
                // As Java.contains cannot match NBSP unicode character, replace it with space just for the filter body
                .filter(dayPeriod -> input.contains(dayPeriod.replace("\u00A0", " ")))
                .max(Comparator.comparingInt(String::length))
                .orElse(EMPTY);
    }

    public static String detectAnyRelativeTime(String input, CldrData cldrData) {
        return cldrData.dateFields.getAllDateFieldsValues().stream()
                .filter(Objects::nonNull)
                // As Java.contains cannot match NBSP unicode character, replace it with space just for the filter body
                .filter(relativeTime -> input.contains(relativeTime.replace("\u00A0", " ")))
                .max(Comparator.comparingInt(String::length))
                .orElse(EMPTY);
    }
}
