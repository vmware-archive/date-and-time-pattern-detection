/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.data;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class DateFieldSymbols {
    // Timezones
    public static String SHORT_TIME_ZONE_ID_SYMBOL = "V"; // E.g: uslax
    public static String LONG_TIME_ZONE_ID_SYMBOL = "VV"; // E.g: America/Los_Angeles (Investigate - some issues are present)
    public static String TIME_ZONE_EXEMPLAR_CITY_SYMBOL = "VVV"; // E.g: Los Angeles
    public static String LONG_GENERIC_NON_LOCATION_TIME_ZONE_SYMBOL = "vvvv"; //CLDR generic long E.g: Los Angeles Time
    public static String SHORT_GENERIC_NON_LOCATION_TIME_ZONE_SYMBOL = "v"; // E.g: PT
    public static String SHORT_SPECIFIC_NON_LOCATION_TIME_ZONE_SYMBOL = "z"; // E.g: EEST
    public static String LONG_SPECIFIC_NON_LOCATION_TIME_ZONE_SYMBOL = "zzzz"; //CLDR standard + daylight long e.g: British Summer Time
    public static String SHORT_LOCALIZED_GMT_SYMBOL = "O"; // E.g: GMT+3 or GMT-12
    public static String LONG_LOCALIZED_GMT_SYMBOL = "ZZZZ"; // E.g: GMT+03:00 or GMT-12:00
    public static String BASIC_HMS_SYMBOL = "Z"; // E.g: GMT+03:00 or GMT-12:00
    public static String EXTENDED_HMS_SYMBOL = "ZZZZZ"; // E.g: +03:00 or -12:00

    // Integers
    public static String DAY_OF_MONTH_1_DIGIT = "d";
    public static String DAY_OF_MONTH_2_DIGITS = "dd";
    public static String DAY_OF_MONTH_3_DIGITS = "ddd";
    public static String DAY_OF_MONTH_4_DIGITS = "dddd";
    public static Map<Integer, String> PADDING_LEADING_ZEROES_TO_DAY_OF_MONTH_SYMBOLS_MAP = ImmutableMap.of(
            0, DAY_OF_MONTH_1_DIGIT,
            1, DAY_OF_MONTH_2_DIGITS,
            2, DAY_OF_MONTH_3_DIGITS,
            3, DAY_OF_MONTH_4_DIGITS
    );
    public static String YEAR_1_DIGIT = "y";
    public static String YEAR_4_DIGITS = "yyyy";
    public static String MONTH_1_DIGIT = "M";
    public static String HOUR_SHORT_SYMBOL = "h";
    public static String HOUR_LONG_SYMBOL = "hh";
    public static String MINUTES_LONG_SYMBOL = "mm";
    public static String MINUTES_SHORT_SYMBOL = "m";
    public static String SECONDS_LONG_SYMBOL = "ss";
    public static String SECONDS_SHORT_SYMBOL = "s";

    // Localized Strings
    public static String MONTH_FORMATTED_ABBREVIATED = "MMM";
    public static String MONTH_FORMATTED_WIDE = "MMMM";
    public static String MONTH_FORMATTED_NARROW = "MMMMM";
    public static String MONTH_STANDALONE_ABBREVIATED = "LLL";
    public static String MONTH_STANDALONE_WIDE = "LLLL";
    public static String MONTH_STANDALONE_NARROW = "LLLLL";
    public static String DAY_OF_WEEK_FORMATTED_NARROW = "EEEEE";
    public static String DAY_OF_WEEK_FORMATTED_ABBREVIATED = "EE";
    public static String DAY_OF_WEEK_FORMATTED_WIDE = "EEEE";
    public static String DAY_OF_WEEK_STANDALONE_ABBREVIATED = "EE";
    public static String DAY_OF_WEEK_STANDALONE_WIDE = "EEEE";
    public static String DAY_OF_WEEK_STANDALONE_NARROW = "EEEEE";
    public static String DAY_PERIOD_FORMATTED_NARROW = "BBBBB";
    public static String DAY_PERIOD_FORMATTED_WIDE = "BBBB";
    public static String DAY_PERIOD_FORMATTED_ABBREVIATED = "B";
    public static String DAY_PERIOD_FORMATTED_ABBREVIATED_AM_PM = "a";
}
