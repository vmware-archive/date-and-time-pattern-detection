/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.google.common.collect.ImmutableList;
import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;
import com.vmware.g11n.pattern.detection.model.serviceData.BatchValidationRequest;
import com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.INCORRECT_CASING_SUGGESTION;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.VALID_NON_STANDARD_DATE_TIME_MESSAGE;

public class ValidatorSmokeFullIT {

    @Test
    public void simpleDates() {
        ValidationResult response = PayloadBuilder.validateInput("it-IT", "31/8/2029");

        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.isLocalizedContent, "The input should be marked as localized.");
        Assert.isTrue(!response.detectedPattern.pattern.isEmpty(), "The date should be marked as valid.");
    }

    @Test
    public void validDatesBatch() {
        BatchValidationRequest requests_DE = BatchValidationRequest.builder()
                .locale("de-DE")
                .inputs(ImmutableList.of("Mittwoch, 9 Februar, 2022 16:57:00"))
                .build();

        BatchValidationRequest requests_ES = BatchValidationRequest.builder()
                .locale("es-ES")
                .inputs(ImmutableList.of("2029 31 viernes ago 3:51:00", "dom, 2 ene, 2022 19:51", "vie, 31 ago 2029",
                        "2029 31 viernes ago 3:51:00", "viernes, 31 de agosto de 2029", "17 ene 2022 7:22:05"))
                .build();

        BatchValidationRequest requests_TW = BatchValidationRequest.builder()
                .locale("zh-TW")
                .inputs(ImmutableList.of("2022年1月18日, 星期二", "2022年1月18日, 星期二 下午3:52:00", "2022年1月18日 下午3:52:00"))
                .build();

        BatchValidationRequest requests_CN = BatchValidationRequest.builder()
                .locale("zh-CN")
                .inputs(ImmutableList.of("2022/2/8周二"))
                .build();

        BatchValidationRequest requests_AU = BatchValidationRequest.builder()
                .locale("en-AU")
                .inputs(ImmutableList.of("3:51 pm Sunday, 11 March, 2012"))
                .build();

        BatchValidationRequest requests_RU = BatchValidationRequest.builder()
                .locale("ru-RU")
                .inputs(ImmutableList.of("30.11.2040", "воскресенье, 2 января, 2022 09:51:00"))
                .build();

        BatchValidationRequest requests_JA = BatchValidationRequest.builder()
                .locale("ja-JP")
                .inputs(ImmutableList.of("2022年2月9日水曜日", "2022/02/09", "2022年2月9日水曜日", "2022年2月9日水曜日 15:52:00"))
                .build();

        BatchValidationRequest requests_US = BatchValidationRequest.builder()
                .locale("en-US")
                .inputs(ImmutableList.of("Monday, February 21, 2022 7:33:00 pm"))
                .build();

        List<ValidationResult> validationList = PayloadBuilder.validateInputBatch(ImmutableList.of(requests_DE, requests_AU,
                requests_ES, requests_JA, requests_TW, requests_RU, requests_CN, requests_US));

        validationList.forEach(
                response -> {
                    Assert.isTrue(response.detectedPattern.isValidDate, "The date should be valid.");
                    Assert.isTrue(response.isLocalizedContent, "The input should be localized");
                    Assert.isTrue(!response.detectedPattern.pattern.isEmpty(), "The pattern should not be empty.");
                }
        );
    }

    @Test
    public void NonFullDatesBatch() {
        BatchValidationRequest requests_DE = BatchValidationRequest.builder()
                .locale("de-DE")
                .inputs(ImmutableList.of("Mi., Februar, 2022", "1. Quartal 2022", "Montag, Februar 2022"))
                .build();

        BatchValidationRequest requests_ES = BatchValidationRequest.builder()
                .locale("es-ES")
                .inputs(ImmutableList.of("2022, septiembre", "febrero, 2022", "lunes 19:33:00"))
                .build();

        BatchValidationRequest requests_TW = BatchValidationRequest.builder()
                .locale("zh-TW")
                .inputs(ImmutableList.of("2月6日 週日", "2022年1月 下午3:52:00"))
                .build();

        BatchValidationRequest requests_CN = BatchValidationRequest.builder()
                .locale("zh-CN")
                .inputs(ImmutableList.of("09:39:00 2022年第1季度", "2022年第1季度"))
                .build();

        BatchValidationRequest requests_US = BatchValidationRequest.builder()
                .locale("en-US")
                .inputs(ImmutableList.of("Monday 7:33:00 PM", "Monday, February 21", "March, 2022"))
                .build();

        List<ValidationResult> validationList = PayloadBuilder.validateInputBatch(ImmutableList.of(requests_DE, requests_CN,
                requests_ES, requests_US, requests_TW));

        validationList.forEach(
                response -> {
                    Assert.isTrue(!response.detectedPattern.isValidDate, "The date should not be valid.");
                    Assert.isTrue(!response.detectedPattern.isStandardFormat, "The input should not be standard.");
                    Assert.isTrue(response.isLocalizedContent, "The input should be localized");
                    Assert.isTrue(!response.detectedPattern.pattern.isEmpty(), "The pattern should not be empty.");
                }
        );
    }

    @Test
    public void standardDatesBatch() {
        BatchValidationRequest requests_DE = BatchValidationRequest.builder()
                .locale("de-DE")
                .inputs(ImmutableList.of("31.03.22", "31. März 2022", "Montag, 7. Februar 2022"))
                .build();

        BatchValidationRequest requests_ES = BatchValidationRequest.builder()
                .locale("es-ES")
                .inputs(ImmutableList.of("jueves, 31 de marzo de 2022", "1:33:25", "1:33", "jueves, 31 de marzo de 2022, 1:33:25 (GMT+02:00)"))
                .build();

        BatchValidationRequest requests_TW = BatchValidationRequest.builder()
                .locale("zh-TW")
                .inputs(ImmutableList.of("2022/3/31", "2022年3月31日 星期四", "上午1:33:00", "上午1:33", "2022/3/31 上午1:33"))
                .build();

        BatchValidationRequest requests_US = BatchValidationRequest.builder()
                .locale("en-US")
                .inputs(ImmutableList.of("Thursday, March 31, 2022", "Mar 31, 2022", "1:33:00 AM", "1:33 AM",
                        "Thursday, March 31, 2022 at 1:33:00 AM America/Argentina/San_Juan"))
                .build();

        List<ValidationResult> validationList = PayloadBuilder.validateInputBatch(ImmutableList.of(requests_DE,
                requests_ES, requests_US, requests_TW));

        validationList.forEach(
                response -> {
                    Assert.isTrue(response.detectedPattern.isStandardFormat, "The input should be standard.");
                    Assert.isTrue(response.isLocalizedContent, "The input should be localized");
                    Assert.isTrue(!response.detectedPattern.pattern.isEmpty(), "The pattern should not be empty.");
                }
        );
    }

    @Test
    public void asianDatesWithNoSpacing() {
        BatchValidationRequest requests_CN = BatchValidationRequest.builder()
                .locale("zh-CN")
                .inputs(ImmutableList.of("2022/2/8周二", "周二下午6:12:11", "周二18:13:02", "2022年2月8日周二", "2022年第1季度",
                        "2022年2月8日下午6:12:11周二", "2022年2月下午6:12:11", "8日下午6:12:11周二"))
                .build();

        List<ValidationResult> validationList = PayloadBuilder.validateInputBatch(ImmutableList.of(requests_CN));

        validationList.forEach(
                response -> {
                    Assert.isTrue(!response.detectedPattern.isStandardFormat, "The input should NOT be standard.");
                    Assert.isTrue(response.isLocalizedContent, "The input should be localized");
                    Assert.isTrue(!response.detectedPattern.pattern.isEmpty(), "The pattern should not be empty.");
                    Assert.isTrue(response.errors.isEmpty(), "No errors should be present.");
                }
        );
    }

    @Test
    public void verifySuggestions() {
        String incorrectCasingInput = "mittwoch, 16. März 2022 20:01:00 GMT+02:00";
        String incorrectTimezoneFormatInput = "Mittwoch, 16. März 2022 20:01:00 GMT+2";
        String combinedIncorrectFormatInput = "mittwoch, 16. März 2022 20:01:00 GMT+2";

        ValidationResult lowerCaseResponse = PayloadBuilder.validateInput("de-DE", incorrectCasingInput);
        Assert.isTrue(lowerCaseResponse.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(lowerCaseResponse.isLocalizedContent, "The input should be marked as localized.");
        Assert.isTrue(lowerCaseResponse.input.equals(incorrectCasingInput), "The input should not be modified");
        Assert.isTrue(!lowerCaseResponse.detectedPattern.pattern.isEmpty(), "The date should be marked as valid.");
        Assert.isTrue(lowerCaseResponse.suggestions.get(SuggestionsType.LOCALIZED_DATA).equals(INCORRECT_CASING_SUGGESTION),
                "There should be a correct suggestion");

        ValidationResult incorrectTimezoneResponse = PayloadBuilder.validateInput("de-DE", incorrectTimezoneFormatInput);
        Assert.isTrue(incorrectTimezoneResponse.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(incorrectTimezoneResponse.isLocalizedContent, "The input should be marked as localized.");
        Assert.isTrue(incorrectTimezoneResponse.input.equals(incorrectTimezoneFormatInput), "The input should not be modified");
        Assert.isTrue(!incorrectTimezoneResponse.detectedPattern.pattern.isEmpty(), "The date should be marked as valid.");

        ValidationResult combinedIncorrectResponse = PayloadBuilder.validateInput("de-DE", combinedIncorrectFormatInput);
        Assert.isTrue(combinedIncorrectResponse.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(combinedIncorrectResponse.isLocalizedContent, "The input should be marked as localized.");
        Assert.isTrue(combinedIncorrectResponse.input.equals(combinedIncorrectFormatInput), "The input should not be modified");
        Assert.isTrue(!combinedIncorrectResponse.detectedPattern.pattern.isEmpty(), "The date should be marked as valid.");
        Assert.isTrue(combinedIncorrectResponse.suggestions.get(SuggestionsType.LOCALIZED_DATA).equals(INCORRECT_CASING_SUGGESTION),
                "There should be a correct suggestion");
    }

    // Verify input containing padded day of month
    @Test
    public void paddedDayOfMonth() {
        ValidationResult responseSingleDigitDay = PayloadBuilder.validateInput("fr-FR", "07 septembre 2016 04:07:10 Los Angeles");
        Assert.isTrue(responseSingleDigitDay.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseSingleDigitDay.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseSingleDigitDay.detectedPattern.pattern.equals("dd MMMM yyyy HH:mm:ss VVV"),
                "The pattern should be correct.");

        ValidationResult responseDoubleDigitDay = PayloadBuilder.validateInput("de-DE", "Mittwoch, 0021 Februar, 2022 16:57:00");
        Assert.isTrue(responseDoubleDigitDay.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseDoubleDigitDay.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseDoubleDigitDay.detectedPattern.pattern.equals("EEEE, dddd MMMM, yyyy HH:mm:ss"),
                "The pattern should be correct.");
    }
}
