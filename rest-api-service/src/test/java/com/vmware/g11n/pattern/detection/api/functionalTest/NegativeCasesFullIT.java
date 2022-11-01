/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;
import com.vmware.g11n.pattern.detection.model.serviceData.ErrorsType;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.INVALID_LOCALE_ERROR;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.NOT_SUPPORTED_LOCALE_ERROR;
import static org.springframework.util.Assert.isTrue;

public class NegativeCasesFullIT {

    @Test
    public void verifyIncorrectDateError() {
        ValidationResult response = PayloadBuilder.validateInput("de-DE", "Mittwoch, 11 Februar, 2022 16:57:00");

        Assert.isTrue(response.isLocalizedContent, "The input is localized.");
        Assert.isTrue(!response.detectedPattern.isValidDate, "The input should not be valid date");
        Assert.isTrue(response.suggestions.isEmpty(), "No suggestions should be present");
        Assert.isTrue(response.errors.size() == 1, "One error should be present");
        Assert.isTrue(response.errors.get(ErrorsType.INCORRECT_DATE).contains("Conflict"), "Error message should be present");
    }

    @Test
    public void verifyInvalidLocale() {
        String errorResponseMessage = PayloadBuilder.validateInputAndGetResponse("zh-Hants", "2022年1日2月")
                .then()
                .statusCode(400)
                .extract()
                .jsonPath()
                .getString("message");

        isTrue(errorResponseMessage.equalsIgnoreCase(INVALID_LOCALE_ERROR), "Error message should be correct");
    }

    @Test
    public void verifyNotSupportedLocale() {
        String errorResponseMessage = PayloadBuilder.validateInputAndGetResponse("de-LI", "7 Feb 2022")
                .then()
                .statusCode(400)
                .extract()
                .jsonPath()
                .getString("message");

        isTrue(errorResponseMessage.equalsIgnoreCase(NOT_SUPPORTED_LOCALE_ERROR), "Error message should be correct");
    }
}
