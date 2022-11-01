/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class Matchers {

    public static ImmutableMap<String, String> formattedTimePatternsToRegexMap = ImmutableMap.<String, String>builder()
            .put("hh:mm:ss", "(1[0-2]|0[1-9]):([0-5]?[0-9]):([0-5]?[0-9])")
            .put("h:mm:ss", "(1[0-2]|[1-9]):([0-5][0-9]):([0-5]?[0-9])")
            .put("hh:mm", "(1[0-2]|0[1-9]):([0-5]?[0-9])")
            .put("h:mm", "(1[0-2]|[1-9]):([0-5][0-9])")
            .build();

    public static ImmutableMap<String, String> standaloneTimePatternsToRegexMap = ImmutableMap.<String, String>builder()
            .put("HH:mm:ss", "(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5]?[0-9])")
            .put("H:mm:ss", "(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])")
            .put("HH:mm", "(2[0-3]|[01][0-9]):([0-5][0-9])")
            .put("H:mm", "(2[0-3]|[01]?[0-9]):([0-5]?[0-9])")
            .build();

    public static String detectDayOfMonthAndGetMatchedGroup(String input) {
        Pattern p = Pattern.compile("^(([0]{0,3}[1-9])|([0]{0,2}[1-2][0-9])|([0]{0,2}3[01]))\\.?$");
        Matcher m = p.matcher(input);

        return m.find() ? m.group() : EMPTY;
    }

    public static String detectYearAndGetMatchedGroup(String input) {
        Pattern p = Pattern.compile("^[12][0-9]{3}\\.?$");
        Matcher m = p.matcher(input);

        return m.find() ? m.group() : EMPTY;
    }

    public static String detectMonthAndGetMatchedGroup(String input) {
        Pattern p = Pattern.compile("^(1[0-2]|[1-9])\\.?$");
        Matcher m = p.matcher(input);

        return m.find() ? m.group() : EMPTY;
    }

    public static String getMatchedTimeOrPattern(String input, Map<String, String> timePatterns, boolean returnPattern) {
        for (Map.Entry<String, String> entry : timePatterns.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getValue());
            Matcher m = pattern.matcher(input);
            if (m.find()) {
                return returnPattern ? entry.getKey() : m.group();
            }
        }
        return EMPTY;
    }

    //TODO: add support for combined chars. Example input: "[Element-to-be-trimmed],"
    public static String getTrimmedComponent(String input) {
        Pattern pattern = Pattern.compile("([,\\[\\])(])");
        Matcher m = pattern.matcher(input);

        while (m.find()) {
            String matchedChar = m.group();
            if (input.startsWith(matchedChar) || input.endsWith(matchedChar)) {
                input = input.replace(matchedChar, "");
            }
        }

        return input;
    }

    public static boolean detectShortGmtFormat(String input) {
        Pattern p = Pattern.compile("^GMT[+-]([1-9]|1[0-2])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static boolean detectLongGmtFormat(String input) {
        Pattern p = Pattern.compile("^GMT[+-](1[0-2]|0[0-9]):(0[0-9]|[1-5][0-9])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static boolean detectBasicHmsFormat(String input) {
        Pattern p = Pattern.compile("^[+-](1[0-2]|0[0-9])(0[0-9]|[1-5][0-9])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static boolean detectExtendedHmsFormat(String input) {
        Pattern p = Pattern.compile("^[+-](1[0-2]|0[0-9]):(0[0-9]|[1-5][0-9])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static boolean detectMinutesOrSecondsStandaloneShortFormat(String input) {
        Pattern p = Pattern.compile("^([1-5][0-9]|[0-9])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static boolean detectMinutesOrSecondsStandaloneLongFormat(String input) {
        Pattern p = Pattern.compile("^(0[0-9]|[1-5][0-9])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static boolean detectHoursShortStandaloneFormat(String input) {
        Pattern p = Pattern.compile("^(1[0-2]|[0-9])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static boolean detectHoursLongStandaloneFormat(String input) {
        Pattern p = Pattern.compile("^(1[0-2]|0[0-9])$");
        Matcher m = p.matcher(input);

        return m.find();
    }

    public static String detectSingleQuotationWordsAndGetMatchedGroup(String input) {
        Pattern p = Pattern.compile("'.*?'\\.?");
        Matcher m = p.matcher(input);

        if (m.find()) {
            return m.group();
        } else {
            return EMPTY;
        }
    }

    public static String detectDateCharacterSeparatorAndGetMatchedGroup(String input) {
        Pattern p = Pattern.compile("[./\\-,]");
        Matcher m = p.matcher(input);

        if (m.find()) {
            return m.group();
        } else {
            return EMPTY;
        }
    }
}
