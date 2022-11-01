/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.services.impl;

import com.vmware.g11n.pattern.detection.library.DateTimePatternDetection;
import com.vmware.g11n.pattern.detection.model.serviceData.ConversionResult;
import org.springframework.stereotype.Service;
import com.vmware.g11n.pattern.detection.api.services.api.IConvertorService;

@Service
public class ConvertorService implements IConvertorService {

    private final DateTimePatternDetection dateTimePatternDetection;

    public ConvertorService() {
        dateTimePatternDetection = new DateTimePatternDetection();
    }

    @Override
    public ConversionResult convertLocalizeInput(String input, String sourceLocale, String targetLocale) {
        return dateTimePatternDetection.convertLocalizedInput(input, sourceLocale, targetLocale);
    }

}