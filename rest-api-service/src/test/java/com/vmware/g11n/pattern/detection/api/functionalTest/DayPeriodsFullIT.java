/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static com.vmware.g11n.pattern.detection.model.serviceData.ErrorsType.INCORRECT_DATE;

public class DayPeriodsFullIT {

    // Verify non-standard date+time containing complex day period
    @Test
    public void complexDayPeriodDateTime() {
        ValidationResult responseUS = PayloadBuilder.validateInput("en-US", "25 May, 2024 at 9:30 in the morning");
        Assert.isTrue(responseUS.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseUS.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseUS.detectedPattern.pattern.equals("d MMM, yyyy 'at' h:mm B"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(responseUS.errors.isEmpty(), "No errors should be present");

        ValidationResult responseFR = PayloadBuilder.validateInput("fr-FR", "25 mai 2022 5:13 de l’après-midi");
        Assert.isTrue(responseFR.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseFR.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseFR.detectedPattern.pattern.equals("d MMM yyyy h:mm B"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(responseFR.errors.isEmpty(), "No errors should be present");
    }

    // Verify non-standard date+time containing complex day period for asian locale
    @Test
    public void complexDayPeriodDateTimeAsian() {
        ValidationResult response = PayloadBuilder.validateInput("zh-CN", "2022年5月25日星期三 晚上3:13");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("y年M月d日EEEE Bh:mm"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    // Verify non-standard time containing non-usual period for asian locale
    @Test
    public void complexDayPeriodTimeAsian() {
        ValidationResult response = PayloadBuilder.validateInput("ko-KR", "PM 3:13");
        Assert.isTrue(!response.detectedPattern.isValidDate, "The date should be marked as invalid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("BBBBB h:mm"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    // Verify non-standard time containing non-usual period for non-asian locale
    @Test
    public void complexDayPeriodTimeNonAsian() {
        ValidationResult response = PayloadBuilder.validateInput("en-GB", "mi 3:13");
        Assert.isTrue(!response.detectedPattern.isValidDate, "The date should be marked as invalid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("BBBBB h:mm"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(response.errors.isEmpty(), "No errors should be present");
    }

    // Verify non-compatible time + day period
    @Test
    public void nonCompatibleTimeDayPeriod() {
        ValidationResult response = PayloadBuilder.validateInput("en-GB", "13:30 at noon");
        Assert.isTrue(!response.detectedPattern.isValidDate, "The date should be marked as invalid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(INVALID_TEMPORAL_OR_DATE_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("HH:mm 'at' B"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(response.errors.size() == 1, "1 error should be present");
        Assert.isTrue(!response.errors.get(INCORRECT_DATE).isEmpty(), "Correct error should be returned");

    }

    // Verify day period with dots + spacing, which is saved as p. m. in CLDR 
    @Test
    public void dayPeriodWithDotsAndSpacings() {
        ValidationResult response = PayloadBuilder.validateInput("es-ES", "4:57 p. m. miércoles, 09 febrero, 2022");
        Assert.isTrue(response.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(response.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(response.detectedPattern.pattern.equals("h:mm a EEEE, dd MMMM, yyyy"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(response.errors.size() == 0, "No errors should be present");
    }
}
