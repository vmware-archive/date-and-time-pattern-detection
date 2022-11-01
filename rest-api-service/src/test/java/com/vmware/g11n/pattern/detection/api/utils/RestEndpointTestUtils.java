/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.utils;

import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.restassured.RestAssured.config;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class RestEndpointTestUtils {

    public static Response get(String url) {
        return setRequestSpecification()
                .get(url);
    }

    public static Response get(String url, Map<String, Object> queryParams) {
        return setRequestSpecification()
                .accept(ContentType.JSON)
                .contentType("application/text")
                .queryParams(queryParams)
                .get(url);
    }

    public static Response post(String url, Object body) {
        return setRequestSpecification()
                .body(body)
                .accept(ContentType.JSON)
                .contentType("application/text")
                .post(url);
    }

    public static Response post(String url, Object body, Map<String, Object> queryParams) {
        return setRequestSpecification()
                .body(body)
                .accept(ContentType.JSON)
                .contentType("application/text")
                .queryParams(queryParams)
                .post(url);
    }

    public static Response postWithJsonContentType(String url, Object body) {
        return setRequestSpecification()
                .body(body)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post(url);
    }

    private static RequestSpecification setRequestSpecification() {
        return RestAssured.given()
                .config(config().encoderConfig(encoderConfig().defaultContentCharset(StandardCharsets.UTF_8)
                        .encodeContentTypeAs("application/text", ContentType.TEXT)))
                .filters(
                        new ResponseLoggingFilter(),
                        new RequestLoggingFilter(),
                        new ErrorLoggingFilter());

    }
}
