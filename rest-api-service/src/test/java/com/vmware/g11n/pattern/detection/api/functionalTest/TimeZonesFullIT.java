/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.google.common.collect.ImmutableList;
import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;
import com.vmware.g11n.pattern.detection.model.serviceData.BatchValidationRequest;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

import static com.vmware.g11n.pattern.detection.library.data.DateFieldSymbols.*;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.VALID_NON_STANDARD_DATE_TIME_MESSAGE;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.VALID_STANDARD_DATE_TIME_MESSAGE;

public class TimeZonesFullIT {

    /*
    Verify Specific non-location formats: z and zzzz (example: PT(z) and Pacific Daylight Time (zzzz)
     */
    @Test
    public void verifySpecificNonLocation() {
        BatchValidationRequest requests_DE = BatchValidationRequest.builder()
                .locale("en-GB")
                .inputs(ImmutableList.of("Thursday 1 September 2016 04:07:10 EEST", "Thursday 1 September 2016 04:07:10 Pacific Daylight Time"))
                .build();

        BatchValidationRequest requests_ES = BatchValidationRequest.builder()
                .locale("es-ES")
                .inputs(ImmutableList.of("1 septiembre 2016 14:07:10 hora de verano del Pacífico", "1/9/16 4:07:10 hora de verano del Pacífico"))
                .build();

        BatchValidationRequest requests_CN = BatchValidationRequest.builder()
                .locale("zh-CN")
                .inputs(ImmutableList.of("2016年9月1日星期四 协调世界时 上午4:07:10"))
                .build();

        List<ValidationResult> validationList = PayloadBuilder.validateInputBatch(ImmutableList.of(requests_DE,
                requests_ES, requests_CN));

        validationList.forEach(
                response -> {
                    Assert.isTrue(response.isLocalizedContent, "The input should be localized");
                    Assert.isTrue(!response.detectedPattern.pattern.isEmpty(), "The pattern should not be empty.");
                    Assert.isTrue(response.detectedPattern.pattern.contains(SHORT_SPECIFIC_NON_LOCATION_TIME_ZONE_SYMBOL), "The pattern should contains correct symbol");
                    Assert.isTrue(response.detectedPattern.patternInfoMessage.equals(VALID_NON_STANDARD_DATE_TIME_MESSAGE)
                            || response.detectedPattern.patternInfoMessage.equals(VALID_STANDARD_DATE_TIME_MESSAGE), "The pattern should be valid date + time");
                }
        );
    }

    /*
    Verify localized GMT formats: ZZZZ and O (example: GMT-3(O) and GMT+03:00(ZZZZ)
     */
    @Test
    public void verifyLocalizedGmt() {
        BatchValidationRequest requests_DE = BatchValidationRequest.builder()
                .locale("en-GB")
                .inputs(ImmutableList.of("Thursday 1 September 2016 04:07:10 GMT+3", "Thursday 1 September 2016 04:07:10 GMT+11:00"))
                .build();

        BatchValidationRequest requests_CN = BatchValidationRequest.builder()
                .locale("zh-CN")
                .inputs(ImmutableList.of("2016年9月1日星期四 上午4:07 GMT-1"))
                .build();

        List<ValidationResult> validationList = PayloadBuilder.validateInputBatch(ImmutableList.of(requests_DE, requests_CN));

        validationList.forEach(
                response -> {
                    Assert.isTrue(response.isLocalizedContent, "The input should be localized");
                    Assert.isTrue(!response.detectedPattern.pattern.isEmpty(), "The pattern should not be empty.");
                    Assert.isTrue(response.detectedPattern.pattern.contains(SHORT_LOCALIZED_GMT_SYMBOL)
                            || response.detectedPattern.pattern.contains(LONG_LOCALIZED_GMT_SYMBOL), "The pattern should contains correct symbol");
                    Assert.isTrue(response.detectedPattern.patternInfoMessage.equals(VALID_NON_STANDARD_DATE_TIME_MESSAGE)
                            || response.detectedPattern.patternInfoMessage.equals(VALID_STANDARD_DATE_TIME_MESSAGE), "The pattern should be valid date + time");
                }
        );
    }

    /**
     * Verify Basic HMS ISO8601 format (Z). Example: -0800 or +1020
     */
    @Test
    public void verifyBasicHms() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2016年9月1日星期四 04:07:10 -0800");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y年M月d日EEEE HH:mm:ss Z"),
                "The pattern should be correct without any spacings");
    }

    /**
     * Verify Extended HMS ISO8601 format (ZZZZZ). Example: -08:00 or +11:00
     */
    @Test
    public void verifyExtendedHms() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2016年9月1日星期四 04:07:10 +10:11");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y年M月d日EEEE HH:mm:ss ZZZZZ"),
                "The pattern should be correct without any spacings");
    }

    /**
     * Verify Generic Non-location format - short (v).
     */
    @Test
    public void verifyGenericShortNonLocation() {
        ValidationResult response = PayloadBuilder.validateInput("en-US", "1 Sep 2016 04:07 PT");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("d MMM yyyy HH:mm v"),
                "The pattern should be correct without any spacings");
    }

    /**
     * Verify Generic Non-location format - long (vvvv).
     */
    @Test
    public void verifyGenericLongNonLocation() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2016/9/1 上午4:07 北美太平洋时间");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y/M/d ah:mm vvvv"),
                "The pattern should be correct without any spacings");
    }

    /**
     * Verify Exemplar city (VVVV)
     */
    @Test
    public void verifyExemplarCity() {
        ValidationResult response = PayloadBuilder.validateInput("fr-FR", "21 septembre 2016 04:07:10 Los Angeles");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("d MMMM yyyy HH:mm:ss VVV"),
                "The pattern should be correct without any spacings");
    }
}
