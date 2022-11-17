/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.google.common.collect.ImmutableList;
import com.vmware.g11n.pattern.detection.library.DateTimePatternDetection;
import com.vmware.g11n.pattern.detection.model.CldrData;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidatedPattern;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import static com.vmware.g11n.pattern.detection.library.utils.CldrProcessors.getValidatedPatternFromCldrFormats;
import static com.vmware.g11n.pattern.detection.library.utils.ResourceLoaders.loadCldrData;
import static org.junit.jupiter.api.Assertions.*;

class CldrProcessorsTest {

    @Test
    void verifyDetectPatternFromCldrFormatsStandardDateInputs() {
        DateTimePatternDetection dateTimePatternDetection = new DateTimePatternDetection();

        dateTimePatternDetection.getSupportedLocales().forEach(localeAsString -> {
            Locale currentLocale = Locale.forLanguageTag(localeAsString);
            CldrData cldrDataForCurrentLocale = loadCldrData(currentLocale);
            DateFormats dateFormatsForCurrentLocale = dateTimePatternDetection.getStandardCldrDatePatterns(localeAsString);
            LocalDate localDate = LocalDate.now();

            dateFormatsForCurrentLocale.getAllDateFormatsAsMap().values().forEach(format -> {
                String formattedInput = localDate.format(DateTimeFormatter.ofPattern(format, currentLocale));
                ValidatedPattern validatedPattern = getValidatedPatternFromCldrFormats(cldrDataForCurrentLocale, formattedInput, currentLocale);
                assertTrue(validatedPattern.isValidDate, "The detected pattern should be valid Date");
                assertTrue(validatedPattern.isStandardFormat, "The detected pattern should be standard");
            });
        });
    }

    @Test
    void verifyDetectPatternFromCldrFormatsStandardTimeInputs() {
        DateTimePatternDetection dateTimePatternDetection = new DateTimePatternDetection();

        dateTimePatternDetection.getSupportedLocales().forEach(localeAsString -> {
            Locale currentLocale = Locale.forLanguageTag(localeAsString);
            CldrData cldrDataForCurrentLocale = loadCldrData(currentLocale);

            TimeFormats timeFormatsForCurrentLocale = dateTimePatternDetection.getStandardCldrTimePatterns(localeAsString);
            ZonedDateTime localDate = ZonedDateTime.now();

            timeFormatsForCurrentLocale.getAllTimeFormatsAsMap().values().forEach(format -> {
                String formattedInput = localDate.format(DateTimeFormatter.ofPattern(format, currentLocale));
                ValidatedPattern validatedPattern = getValidatedPatternFromCldrFormats(cldrDataForCurrentLocale, formattedInput, currentLocale);
                assertFalse(validatedPattern.isValidDate, "The detected pattern should NOT be valid Date");
                assertTrue(validatedPattern.isStandardFormat, "The detected pattern should be standard");
            });
        });
    }

    @Test
    void verifyDetectPatternFromCldrFormatsStandardDateTimeInputs() {
        DateTimePatternDetection dateTimePatternDetection = new DateTimePatternDetection();

        dateTimePatternDetection.getSupportedLocales().forEach(localeAsString -> {
            Locale currentLocale = Locale.forLanguageTag(localeAsString);
            CldrData cldrDataForCurrentLocale = loadCldrData(currentLocale);

            Map<String, String> dateTimeFormatsForCurrentLocale = dateTimePatternDetection.getStandardCldrDateTimePatterns(localeAsString);
            ZonedDateTime localDate = ZonedDateTime.now();

            dateTimeFormatsForCurrentLocale.values().forEach(format -> {
                String formattedInput = localDate.format(DateTimeFormatter.ofPattern(format, currentLocale));
                ValidatedPattern validatedPattern = getValidatedPatternFromCldrFormats(cldrDataForCurrentLocale, formattedInput, currentLocale);
                assertTrue(validatedPattern.isValidDate, "The detected pattern should be valid Date");
                assertTrue(validatedPattern.isStandardFormat, "The detected pattern should be standard");
            });
        });
    }

    @Test
    void verifyDetectNonStandardTemporalCldrFormats() {
        CldrData cldrDataCN = loadCldrData(Locale.SIMPLIFIED_CHINESE);
        ValidatedPattern validatedPattern = getValidatedPatternFromCldrFormats(cldrDataCN, "2022年第4季度", Locale.SIMPLIFIED_CHINESE);

        assertFalse(validatedPattern.isStandardFormat, "The detected pattern should NOT be standard.");
        assertFalse(validatedPattern.isValidDate, "The detected pattern should NOT be valid Date.");
        assertEquals("y年第Q季度", validatedPattern.pattern, "Detected pattern is not correct.");
    }

    @Test
    void verifyDetectNonStandardDateCldrFormats() {
        CldrData cldrDataCN = loadCldrData(Locale.SIMPLIFIED_CHINESE);
        ImmutableList.of("2022/11/16周三", "公元2022年11月16日", "2022年11月16日周三").forEach(
                input -> {
                    ValidatedPattern validatedPattern = getValidatedPatternFromCldrFormats(cldrDataCN, input, Locale.SIMPLIFIED_CHINESE);
                    assertFalse(validatedPattern.isStandardFormat, "The detected pattern should NOT be standard.");
                    assertTrue(validatedPattern.isValidDate, "The detected pattern should be valid Date.");
                }
        );
    }

    @Test
    void verifyDetectNonStandardTimeCldrFormats() {
        CldrData cldrDataES = loadCldrData(Locale.forLanguageTag("es-ES"));
        ValidatedPattern validatedPattern = getValidatedPatternFromCldrFormats(cldrDataES, "11:27 de la mañana", Locale.forLanguageTag("es-ES"));

        assertFalse(validatedPattern.isStandardFormat, "The detected pattern should NOT be standard.");
        assertFalse(validatedPattern.isValidDate, "The detected pattern should NOT be valid Date.");
        assertEquals("h:mm B", validatedPattern.pattern, "Detected pattern is not correct.");
    }
}