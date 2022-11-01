/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.utils;


import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import com.vmware.g11n.pattern.detection.model.serviceData.BatchValidationRequest;
import com.vmware.g11n.pattern.detection.model.serviceData.ConversionResult;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static com.vmware.g11n.pattern.detection.api.utils.RestEndpointTestUtils.post;
import static com.vmware.g11n.pattern.detection.api.utils.RestEndpointTestUtils.postWithJsonContentType;

public class PayloadBuilder {

    public static final String VALIDATOR_HOST_URL = "http://localhost:8083";

    public static DateFormats getStandardDateFormats(String locale) {
        return get(buildGetStandardCldrDateFormatsUrl(locale))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(DateFormats.class);
    }

    public static TimeFormats getStandardTimeFormats(String locale) {
        return get(buildGetStandardCldrTimeFormatsUrl(locale))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TimeFormats.class);
    }

    public static Map<String, String> getStandardDateTimeFormats(String locale) {
        return get(buildGetStandardCldrDateTimeFormatsUrl(locale))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getMap("$");
    }

    public static List<String> getSupportedLocales() {
        return get(buildGetSupportedLocalesUrl())
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("$");
    }

    public static ValidationResult validateInput(String locale, String input) {
        return validateInputAndGetResponse(locale, input)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ValidationResult.class);
    }

    public static ConversionResult convertInput(String sourceLocale, String targetLocale, String input) {
        return convertInputAndGetResponse(sourceLocale, targetLocale, input)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ConversionResult.class);
    }

    public static Response validateInputAndGetResponse(String locale, String input) {
        return post(buildValidateL10nInputUrl(locale), input);
    }

    public static Response convertInputAndGetResponse(String sourceLocale, String targetLocale, String input) {
        return post(buildConvertL10nInputUrl(), input, Map.of("sourceLocale", sourceLocale, "targetLocale", targetLocale));
    }

    public static List<ValidationResult> validateInputBatch(List<BatchValidationRequest> request) {
        return postWithJsonContentType(buildBatchValidateL10nInputUrl(), request)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("$", ValidationResult.class);
    }

    private static String buildValidateL10nInputUrl(String locale) {
        return VALIDATOR_HOST_URL + "/i18n/validate/localizedInput/" + locale;
    }

    private static String buildConvertL10nInputUrl() {
        return VALIDATOR_HOST_URL + "/i18n/convert/localizedInput/";
    }

    private static String buildBatchValidateL10nInputUrl() {
        return VALIDATOR_HOST_URL + "/i18n/validate/localizedInputs/";
    }

    private static String buildGetStandardCldrDateFormatsUrl(String locale) {
        return VALIDATOR_HOST_URL + "/i18n/cldr/standards/date/" + locale;
    }

    private static String buildGetStandardCldrTimeFormatsUrl(String locale) {
        return VALIDATOR_HOST_URL + "/i18n/cldr/standards/time/" + locale;
    }

    private static String buildGetStandardCldrDateTimeFormatsUrl(String locale) {
        return VALIDATOR_HOST_URL + "/i18n/cldr/standards/datetime/" + locale;
    }

    private static String buildGetSupportedLocalesUrl() {
        return VALIDATOR_HOST_URL + "/i18n/cldr/supportedLocales/";
    }
}
