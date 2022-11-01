/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants;
import com.vmware.g11n.pattern.detection.library.exceptions.DateTimePatternDetectionException;
import com.vmware.g11n.pattern.detection.library.utils.ResourceLoaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceLoadersTest {

    @Test
    void verifyLoadCldrDataForUnsupportedLocale() {
        Exception exception = assertThrows(DateTimePatternDetectionException.class, () -> ResourceLoaders.loadCldrData(Locale.forLanguageTag("de-FR")));
        Assertions.assertEquals(PatternDetectionConstants.NOT_SUPPORTED_LOCALE_ERROR, exception.getMessage());
    }
}