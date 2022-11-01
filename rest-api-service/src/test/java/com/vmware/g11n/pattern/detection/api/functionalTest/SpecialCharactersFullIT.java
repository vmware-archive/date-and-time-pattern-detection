/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.VALID_NON_STANDARD_DATE_TIME_MESSAGE;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.INCORRECT_FORMAT_SEPARATOR;

public class SpecialCharactersFullIT {

    // Verify abbreviated months which contain valid period (".") in the localized String
    @Test
    public void validTrailingPeriodLocalizedString() {
        ValidationResult response = PayloadBuilder.validateInput("de-DE", "Feb. 21. 2021");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.pattern.equals("MMM d. yyyy"), "The pattern should be correct.");
    }

    // Verify abbreviated months which normally doesn't contain period (".") in the localized String
    @Test
    public void invalidTrailingPeriodLocalizedString() {
        ValidationResult response = PayloadBuilder.validateInput("es-ES", "13 abr. 2022");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.pattern.equals("d MMM. yyyy"), "The pattern should be correct.");
    }

    // Verify day of month with trailing period
    @Test
    public void trailingPeriodDayOfMonth() {
        ValidationResult response = PayloadBuilder.validateInput("de-DE", "Mittwoch, 004. Februar, 2022 16:57:00");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("EEEE, ddd. MMMM, yyyy HH:mm:ss"),
                "The pattern should be correct.");
    }

    // Verify non-localized element with trailing period + comma
    @Test
    public void trailingPeriodWithCommaNonLocalizedString() {
        ValidationResult response = PayloadBuilder.validateInput("de-DE", "Feb 21., 2021");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.pattern.equals("LLL d., yyyy"), "The pattern should be correct.");
    }

    // Verify input containing spacings
    @Test
    public void inputWithWhiteSpaces() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN",
                "   2022年3月16日星期三       GMT+2      下午8:01:00   ");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.pattern.equals("y年M月d日EEEE O ah:mm:ss"),
                "The pattern should be correct without any spacings");
    }

    // Verify input containing spacings
    @Test
    public void inputWithCommas() {
        ValidationResult response = PayloadBuilder.validateInput("en-US",
                "Thursday, March, 31, 2022, 1:33:00 AM, America/Argentina/San_Juan");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("EEEE, MMMM, d, yyyy, h:mm:ss a, VV"),
                "The pattern should be correct without any spacings");
    }

    // Verify input containing component surrounded in brackets
    @Test
    public void inputWithBrackets() {
        ValidationResult response = PayloadBuilder.validateInput("es-ES", "16/3/22 20:01:00 (GMT+2)");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("d/M/yy H:mm:ss (O)"),
                "The pattern should be correct without any spacings");
    }

    // Verify CLDR-valid date format with different character separator
    @Test
    public void dateInputWithNonStandardCharacterSeparator() {
        ValidationResult response = PayloadBuilder.validateInput("it-IT", "14.05.21 21:14");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(!response.detectedPattern.isStandardFormat, "The pattern should NOT be standard.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("dd.MM.yy HH:mm"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(!response.suggestions.get(INCORRECT_FORMAT_SEPARATOR).isEmpty());
    }
}
