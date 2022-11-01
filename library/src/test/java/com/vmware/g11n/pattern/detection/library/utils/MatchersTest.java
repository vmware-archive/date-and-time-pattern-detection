/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.vmware.g11n.pattern.detection.library.utils.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.StringUtils.EMPTY;

class MatchersTest {

    static final String EXPECTED_MATCHED_INPUT_ERROR_MESSAGE = "Input should be matched. Check Regex expression.";
    static final String EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE = "Input should NOT be matched. Check Regex expression.";
    static final String INCORRECT_GROUP_MATCHED_ERROR_MESSAGE = "Incorrect group was matched. Check Regex expression.";
    static final String MATCHED_GROUP_ERROR_MESSAGE = "No group should be matched. Check Regex expression.";
    static final String INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE = "Incorrect time format matched. Check time formats map.";

    @Test
    void verifyGetTrimmedComponent() {
        Assertions.assertEquals("Trimmed-input", Matchers.getTrimmedComponent("Trimmed-input,"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("Trimmed-input", Matchers.getTrimmedComponent("[Trimmed-input]"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("Trimmed-input", Matchers.getTrimmedComponent("(Trimmed-input)"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectShortGmtFormat() {
        Assertions.assertTrue(Matchers.detectShortGmtFormat("GMT+4"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectShortGmtFormat("GMT-11"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectShortGmtFormat("GMT+03:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectShortGmtFormat("GMT+5:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectShortGmtFormat("GMT-15"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectLongGmtFormat() {
        Assertions.assertTrue(Matchers.detectLongGmtFormat("GMT+04:00"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectLongGmtFormat("GMT-11:43"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectLongGmtFormat("+03:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectLongGmtFormat("GMT+3:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectLongGmtFormat("GMT-09:0"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectBasicHmsFormat() {
        Assertions.assertTrue(Matchers.detectBasicHmsFormat("+0325"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectBasicHmsFormat("-0500"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectBasicHmsFormat("+1159"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectBasicHmsFormat("+3:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectBasicHmsFormat("+1515"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectBasicHmsFormat("-900"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectExtendedHmsFormat() {
        Assertions.assertTrue(Matchers.detectExtendedHmsFormat("+03:25"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectExtendedHmsFormat("-12:00"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectExtendedHmsFormat("+11:59"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectExtendedHmsFormat("+3:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectExtendedHmsFormat("+15:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectExtendedHmsFormat("-9:00"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectMinutesOrSecondsStandaloneShortFormat() {
        Assertions.assertTrue(Matchers.detectMinutesOrSecondsStandaloneShortFormat("5"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectMinutesOrSecondsStandaloneShortFormat("25"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectMinutesOrSecondsStandaloneShortFormat("19"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectMinutesOrSecondsStandaloneShortFormat("05"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectMinutesOrSecondsStandaloneShortFormat("100"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectMinutesOrSecondsStandaloneShortFormat("-21"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectMinutesOrSecondsStandaloneLongFormat() {
        Assertions.assertTrue(Matchers.detectMinutesOrSecondsStandaloneLongFormat("05"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectMinutesOrSecondsStandaloneLongFormat("35"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectMinutesOrSecondsStandaloneLongFormat("59"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectMinutesOrSecondsStandaloneLongFormat("5"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectMinutesOrSecondsStandaloneLongFormat("60"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectMinutesOrSecondsStandaloneLongFormat("0"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectHoursShortStandaloneFormat() {
        Assertions.assertTrue(Matchers.detectHoursShortStandaloneFormat("11"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectHoursShortStandaloneFormat("2"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectHoursShortStandaloneFormat("05"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectHoursShortStandaloneFormat("24"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectHoursShortStandaloneFormat("31"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectHoursLongStandaloneFormat() {
        Assertions.assertTrue(Matchers.detectHoursLongStandaloneFormat("11"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertTrue(Matchers.detectHoursLongStandaloneFormat("02"), EXPECTED_MATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectHoursLongStandaloneFormat("5"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectHoursLongStandaloneFormat("24"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectHoursLongStandaloneFormat("31"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
        Assertions.assertFalse(Matchers.detectHoursLongStandaloneFormat("0"), EXPECTED_UNMATCHED_INPUT_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectDayOfMonthAndGetMatchedGroup() {
        Assertions.assertEquals("31", Matchers.detectDayOfMonthAndGetMatchedGroup("31"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("02", Matchers.detectDayOfMonthAndGetMatchedGroup("02"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("1", Matchers.detectDayOfMonthAndGetMatchedGroup("1"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("009", Matchers.detectDayOfMonthAndGetMatchedGroup("009"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectDayOfMonthAndGetMatchedGroup("35"), MATCHED_GROUP_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectDayOfMonthAndGetMatchedGroup("0"), MATCHED_GROUP_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectDayOfMonthAndGetMatchedGroup("99"), MATCHED_GROUP_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectDayOfMonthAndGetMatchedGroup("4.5"), MATCHED_GROUP_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectYearAndGetMatchedGroup() {
        Assertions.assertEquals("2001", Matchers.detectYearAndGetMatchedGroup("2001"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("1901", Matchers.detectYearAndGetMatchedGroup("1901"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("2999", Matchers.detectYearAndGetMatchedGroup("2999"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectYearAndGetMatchedGroup("35"), MATCHED_GROUP_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectYearAndGetMatchedGroup("-3221"), MATCHED_GROUP_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectYearAndGetMatchedGroup("9999"), MATCHED_GROUP_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectMonthAndGetMatchedGroup() {
        Assertions.assertEquals("12", Matchers.detectMonthAndGetMatchedGroup("12"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("1", Matchers.detectMonthAndGetMatchedGroup("1"), INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectMonthAndGetMatchedGroup("13"), MATCHED_GROUP_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectMonthAndGetMatchedGroup("0"), MATCHED_GROUP_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectSingleQuotationWordsAndGetMatchedGroup() {
        Assertions.assertEquals("'at'", Matchers.detectSingleQuotationWordsAndGetMatchedGroup("November 12, 2021, 'at' 1:35 PM"),
                INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("'word'", Matchers.detectSingleQuotationWordsAndGetMatchedGroup("Test \" other examples `test`. 'word'"),
                INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.detectSingleQuotationWordsAndGetMatchedGroup("\"Test other examples `test`.\""),
                INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
    }

    @Test
    void verifyDetectDateCharacterSeparatorAndGetMatchedGroup() {
        Assertions.assertEquals("-", Matchers.detectDateCharacterSeparatorAndGetMatchedGroup("2021-15-10"),
                INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals(".", Matchers.detectDateCharacterSeparatorAndGetMatchedGroup("11.15.2022"),
                INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("/", Matchers.detectDateCharacterSeparatorAndGetMatchedGroup("2021/15/10"),
                INCORRECT_GROUP_MATCHED_ERROR_MESSAGE);
    }


    @Test
    void verifyGetMatchedTimeOrPattern() {
        Assertions.assertEquals("h:mm", Matchers.getMatchedTimeOrPattern("5:15", Matchers.formattedTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("h:mm:ss", Matchers.getMatchedTimeOrPattern("1:25:55", Matchers.formattedTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("hh:mm:ss", Matchers.getMatchedTimeOrPattern("01:25:55", Matchers.formattedTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("h:mm:ss", Matchers.getMatchedTimeOrPattern("35:25:55", Matchers.formattedTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("HH:mm:ss", Matchers.getMatchedTimeOrPattern("15:15:15", Matchers.standaloneTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("HH:mm", Matchers.getMatchedTimeOrPattern("06:59", Matchers.standaloneTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("H:mm", Matchers.getMatchedTimeOrPattern("6:59", Matchers.standaloneTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals(EMPTY, Matchers.getMatchedTimeOrPattern("95:66", Matchers.formattedTimePatternsToRegexMap, true), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);

        Assertions.assertEquals("1:50:10", Matchers.getMatchedTimeOrPattern("31:50:100Testing", Matchers.standaloneTimePatternsToRegexMap, false), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
        Assertions.assertEquals("5:12", Matchers.getMatchedTimeOrPattern("Test-55:12:Testing", Matchers.formattedTimePatternsToRegexMap, false), INCORRECT_TIME_FORMAT_MATCHED_ERROR_MESSAGE);
    }
}