/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.services.impl;

import com.vmware.g11n.pattern.detection.api.services.api.ICldrService;
import com.vmware.g11n.pattern.detection.library.DateTimePatternDetection;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CldrService implements ICldrService {

    private final DateTimePatternDetection dateTimePatternDetection;

    @Autowired
    public CldrService() {
        dateTimePatternDetection = new DateTimePatternDetection();
    }

    @Override
    public List<String> getSupportedLocales() {
        return dateTimePatternDetection.getSupportedLocales();
    }

    @Override
    public TimeFormats getStandardCldrTimePatterns(String locale) {
        return dateTimePatternDetection.getStandardCldrTimePatterns(locale);
    }

    @Override
    public DateFormats getStandardCldrDatePatterns(String locale) {
        return dateTimePatternDetection.getStandardCldrDatePatterns(locale);

    }

    @Override
    public Map<String, String> getStandardCldrDateTimePatterns(String locale) {
        return dateTimePatternDetection.getStandardCldrDateTimePatterns(locale);
    }

    @Override
    public String getCldrVersion() {
        return dateTimePatternDetection.getCldrVersion();
    }

}