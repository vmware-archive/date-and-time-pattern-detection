/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.google.common.collect.ImmutableMap;
import com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidatedPattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static java.time.format.DateTimeFormatter.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class IsoProcessors {

    public static String ISO_DATE_MESSAGE = "The input is valid ISO_8601 Standard Date.";
    public static String ISO_DATE_TIME_MESSAGE = "The input is valid ISO_8601 Standard Date + Time.";
    public static String ISO_DATE_TIME_OFFSET_MESSAGE = "The input is valid ISO_8601 Standard Date + Time with offset.";
    public static String ISO_ZONED_DATE_MESSAGE = "The input is valid ISO_8601 Standard Zoned Date + Time.";
    public static String ISO_OFFSET_DATE_MESSAGE = "The input is valid ISO_8601 Standard Offset Date.";
    public static String ISO_OFFSET_TIME_MESSAGE = "The input is valid ISO_8601 Standard Offset Time.";
    public static String ISO_OFFSET_TIME_WITHOUT_SECONDS_MESSAGE = "The input is valid ISO_8601 Standard Offset Time.";
    public static String ISO_ORDINAL_DATE_MESSAGE = "The input is valid ISO_8601 Ordinal Date.";

    public static String ISO_LOCAL_DATE_PATTERN = "yyyy-MM-dd";
    public static String ISO_ORDINAL_DATE_PATTERN = "yyyy-D";
    public static String ISO_LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static String ISO_OFFSET_TIME_PATTERN = "HH:mm:ssZZZZZ";
    public static String ISO_LOCAL_TIME_PATTERN = "HH:mmZZZZZ";
    public static String ISO_OFFSET_DATE_PATTERN = "yyyy-MM-ddZZZZZ";
    public static String ISO_ZONED_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZZ'['VV']'";
    public static String ISO_OFFSET_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

    static Map<DateTimeFormatter, String> isoFormatsToMessagesMap = new ImmutableMap.Builder<DateTimeFormatter, String>()
            .put(ISO_LOCAL_DATE, ISO_DATE_MESSAGE)
            .put(ISO_LOCAL_DATE_TIME, ISO_DATE_TIME_MESSAGE)
            .put(ISO_OFFSET_DATE_TIME, ISO_DATE_TIME_OFFSET_MESSAGE)
            .put(ISO_ZONED_DATE_TIME, ISO_ZONED_DATE_MESSAGE)
            .put(ISO_OFFSET_DATE, ISO_OFFSET_DATE_MESSAGE)
            .put(ISO_OFFSET_TIME, ISO_OFFSET_TIME_MESSAGE)
            .put(ISO_LOCAL_TIME, ISO_OFFSET_TIME_WITHOUT_SECONDS_MESSAGE)
            .put(ISO_ORDINAL_DATE, ISO_ORDINAL_DATE_MESSAGE)
            .build();

    static Map<DateTimeFormatter, String> isoDateFormatsToPatternsMap = new ImmutableMap.Builder<DateTimeFormatter, String>()
            .put(ISO_LOCAL_DATE, ISO_LOCAL_DATE_PATTERN)
            .put(ISO_ORDINAL_DATE, ISO_ORDINAL_DATE_PATTERN)
            .build();

    static Map<DateTimeFormatter, String> isoDateTimeFormatsToPatternsMap = new ImmutableMap.Builder<DateTimeFormatter, String>()
            .put(ISO_LOCAL_DATE_TIME, ISO_LOCAL_DATE_TIME_PATTERN)
            .build();

    static Map<DateTimeFormatter, String> isoTimeFormatsToPatternsMap = new ImmutableMap.Builder<DateTimeFormatter, String>()
            .put(ISO_OFFSET_TIME, ISO_OFFSET_TIME_PATTERN)
            .put(ISO_LOCAL_TIME, ISO_LOCAL_TIME_PATTERN)
            .build();

    static Map<DateTimeFormatter, String> isoZonedDateTimeFormatsToPatternsMap = new ImmutableMap.Builder<DateTimeFormatter, String>()
            .put(ISO_OFFSET_DATE_TIME, ISO_OFFSET_DATE_TIME_PATTERN)
            .put(ISO_ZONED_DATE_TIME, ISO_ZONED_DATE_TIME_PATTERN)
            .put(ISO_OFFSET_DATE, ISO_OFFSET_DATE_PATTERN)
            .build();

    public static ValidatedPattern detectPatternFromPredefinedIsoFormats(String input) {
        DateTimeFormatter parsableFormatter = detectIsoDateFormatter(input);
        if (parsableFormatter != null) {
            return ValidatedPattern.builder().pattern(isoDateFormatsToPatternsMap.get(parsableFormatter)).localizedType(LocalizedType.ISO_DATE)
                    .isStandardFormat(true).isValidDate(true).patternInfoMessage(isoFormatsToMessagesMap.get(parsableFormatter)).build();
        }

        parsableFormatter = detectIsoDateTimeFormatter(input);
        if (parsableFormatter != null) {
            return ValidatedPattern.builder().pattern(isoDateTimeFormatsToPatternsMap.get(parsableFormatter)).localizedType(LocalizedType.ISO_DATE_TIME)
                    .isStandardFormat(true).isValidDate(true).patternInfoMessage(isoFormatsToMessagesMap.get(parsableFormatter)).build();
        }

        parsableFormatter = detectIsoZonedDateTimeFormatter(input);
        if (parsableFormatter != null) {
            return ValidatedPattern.builder().pattern(isoZonedDateTimeFormatsToPatternsMap.get(parsableFormatter)).localizedType(LocalizedType.ISO_DATE_TIME)
                    .isStandardFormat(true).isValidDate(true).patternInfoMessage(isoFormatsToMessagesMap.get(parsableFormatter)).build();
        }

        parsableFormatter = detectIsoOffsetTimeFormatter(input);
        if (parsableFormatter != null) {
            return ValidatedPattern.builder().pattern(isoTimeFormatsToPatternsMap.get(parsableFormatter)).localizedType(LocalizedType.ISO_TIME)
                    .isStandardFormat(true).isValidDate(true).patternInfoMessage(isoFormatsToMessagesMap.get(parsableFormatter)).build();
        }

        return ValidatedPattern.builder().pattern(EMPTY).build();
    }

    private static DateTimeFormatter detectIsoDateFormatter(String input) {
        return isoDateFormatsToPatternsMap.keySet().stream()
                .filter(isoFormatter -> {
                    try {
                        return !LocalDate.parse(input, isoFormatter).toString().isEmpty();
                    } catch (DateTimeParseException ignored) {
                        return false;
                    }
                }).findFirst().orElse(null);
    }

    private static DateTimeFormatter detectIsoDateTimeFormatter(String input) {
        return isoDateTimeFormatsToPatternsMap.keySet().stream()
                .filter(isoFormatter -> {
                    try {
                        return !LocalDateTime.parse(input, isoFormatter).toString().isEmpty();
                    } catch (DateTimeParseException ignored) {
                        return false;
                    }
                }).findFirst().orElse(null);
    }

    private static DateTimeFormatter detectIsoZonedDateTimeFormatter(String input) {
        return isoZonedDateTimeFormatsToPatternsMap.keySet().stream()
                .filter(isoFormatter -> {
                    try {
                        return !ZonedDateTime.parse(input, isoFormatter).toString().isEmpty();
                    } catch (DateTimeParseException ignored) {
                        return false;
                    }
                })
                .findFirst().orElse(null);
    }

    private static DateTimeFormatter detectIsoOffsetTimeFormatter(String input) {
        return isoTimeFormatsToPatternsMap.keySet().stream()
                .filter(isoFormatter -> {
                    try {
                        return !LocalTime.parse(input, isoFormatter).toString().isEmpty();
                    } catch (DateTimeParseException ignored) {
                        return false;
                    }
                })
                .findFirst().orElse(null);
    }
}
