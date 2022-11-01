/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.services.api;

import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ICldrService {
    List<String> getSupportedLocales() throws IOException;

    TimeFormats getStandardCldrTimePatterns(String locale);

    DateFormats getStandardCldrDatePatterns(String locale);

    Map<String, String> getStandardCldrDateTimePatterns(String locale);

    String getCldrVersion();
}
