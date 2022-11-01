/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder;
import com.vmware.g11n.pattern.detection.model.serviceData.ConversionResult;
import org.junit.jupiter.api.Test;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static com.vmware.g11n.pattern.detection.model.serviceData.ErrorsType.UNDETECTED_COMPONENTS;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION;
import static org.springframework.util.Assert.isTrue;

public class ConversionFullIT {

    @Test
    public void verifyNonStandardDateTimeNonAsianLocales() {
        ConversionResult response = PayloadBuilder.convertInput("en-AU", "de-DE", "3:51 pm Sunday, 11 March, 2012");

        isTrue(response.isTargetContentLocalized, "Target Output should be localized");
        isTrue(response.isSourceContentLocalized, "Source Input should be localized");
        isTrue(response.targetLocalizedOutput.equals("3:51 PM Sonntag, 11 März, 2012"), "Target Output should be correct");
        isTrue(response.sourceLocalizedInput.equals("3:51 pm Sunday, 11 March, 2012"), "Source input should be correct");
        isTrue(response.sourcePattern.isValidDate, "Source pattern should be valid date");
        isTrue(response.targetPattern.isValidDate, "Target pattern should be valid date");
        isTrue(!response.sourcePattern.isStandardFormat, "Source pattern should be non-standard format");
        isTrue(!response.targetPattern.isStandardFormat, "Target pattern should be non-standard format");
        isTrue(response.conversionErrors.isEmpty(), "No errors should be present");
    }

    @Test
    public void verifyNonStandardDateTimeAsianLocaleIncorrectConversion() {
        ConversionResult response = PayloadBuilder.convertInput("ja-JP", "fr-FR", "2022年2月9日 水曜日");

        isTrue(!response.isTargetContentLocalized, "Target Output should NOT be localized");
        isTrue(response.isSourceContentLocalized, "Source Input should be localized");
        isTrue(response.targetLocalizedOutput.equals("2022年2月9日 mercredi"), "Target Output should be correct");
        isTrue(response.sourceLocalizedInput.equals("2022年2月9日 水曜日"), "Source input should be correct");
        isTrue(response.sourcePattern.isValidDate, "Source pattern should be valid date");
        isTrue(!response.targetPattern.isValidDate, "Target pattern should NOT be valid date");
        isTrue(!response.sourcePattern.isStandardFormat, "Source pattern should be non-standard format");
        isTrue(!response.targetPattern.isStandardFormat, "Target pattern should be non-standard format");
        isTrue(response.conversionErrors.get(UNDETECTED_COMPONENTS).contains(UNDETECTED_COMPONENTS_ERROR), "Errors should be correct");
        isTrue(!response.conversionSuggestions.isEmpty(), "Suggestions should Not be empty");

    }

    @Test
    public void verifyNonStandardDateTimeAsianLocaleValidConversion() {
        ConversionResult response = PayloadBuilder.convertInput("zh-CN", "en-US", "21 十一月, 2025, 2:31 下午");

        isTrue(response.isTargetContentLocalized, "Target Output should be localized");
        isTrue(response.isSourceContentLocalized, "Source Input should be localized");
        isTrue(response.targetLocalizedOutput.equals("21 November, 2025, 2:31 PM"), "Target Output should be correct");
        isTrue(response.sourceLocalizedInput.equals("21 十一月, 2025, 2:31 下午"), "Source input should be correct");
        isTrue(response.sourcePattern.isValidDate, "Source pattern should be valid date");
        isTrue(response.targetPattern.isValidDate, "Target pattern should be valid date");
        isTrue(!response.sourcePattern.isStandardFormat, "Source pattern should be non-standard format");
        isTrue(!response.targetPattern.isStandardFormat, "Target pattern should be non-standard format");
        isTrue(response.conversionErrors.isEmpty(), "Errors should NOT be correct");
        isTrue(response.conversionSuggestions.get(ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION).equals(ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION_SUGGESTION), "Suggestions should be correct");

    }

    @Test
    public void verifyNonStandardDateTimeWithTimezone() {
        ConversionResult response = PayloadBuilder.convertInput("zh-CN", "it-IT", "21 十二月, 2022 上午4:07 北美太平洋时间");

        isTrue(response.isTargetContentLocalized, "Target Output should be localized");
        isTrue(response.isSourceContentLocalized, "Source Input should be localized");
        isTrue(response.targetLocalizedOutput.equals("21 dicembre, 2022 AM4:07 Ora del Pacifico USA"), "Target Output should be correct");
        isTrue(response.sourceLocalizedInput.equals("21 十二月, 2022 上午4:07 北美太平洋时间"), "Source input should be correct");
        isTrue(response.sourcePattern.isValidDate, "Source pattern should be valid date");
        isTrue(response.targetPattern.isValidDate, "Target pattern should be valid date");
        isTrue(!response.sourcePattern.isStandardFormat, "Source pattern should be non-standard format");
        isTrue(!response.targetPattern.isStandardFormat, "Target pattern should be non-standard format");
        isTrue(response.conversionErrors.isEmpty(), "Errors should be empty");
        isTrue(response.conversionSuggestions.get(ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION).equals(ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION_SUGGESTION), "Suggestions should be correct");

    }

    @Test
    public void verifyInabilityToConvertNonFullDates() {
        String responseErrorMessage = PayloadBuilder.convertInputAndGetResponse("de-DE", "pt-PT", "Feb 21. ")
                .then()
                .statusCode(400)
                .extract()
                .jsonPath()
                .getString("message");

        isTrue(responseErrorMessage.equalsIgnoreCase(NON_CONVERTABLE_INPUT), "Error message should be correct");
    }

    @Test
    public void verifyInabilityToConvertNonValidLocales() {
        String responseErrorMessage = PayloadBuilder.convertInputAndGetResponse("pt-PTsda", "de-DE", "Feb 21. ")
                .then()
                .statusCode(400)
                .extract()
                .jsonPath()
                .getString("message");

        isTrue(responseErrorMessage.equalsIgnoreCase(INVALID_LOCALE_ERROR), "Error message should be correct");

    }
}
