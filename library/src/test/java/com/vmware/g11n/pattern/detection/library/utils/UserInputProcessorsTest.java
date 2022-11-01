/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.vmware.g11n.pattern.detection.library.exceptions.DateTimePatternDetectionException;
import com.vmware.g11n.pattern.detection.library.utils.UserInputProcessors;
import com.vmware.g11n.pattern.detection.model.CldrData;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Locale;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.INVALID_LOCALE_ERROR;
import static com.vmware.g11n.pattern.detection.library.utils.ResourceLoaders.loadCldrData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserInputProcessorsTest {

    @Test
    void verifySplitInputForNonCombinableComponents() {
        CldrData cldrData = loadCldrData(Locale.GERMANY);
        String userInput = "Mittwoch, 9 Februar, 2022 16:57:00";

        LinkedList<String> components = UserInputProcessors.splitInputString(userInput, cldrData);

        assertEquals(5, components.size(), "Input is not split into correct number of components.");
        assertEquals(userInput, String.join(" ", components), "Original user input was changed after split into a list.");
    }

    @Test
    void verifySplitInputForCombinableComponents() {
        CldrData cldrDataFr = loadCldrData(Locale.FRANCE);
        CldrData cldrDataEs = loadCldrData(Locale.forLanguageTag("es-ES"));
        CldrData cldrDataEn = loadCldrData(Locale.US);

        String userInputRelativePeriod = "29 juin, 2022 mer. prochain";
        String userInputTimezone = "1 septiembre 2016 14:07:10 hora de verano del Pacífico";
        String userInputDayPeriod = "Monday 7:33:00 PM";
        String userDayPeriodAndTimezone = "28 November, 2021 at 1:25 AM GMT+03:00"; // Both 1:25 and GMT+03:00 contain data matchable to time

        LinkedList<String> componentsRelativePeriod = UserInputProcessors.splitInputString(userInputRelativePeriod, cldrDataFr);
        LinkedList<String> componentsTimezone = UserInputProcessors.splitInputString(userInputTimezone, cldrDataEs);
        LinkedList<String> componentsDayPeriod = UserInputProcessors.splitInputString(userInputDayPeriod, cldrDataEn);
        LinkedList<String> componentsDayPeriodTimezone = UserInputProcessors.splitInputString(userDayPeriodAndTimezone, cldrDataEn);

        assertEquals(4, componentsRelativePeriod.size(), "Input is not split into correct number of components.");
        assertEquals(userInputRelativePeriod, String.join(" ", componentsRelativePeriod), "Original user input was changed after split into a list.");

        assertEquals(5, componentsTimezone.size(), "Input is not split into correct number of components.");
        assertEquals(userInputTimezone, String.join(" ", componentsTimezone), "Original user input was changed after split into a list.");

        assertEquals(2, componentsDayPeriod.size(), "Input is not split into correct number of components.");
        assertEquals(userInputDayPeriod, String.join(" ", componentsDayPeriod), "Original user input was changed after split into a list.");

        assertEquals(6, componentsDayPeriodTimezone.size(), "Input is not split into correct number of components.");
        assertEquals(userDayPeriodAndTimezone, String.join(" ", componentsDayPeriodTimezone), "Original user input was changed after split into a list.");
    }

    @Test
    void verifyAndLoadLocaleAssertException() {
        Exception exception = assertThrows(DateTimePatternDetectionException.class, () -> UserInputProcessors.verifyAndLoadLocale("de-CN"));
        assertEquals(exception.getMessage(), INVALID_LOCALE_ERROR, "Exception message is not correct. ");
    }

    @Test
    void verifySimplifiedChineseLocaleLoaded() {
        Locale loadedLocale = UserInputProcessors.verifyAndLoadLocale("zh-Hans-CN");
        assertEquals("zh-CN", loadedLocale.toLanguageTag());
    }

    @Test
    void verifyTrimInput() {
        String untrimmedInput = "   Testing  untrimmed    input  ";
        String expectedTrimmedInput = "Testing untrimmed input";
        assertEquals(expectedTrimmedInput, UserInputProcessors.trimInput(untrimmedInput));
    }
}