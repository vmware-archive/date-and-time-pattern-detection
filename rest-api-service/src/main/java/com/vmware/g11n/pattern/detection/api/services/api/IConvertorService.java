/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.services.api;

import com.vmware.g11n.pattern.detection.model.serviceData.ConversionResult;

public interface IConvertorService {
    ConversionResult convertLocalizeInput(String input, String sourceLocale, String targetLocale);
}
