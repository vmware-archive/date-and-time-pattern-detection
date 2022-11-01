/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;

public class RelativePeriodsFullIT {

    // Verify non-standard date containing relative day of week
    @Test
    public void relativeDayOfWeek() {
        ValidationResult responseUS = PayloadBuilder.validateInput("en-US", "26 June, 2022 this Saturday");
        Assert.isTrue(responseUS.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseUS.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseUS.detectedPattern.pattern.equals("d MMMM, yyyy 'this Saturday'"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(responseUS.errors.isEmpty(), "No errors should be present");
        Assert.isTrue(responseUS.suggestions.get(SuggestionsType.RELATIVE_TIME).contains(RELATIVE_TIME_DETECTED_SUGGESTION));

        ValidationResult responseFR = PayloadBuilder.validateInput("fr-FR", "29 juin, 2022 mer. prochain");
        Assert.isTrue(responseFR.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseFR.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseFR.detectedPattern.pattern.equals("d MMM, yyyy 'mer. prochain'"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(responseFR.errors.isEmpty(), "No errors should be present");
        Assert.isTrue(responseFR.suggestions.get(SuggestionsType.RELATIVE_TIME).contains(RELATIVE_TIME_DETECTED_SUGGESTION));
    }

    // Verify non-standard date + time containing relative period
    @Test
    public void relativeSimplePeriod() {
        ValidationResult responseUS = PayloadBuilder.validateInput("en-US", "26 June, 2022 at 9:34 PM yesterday");
        Assert.isTrue(responseUS.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseUS.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseUS.detectedPattern.pattern.equals("d MMMM, yyyy 'at' h:mm a 'yesterday'"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(responseUS.errors.isEmpty(), "No errors should be present");
        Assert.isTrue(responseUS.suggestions.get(SuggestionsType.RELATIVE_TIME).contains(RELATIVE_TIME_DETECTED_SUGGESTION));

        ValidationResult responseCN = PayloadBuilder.validateInput("zh-CN", "2022年6月26日 GMT+3 下午6:07:00 昨天");
        Assert.isTrue(responseCN.detectedPattern.isValidDate, "The date should be marked as valid.");
        Assert.isTrue(responseCN.detectedPattern.patternInfoMessage.equalsIgnoreCase(VALID_NON_STANDARD_DATE_TIME_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseCN.detectedPattern.pattern.equals("y年M月d日 O ah:mm:ss '昨天'"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(responseCN.errors.isEmpty(), "No errors should be present");
        Assert.isTrue(responseCN.suggestions.get(SuggestionsType.RELATIVE_TIME).contains(RELATIVE_TIME_DETECTED_SUGGESTION));
    }

    // Verify non-full date containing relative month
    @Test
    public void relativeMonth() {
        ValidationResult responseDE = PayloadBuilder.validateInput("de-DE", "26. letzten Monat");
        Assert.isTrue(!responseDE.detectedPattern.isValidDate, "The date should NOT be marked as valid.");
        Assert.isTrue(responseDE.detectedPattern.patternInfoMessage.equalsIgnoreCase(INVALID_TEMPORAL_OR_DATE_MESSAGE),
                "The pattern should have valid message");
        Assert.isTrue(responseDE.detectedPattern.pattern.equals("d. 'letzten Monat'"),
                "The pattern should be correct without any spacings");
        Assert.isTrue(responseDE.errors.isEmpty(), "No errors should be present");
        Assert.isTrue(responseDE.suggestions.get(SuggestionsType.RELATIVE_TIME).contains(RELATIVE_TIME_DETECTED_SUGGESTION));
    }

}
