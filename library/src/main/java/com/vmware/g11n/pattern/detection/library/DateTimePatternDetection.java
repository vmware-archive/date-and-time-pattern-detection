/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library;

import com.vmware.g11n.pattern.detection.library.services.CldrService;
import com.vmware.g11n.pattern.detection.library.services.ConvertorService;
import com.vmware.g11n.pattern.detection.library.services.ValidatorService;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import com.vmware.g11n.pattern.detection.model.serviceData.BatchValidationRequest;
import com.vmware.g11n.pattern.detection.model.serviceData.ConversionResult;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import com.vmware.g11n.pattern.detection.library.utils.ResourceLoaders;
import com.vmware.g11n.pattern.detection.library.utils.UserInputProcessors;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DateTimePatternDetection {

    static Map<String, Object> yamlConfigs;

    public static void setCldrVersion(String providedCldrVersion) {
        yamlConfigs = new Yaml().load(DateTimePatternDetection.class.getClassLoader()
                .getResourceAsStream("cldr-data.yml"));

        String currentCldrVersion = yamlConfigs.get("cldr-version").toString();

        if (!providedCldrVersion.equals(currentCldrVersion)) {
            ResourceLoaders resourceLoaders = new ResourceLoaders();
            resourceLoaders.generateCldrData(providedCldrVersion, Arrays.stream(String.valueOf(yamlConfigs.get("locales")).split(", ")).toList());
            yamlConfigs.replace("cldr-version", providedCldrVersion);
            resourceLoaders.saveYamlToFile(yamlConfigs);
        }
    }

    public ValidationResult validateLocalizedInput(String input, String locale) {
        ValidatorService validatorService = new ValidatorService();
        return validatorService.validateInput(input, locale);
    }

    public List<ValidationResult> validateLocalizedInput(String input) {
        ValidatorService validatorService = new ValidatorService();
        return validatorService.validateInput(input);
    }

    public List<ValidationResult> batchValidateLocalizedInput(List<BatchValidationRequest> batchValidationRequest) {
        ValidatorService validatorService = new ValidatorService();
        return validatorService.bulkValidateLocalizedInput(batchValidationRequest);
    }

    public List<String> getSupportedLocales() {
        CldrService cldrService = new CldrService();
        return cldrService.getSupportedLocales();
    }

    public ConversionResult convertLocalizedInput(String input, String sourceLocale, String targetLocale) {
        ConvertorService convertorService = new ConvertorService();
        return convertorService.convertLocalizeInput(input, sourceLocale, targetLocale);
    }

    public String getCldrVersion() {
        CldrService cldrService = new CldrService();
        return cldrService.getCldrVersion();
    }

    public TimeFormats getStandardCldrTimePatterns(String locale) {
        Locale providedLocale = UserInputProcessors.verifyAndLoadLocale(locale);
        return ResourceLoaders.loadCldrData(providedLocale).gregorianCalendar.timeFormats;
    }

    public DateFormats getStandardCldrDatePatterns(String locale) {
        Locale providedLocale = UserInputProcessors.verifyAndLoadLocale(locale);
        return ResourceLoaders.loadCldrData(providedLocale).gregorianCalendar.dateFormats;
    }

    public Map<String, String> getStandardCldrDateTimePatterns(String locale) {
        Locale providedLocale = UserInputProcessors.verifyAndLoadLocale(locale);
        return ResourceLoaders.loadCldrData(providedLocale).gregorianCalendar.getStandardDateTimeMap();
    }
}
