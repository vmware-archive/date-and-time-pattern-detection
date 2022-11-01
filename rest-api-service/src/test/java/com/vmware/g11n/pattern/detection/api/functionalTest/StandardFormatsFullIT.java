/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import com.vmware.g11n.pattern.detection.model.serviceData.ConversionResult;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static org.springframework.util.Assert.isTrue;

public class StandardFormatsFullIT {

    @Test
    public void verifyStandardDates() {
        List<String> supportedLocales = PayloadBuilder.getSupportedLocales();

        supportedLocales.forEach(locale -> {
            DateFormats dateFormatsForCurrentLocale = PayloadBuilder.getStandardDateFormats(locale);
            LocalDate localDate = LocalDate.now();
            dateFormatsForCurrentLocale.getAllDateFormatsAsMap().values().forEach(format -> {
                String formattedInput = localDate.format(DateTimeFormatter.ofPattern(format, Locale.forLanguageTag(locale)));
                ValidationResult response = PayloadBuilder.validateInput(locale, formattedInput);
                isTrue(response.detectedPattern.isValidDate, "The detected pattern should be valid Date");
                isTrue(response.detectedPattern.isStandardFormat, "The detected pattern should be standard");
                isTrue(dateFormatsForCurrentLocale.getAllDateFormatsAsMap().containsValue(response.detectedPattern.pattern), "Expected pattern: " + format);
                isTrue(response.detectedPattern.patternInfoMessage.equals(VALID_STANDARD_DATE_MESSAGE), "The message should be standard Date");
            });
        });
    }

    @Test
    public void verifyStandardTime() {
        List<String> supportedLocales = PayloadBuilder.getSupportedLocales();

        supportedLocales.forEach(locale -> {
            TimeFormats timeFormatsForCurrentLocale = PayloadBuilder.getStandardTimeFormats(locale);
            ZonedDateTime localDate = ZonedDateTime.now();
            timeFormatsForCurrentLocale.getAllTimeFormatsAsMap().values().forEach(format -> {
                String formattedInput = localDate.format(DateTimeFormatter.ofPattern(format, Locale.forLanguageTag(locale)));
                ValidationResult response = PayloadBuilder.validateInput(locale, formattedInput);
                isTrue(!response.detectedPattern.isValidDate, "The detected pattern should not be valid Date");
                isTrue(response.detectedPattern.isStandardFormat, "The detected pattern should be standard");
                isTrue(timeFormatsForCurrentLocale.getAllTimeFormatsAsMap().containsValue(response.detectedPattern.pattern), "Expected pattern: " + format);
                isTrue(response.detectedPattern.patternInfoMessage.equals(VALID_STANDARD_TIME_MESSAGE), "The message should be standard Time");
            });
        });
    }

    @Test
    public void verifyStandardDateTime() {
        List<String> supportedLocales = PayloadBuilder.getSupportedLocales();

        supportedLocales.forEach(locale -> {
            Map<String, String> dateTimeFormatsForCurrentLocale = PayloadBuilder.getStandardDateTimeFormats(locale);
            ZonedDateTime localDate = ZonedDateTime.now();
            dateTimeFormatsForCurrentLocale.values().forEach(format -> {
                String formattedInput = localDate.format(DateTimeFormatter.ofPattern(format, Locale.forLanguageTag(locale)));
                ValidationResult response = PayloadBuilder.validateInput(locale, formattedInput);
                isTrue(response.detectedPattern.isValidDate, "The detected pattern should be valid Date");
                isTrue(response.detectedPattern.isStandardFormat, "The detected pattern should be standard");
                isTrue(dateTimeFormatsForCurrentLocale.containsValue(response.detectedPattern.pattern), "Expected pattern: " + format);
                isTrue(response.detectedPattern.patternInfoMessage.equals(VALID_STANDARD_DATE_TIME_MESSAGE), "The message should be standard date + Time");
            });
        });
    }

    @Test
    public void verifyConversionOfStandardDates() {
        List<String> supportedLocales = PayloadBuilder.getSupportedLocales();

        supportedLocales.forEach(locale -> {
            DateFormats dateFormatsForCurrentLocale = PayloadBuilder.getStandardDateFormats(locale);
            LocalDate localDate = LocalDate.now();
            dateFormatsForCurrentLocale.getAllDateFormatsAsMap().values().forEach(format -> {
                String formattedInput = localDate.format(DateTimeFormatter.ofPattern(format, Locale.forLanguageTag(locale)));

                ConversionResult conversionResult = PayloadBuilder.convertInput(locale,
                        supportedLocales.get(new Random().nextInt(supportedLocales.size())), formattedInput);

                isTrue(conversionResult.isSourceContentLocalized, "The source input should be detected as localized.");
                isTrue(conversionResult.isTargetContentLocalized, "The target input should be detected as localized.");
                isTrue(conversionResult.sourcePattern.isStandardFormat, "The source pattern should be detected as standard.");
                isTrue(conversionResult.targetPattern.isStandardFormat, "The target pattern should be detected as standard.");
                isTrue(conversionResult.conversionErrors.isEmpty(), "No errors should be present");
                isTrue(conversionResult.sourcePattern.patternInfoMessage.equals(VALID_STANDARD_DATE_MESSAGE),
                        "The pattern message should be correct");
                isTrue(conversionResult.targetPattern.patternInfoMessage.equals(VALID_STANDARD_DATE_MESSAGE),
                        "The pattern message should be correct");
                isTrue(conversionResult.sourcePattern.isValidDate, "The source pattern should be detected as date.");
                isTrue(conversionResult.targetPattern.isValidDate, "The target pattern should be detected as date.");
            });
        });
    }

    @Test
    public void verifyConversionOfStandardTimes() {
        List<String> supportedLocales = PayloadBuilder.getSupportedLocales();

        supportedLocales.forEach(locale -> {
            TimeFormats timeFormatsForCurrentLocale = PayloadBuilder.getStandardTimeFormats(locale);
            ZonedDateTime localDateTime = ZonedDateTime.now();
            timeFormatsForCurrentLocale.getAllTimeFormatsAsMap().values().forEach(format -> {
                String formattedInput = localDateTime.format(DateTimeFormatter.ofPattern(format, Locale.forLanguageTag(locale)));

                ConversionResult conversionResult = PayloadBuilder.convertInput(locale,
                        supportedLocales.get(new Random().nextInt(supportedLocales.size())), formattedInput);

                isTrue(conversionResult.isSourceContentLocalized, "The source input should be detected as localized.");
                isTrue(conversionResult.isTargetContentLocalized, "The target input should be detected as localized.");
                isTrue(conversionResult.sourcePattern.isStandardFormat, "The source pattern should be detected as standard.");
                isTrue(conversionResult.targetPattern.isStandardFormat, "The target pattern should be detected as standard.");
                isTrue(conversionResult.conversionErrors.isEmpty(), "No errors should be present");
                isTrue(conversionResult.sourcePattern.patternInfoMessage.equals(VALID_STANDARD_TIME_MESSAGE),
                        "The pattern message should be correct");
                isTrue(conversionResult.targetPattern.patternInfoMessage.equals(VALID_STANDARD_TIME_MESSAGE),
                        "The pattern message should be correct");
                isTrue(!conversionResult.sourcePattern.isValidDate, "The source pattern should NOT be detected as date.");
                isTrue(!conversionResult.targetPattern.isValidDate, "The target pattern should NOT be detected as date.");
            });
        });
    }

    @Test
    public void verifyConversionOfStandardDateTimes() {
        List<String> supportedLocales = PayloadBuilder.getSupportedLocales();

        supportedLocales.forEach(locale -> {
            Map<String, String> dateTimeFormatsForCurrentLocale = PayloadBuilder.getStandardDateTimeFormats(locale);
            ZonedDateTime localDateTime = ZonedDateTime.now();
            dateTimeFormatsForCurrentLocale.values().forEach(format -> {
                String formattedInput = localDateTime.format(DateTimeFormatter.ofPattern(format, Locale.forLanguageTag(locale)));

                ConversionResult conversionResult = PayloadBuilder.convertInput(locale,
                        supportedLocales.get(new Random().nextInt(supportedLocales.size())), formattedInput);

                isTrue(conversionResult.isSourceContentLocalized, "The source input should be detected as localized.");
                isTrue(conversionResult.isTargetContentLocalized, "The target input should be detected as localized.");
                isTrue(conversionResult.sourcePattern.isStandardFormat, "The source pattern should be detected as standard.");
                isTrue(conversionResult.targetPattern.isStandardFormat, "The target pattern should be detected as standard.");
                isTrue(conversionResult.conversionErrors.isEmpty(), "No errors should be present");
                isTrue(conversionResult.sourcePattern.patternInfoMessage.equals(VALID_STANDARD_DATE_TIME_MESSAGE),
                        "The pattern message should be correct");
                isTrue(conversionResult.targetPattern.patternInfoMessage.equals(VALID_STANDARD_DATE_TIME_MESSAGE),
                        "The pattern message should be correct");
                isTrue(conversionResult.sourcePattern.isValidDate, "The source pattern should be detected as date.");
                isTrue(conversionResult.targetPattern.isValidDate, "The target pattern should be detected as date.");
            });
        });
    }
}
