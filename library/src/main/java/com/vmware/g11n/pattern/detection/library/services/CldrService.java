/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.services;

import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.vmware.g11n.pattern.detection.library.utils.ResourceLoaders.loadCldrData;
import static com.vmware.g11n.pattern.detection.library.utils.UserInputProcessors.verifyAndLoadLocale;

public class CldrService {

    Map<String, Object> yamlConfigs;

    public CldrService() {
        yamlConfigs = new Yaml().load(CldrService.class.getClassLoader()
                .getResourceAsStream("cldr-data.yml"));
    }

    public List<String> getSupportedLocales() {
        String[] supportedLocales = String.valueOf(yamlConfigs.get("locales")).split(", ");
        return Arrays.stream(supportedLocales).toList();
    }

    public String getCldrVersion() {
        return String.valueOf(yamlConfigs.get("cldr-version"));
    }

    public TimeFormats getStandardCldrTimePatterns(String locale) {
        Locale providedLocale = verifyAndLoadLocale(locale);
        return loadCldrData(providedLocale).gregorianCalendar.timeFormats;
    }

    public DateFormats getStandardCldrDatePatterns(String locale) {
        Locale providedLocale = verifyAndLoadLocale(locale);
        return loadCldrData(providedLocale).gregorianCalendar.dateFormats;
    }

    public Map<String, String> getStandardCldrDateTimePatterns(String locale) {
        Locale providedLocale = verifyAndLoadLocale(locale);
        return loadCldrData(providedLocale).gregorianCalendar.getStandardDateTimeMap();
    }

}