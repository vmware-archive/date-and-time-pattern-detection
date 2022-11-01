/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.VALID_NON_STANDARD_DATE_TIME_MESSAGE;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.VALID_NON_STANDARD_TIME_MESSAGE;

public class NonStandardSpacingsFullIT {

    // Verify input containing asian date + 12h time + time period + timezone + day of week with no spacing
    @Test
    public void asianDateTimeWithDayOfWeekAndTimezone() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2022年4月8日星期五GMT+08:00上午9:13:07");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y年M月d日EEEEZZZZah:mm:ss"),
                "The pattern should be correct without any spacings");
    }

    // Verify input containing asian date + 24h time + timezone + partial day of week with no spacing
    @Test
    public void asianDateTimeWithAbbreviatedDayOfWeekAndTimezone() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2022年4月8日19:13:07星期五GMT+08:00");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y年M月d日HH:mm:ssEEEEZZZZ"),
                "The pattern should be correct without any spacings");
    }

    // Verify input containing asian date + 24h time + localized timezone + day of week with no spacing
    @Test
    public void asianDateTimeWithLocalizedTimezone() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2022年2月8日周二上午9:13:07扎波罗热");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y年M月d日EEah:mm:ssVVV"),
                "The pattern should be correct without any spacings");
    }

    // Verify input containing asian dates with spacing between the time components
    @Test
    public void asianDateTimeWithSpacingBetweenTimeComponents() {
        ValidationResult response = PayloadBuilder.validateInput("ko-KR", "2016년 9월 1일 목요일 AM 11시 7분 10초 그리니치 표준시");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(!response.detectedPattern.isStandardFormat, "The format should NOT be standard.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y년 MMM d일 EEEE BBBBB h시 m분 s초 zzzz"),
                "The pattern should be correct.");
    }

    // Verify input containing asian dates with spacing between the time components and 24h format for time
    @Test
    public void asianDateTimeWithSpacingBetweenTimeComponents24hFormat() {
        ValidationResult response = PayloadBuilder.validateInput("ko-KR", "2016년 9월 1일 23시 07분 03초");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(!response.detectedPattern.isStandardFormat, "The format should NOT be standard.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y년 MMM d일 H시 mm분 ss초"),
                "The pattern should be correct.");
    }

    // Verify input containing asian dates with spacing between the time components and 24h format for time
    @Test
    public void asianTimeWithNoSpacingBetweenComponents12hFormat() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "上午1小时30分钟10秒");
        Assert.isTrue(!response.detectedPattern.isValidDate, "The date should NOT be marked as valid.");
        Assert.isTrue(!response.detectedPattern.isStandardFormat, "The format should NOT be standard.");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be empty.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("ah小时m分钟s秒"),
                "The pattern should be correct.");
    }
}
