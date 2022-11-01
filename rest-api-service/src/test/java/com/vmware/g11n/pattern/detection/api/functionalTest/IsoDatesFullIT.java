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
import static com.vmware.g11n.pattern.detection.library.utils.IsoProcessors.*;

public class IsoDatesFullIT {

    @Test
    public void standardIsoDate() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2012-05-21");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(ISO_DATE_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("yyyy-MM-dd"), "The pattern should be correct ISO format.");
        Assert.isTrue(response.detectedPattern.isStandardFormat, "The pattern should be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    @Test
    public void standardIsoDateTime() {
        ValidationResult response = PayloadBuilder.validateInput("es-ES", "2012-05-21T21:25:10");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(ISO_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("yyyy-MM-dd'T'HH:mm:ss"), "The pattern should be correct ISO format.");
        Assert.isTrue(response.detectedPattern.isStandardFormat, "The pattern should be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    @Test
    public void standardOrdinalDateTime() {
        ValidationResult response = PayloadBuilder.validateInput("fr-FR", "2021-215");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(ISO_ORDINAL_DATE_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("yyyy-D"), "The pattern should be correct ISO format.");
        Assert.isTrue(response.detectedPattern.isStandardFormat, "The pattern should be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    @Test
    public void standardOffsetDateTime() {
        ValidationResult response = PayloadBuilder.validateInput("ko-KR", "2021-11-04T21:20+04:00");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(ISO_DATE_TIME_OFFSET_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("yyyy-MM-dd'T'HH:mm:ssZZZZZ"), "The pattern should be correct ISO format.");
        Assert.isTrue(response.detectedPattern.isStandardFormat, "The pattern should be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    @Test
    public void standardOffsetTime() {
        ValidationResult response = PayloadBuilder.validateInput("bg-BG", "21:20:11-04:00");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(ISO_OFFSET_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("HH:mm:ssZZZZZ"), "The pattern should be correct ISO format.");
        Assert.isTrue(response.detectedPattern.isStandardFormat, "The pattern should be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    @Test
    public void standardZonedOffsetDateTime() {
        ValidationResult response = PayloadBuilder.validateInput("it-IT", "1999-05-02T21:20:11-04:00[Europe/Paris]");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(ISO_ZONED_DATE_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("yyyy-MM-dd'T'HH:mm:ssZZZZZ'['VV']'"),
                "The pattern should be correct ISO format.");
        Assert.isTrue(response.detectedPattern.isStandardFormat, "The pattern should be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    @Test
    public void nonStandardOffsetDateTime() {
        ValidationResult response = PayloadBuilder.validateInput("en-US", "2021-11-04T21:21:00, Thursday");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("yyyy-MM-dd'T'HH:mm:ss, EEEE"),
                "The pattern should be correct ISO format.");
        Assert.isTrue(!response.detectedPattern.isStandardFormat, "The pattern should NOT be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }
}
