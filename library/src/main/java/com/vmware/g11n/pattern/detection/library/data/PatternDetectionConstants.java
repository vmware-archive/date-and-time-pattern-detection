/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public final class PatternDetectionConstants {
    public static String FORMATTED_WIDE_CLDR_DATA_KEY = "formatted-wide";
    public static String FORMATTED_NARROW_CLDR_DATA_KEY = "formatted-narrow";
    public static String FORMATTED_ABBREVIATED_CLDR_DATA_KEY = "formatted-abbreviated";
    public static String FORMATTED_ABBREVIATED_CLDR_DATA_KEY_AM_PM_ONLY = "formatted-abbreviated-am-pm";

    //Date fields
    public static String MONTH_FIELD_KEY = "month-field";
    public static String YEAR_FIELD_KEY = "year-field";
    public static String DAY_FIELD_KEY = "day-field";
    public static String HOUR_FIELD_KEY = "hour-field";
    public static String MINUTE_FIELD_KEY = "minute-field";
    public static String SECOND_FIELD_KEY = "second-field";

    public static String MONTHS_SUFFIX = "-months";
    public static String DAYS_SUFFIX = "-days";
    public static String DAY_PERIOD_SUFFIX = "-dayPeriods";

    public static String STANDALONE_WIDE_CLDR_DATA_KEY = "standalone-wide";
    public static String STANDALONE_NARROW_CLDR_DATA_KEY = "standalone-narrow";
    public static String STANDALONE_ABBREVIATED_CLDR_DATA_KEY = "standalone-abbreviated";

    public static List<String> ASIAN_LANGUAGES_LIST = ImmutableList.of("zh", "ja", "ko");

    // Validation messages
    public static String VALID_NON_STANDARD_DATE_MESSAGE = "The input is valid and non-standard localized date.";
    public static String VALID_STANDARD_DATE_MESSAGE = "The input is valid and standard localized date.";
    public static String VALID_NON_STANDARD_TIME_MESSAGE = "The input is valid and non-standard localized time.";
    public static String VALID_STANDARD_TIME_MESSAGE = "The input is valid and standard localized time.";
    public static String VALID_STANDARD_DATE_TIME_MESSAGE = "The input is valid and standard localized date+time.";
    public static String VALID_NON_STANDARD_DATE_TIME_MESSAGE = "The input is valid and non-standard localized date+time.";
    public static String VALID_TEMPORAL_MESSAGE = "The input is valid localized temporal";
    public static String INVALID_TEMPORAL_OR_DATE_MESSAGE = "The input is localized correctly, but can't be parsed as date or any temporal type";

    // Conversion messages
    public static String NON_APPLICABLE_SOURCE_PATTERN = "The generated output is not localized correctly. The source pattern is not applicable for the target locale.";

    // Suggestions
    public static String INCORRECT_CASING_SUGGESTION = "Incorrect casing. Please follow recommended CLDR casing for localized content for the provided locale.";
    public static String NON_STANDARD_DATE_SUGGESTION = "Provided Date does not match the standard CLDR date patterns: ";
    public static String NON_STANDARD_TIME_SUGGESTION = "Provided Time does not match the standard CLDR time patterns: ";
    public static String NON_STANDARD_DATE_TIME_SUGGESTION = "Provided Date-Time does not match the standard CLDR dateTime patterns: ";
    public static String RELATIVE_TIME_DETECTED_SUGGESTION = "The input is containing relative time components. If left as part of the pattern, they can introduce issues when input is formatted to another language. Relative components detected: ";
    public static String ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION_SUGGESTION = "It is not recommended to convert asian and non-asian localized inputs which do not follow CLDR Standard formats. Some components may not be parsable using the same patterns.";
    public static String NON_CONVERTABLE_COMPONENTS_SUGGESTION = "Non-convertable (hardcoded) components detected in source input. The following elements are not localized to the target locale: ";
    public static String INCORRECT_FORMAT_SEPARATOR_SUGGESTION = "The date component (%s) is correctly localized, but the separator is not CLDR standard. Standard separator: '%s'";

    // Errors
    public static String INVALID_LOCALE_ERROR = "Provided locale is invalid. Please check input.";
    public static String NOT_SUPPORTED_LOCALE_ERROR = "Provided locale is not supported.";
    public static String UNDETECTED_COMPONENTS_ERROR = "Cannot generate/validate skeleton pattern for the following components: ";
    public static String NON_CONVERTABLE_INPUT = "Localized output cannot be generated. The detected pattern of the source input is invalid or not full date.";

    // This is needed as VIP and standard libraries accept locales in different way. Example: zh-Hant and zh-TW
    public static Map<String, String> serviceSpecialLocaleFormats = ImmutableMap.of(
            "zh-Hant", "zh-TW",
            "zh-Hans", "zh-CN",
            "zh-Hans-CN", "zh-CN",
            "zh-Hant-TW", "zh-TW"
    );

    public static ImmutableMap<String, String> FORMATS_TO_PATTERNS_MAP = ImmutableMap.<String, String>builder()
            // Months
            .put(FORMATTED_WIDE_CLDR_DATA_KEY + MONTHS_SUFFIX, DateFieldSymbols.MONTH_FORMATTED_WIDE)
            .put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY + MONTHS_SUFFIX, DateFieldSymbols.MONTH_FORMATTED_ABBREVIATED)
            .put(FORMATTED_NARROW_CLDR_DATA_KEY + MONTHS_SUFFIX, DateFieldSymbols.MONTH_FORMATTED_NARROW)
            .put(STANDALONE_WIDE_CLDR_DATA_KEY + MONTHS_SUFFIX, DateFieldSymbols.MONTH_STANDALONE_WIDE)
            .put(STANDALONE_ABBREVIATED_CLDR_DATA_KEY + MONTHS_SUFFIX, DateFieldSymbols.MONTH_STANDALONE_ABBREVIATED)
            .put(STANDALONE_NARROW_CLDR_DATA_KEY + MONTHS_SUFFIX, DateFieldSymbols.MONTH_STANDALONE_NARROW)

            // Days
            .put(FORMATTED_WIDE_CLDR_DATA_KEY + DAYS_SUFFIX, DateFieldSymbols.DAY_OF_WEEK_FORMATTED_WIDE)
            .put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY + DAYS_SUFFIX, DateFieldSymbols.DAY_OF_WEEK_FORMATTED_ABBREVIATED)
            .put(FORMATTED_NARROW_CLDR_DATA_KEY + DAYS_SUFFIX, DateFieldSymbols.DAY_OF_WEEK_FORMATTED_NARROW)
            .put(STANDALONE_WIDE_CLDR_DATA_KEY + DAYS_SUFFIX, DateFieldSymbols.DAY_OF_WEEK_STANDALONE_WIDE)
            .put(STANDALONE_NARROW_CLDR_DATA_KEY + DAYS_SUFFIX, DateFieldSymbols.DAY_OF_WEEK_STANDALONE_NARROW)
            .put(STANDALONE_ABBREVIATED_CLDR_DATA_KEY + DAYS_SUFFIX, DateFieldSymbols.DAY_OF_WEEK_STANDALONE_ABBREVIATED)

            // Day periods
            .put(FORMATTED_WIDE_CLDR_DATA_KEY + DAY_PERIOD_SUFFIX, DateFieldSymbols.DAY_PERIOD_FORMATTED_WIDE)
            .put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY + DAY_PERIOD_SUFFIX, DateFieldSymbols.DAY_PERIOD_FORMATTED_ABBREVIATED)
            .put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY_AM_PM_ONLY + DAY_PERIOD_SUFFIX, DateFieldSymbols.DAY_PERIOD_FORMATTED_ABBREVIATED_AM_PM)
            .put(FORMATTED_NARROW_CLDR_DATA_KEY + DAY_PERIOD_SUFFIX, DateFieldSymbols.DAY_PERIOD_FORMATTED_NARROW)

            .build();
}
