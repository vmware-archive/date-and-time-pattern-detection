/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
@EnableWebMvc
public class DateTimePatternDetectionServiceApplication extends WebMvcConfigurationSupport {
    public static void main(String[] args) {
        SpringApplication.run(DateTimePatternDetectionServiceApplication.class, args);
    }
}