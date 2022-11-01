/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.patterns;

import com.vmware.g11n.pattern.detection.library.patterns.PatternValidators;
import com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

import static com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType.*;
import static java.util.Locale.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PatternValidatorsTest {

    @ParameterizedTest
    @MethodSource(value = "validPatternsArguments")
    @DisplayName("Assert isValidPatternMethod works by providing valid dates and time")
    void isValidPatternPositiveTest(String pattern, String value, Locale locale, LocalizedType type) {
        Assertions.assertTrue(PatternValidators.isValidPattern(pattern, value, locale, type));
    }

    @ParameterizedTest
    @MethodSource(value = "invalidPatternsArguments")
    @DisplayName("Assert isValidPatternMethod works by providing invalid dates and time and expect false")
    void isValidPatternNegativeTest(String pattern, String value, Locale locale, LocalizedType type) {
        Assertions.assertFalse(PatternValidators.isValidPattern(pattern, value, locale, type));
    }

    private static Stream<Arguments> validPatternsArguments() {
        return Stream.of(
                arguments("yyyy-MM-dd", "2012-05-21", TRADITIONAL_CHINESE, DATE),
                arguments("yyyy-MM-dd'T'HH:mm:ss", "2012-05-21T21:25:10", TRADITIONAL_CHINESE, DATE_TIME),
                arguments("y년 MMM d일 EEEE BBBBB h시 m분 s초 zzzz", "2016년 9월 1일 목요일 AM 11시 7분 10초 그리니치 표준시", KOREAN, DATE_TIME),
                arguments("d MMM. yyyy", "13 abr. 2022", forLanguageTag("es-ES"), DATE),
                arguments("y年M月d日EEEE ah:mm O", "2016年9月1日星期四 上午4:07 GMT-1", SIMPLIFIED_CHINESE, DATE_TIME),
                arguments("d MMM yyyy HH:mm v", "1 Sep 2016 04:07 PT", US, DATE_TIME),
                arguments("y/M/dE", "2022/2/8周二", SIMPLIFIED_CHINESE, DATE),
                arguments("EEEE h:mm:ss a", "Monday 7:33:00 PM", US, TIME)
        );
    }

    private static Stream<Arguments> invalidPatternsArguments(){
        return Stream.of(
                arguments("y/M/dE", "2022/2/8周二", SIMPLIFIED_CHINESE, DATE_TIME),
                arguments("d. 'letzten Monat'", "26. letzten Monat", GERMANY, DATE),
                arguments("HH:mm 'at' B", "13:30 at noon", ENGLISH, TIME),
                arguments("yyyy-MM-dd", "2012-05-21", TRADITIONAL_CHINESE, DATE_TIME)
        );
    }
}