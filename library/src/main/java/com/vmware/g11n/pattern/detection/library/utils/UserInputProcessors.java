/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.vmware.g11n.pattern.detection.library.exceptions.DateTimePatternDetectionException;
import com.vmware.g11n.pattern.detection.model.CldrData;
import org.apache.commons.lang3.LocaleUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.INVALID_LOCALE_ERROR;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.serviceSpecialLocaleFormats;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.join;


public class UserInputProcessors {

    public static LinkedList<String> splitInputString(String input, CldrData cldrData) {
        LinkedList<String> orderedList = new LinkedList<>(Arrays.asList(input.split(" ")));

        if (orderedList.size() == 1) {
            return orderedList;
        }

        // Check if the input contains timezone components which need to be counted as 1 component (example: Los Angeles)
        combineSpacedTimezoneElementsFromList(orderedList, cldrData);

        // Check if the input contains relative-time components which need to be counted as 1 component (example: next week)
        combineSpacedRelativeTimeElementsFromList(orderedList, cldrData);

        // Check if input contains day period components which need to be counted as 1 component (example: this morning)
        // TODO: add support for case insensitive period
        combineTimeAndDayPeriodElementsFromList(orderedList, cldrData);

        return orderedList;
    }

    private static void combineSpacedRelativeTimeElementsFromList(LinkedList<String> orderedList, CldrData cldrData) {
        String relativePeriod = CldrProcessors.detectAnyRelativeTime(join(orderedList, " "), cldrData);

        if (relativePeriod.isEmpty()) {
            return;
        }

        // Combine the relative period sub-components into 1 (example: 'last friday')
        combineElementsFromListBySeparator(relativePeriod, orderedList, " ");
    }

    public static Locale verifyAndLoadLocale(String locale) {
        Locale providedLocale = Locale.forLanguageTag(serviceSpecialLocaleFormats.getOrDefault(locale, locale));

        if (!LocaleUtils.isAvailableLocale(providedLocale)) {
            throw new DateTimePatternDetectionException(INVALID_LOCALE_ERROR);
        }

        return providedLocale;
    }

    public static String trimInput(String input) {
        String trimmedInput = input.trim().replaceAll(" +", " ");
        trimmedInput = trimmedInput.replaceAll(" ,", ",");

        return trimmedInput;
    }

    private static void combineSpacedTimezoneElementsFromList(LinkedList<String> orderedList, CldrData cldrData) {
        String zoneWithSpacings = CldrProcessors.detectTimezonesWithSpacings(join(orderedList, " "), cldrData);
        if (!zoneWithSpacings.isEmpty()) {
            List<String> zoneElements = Arrays.stream(zoneWithSpacings.split(" ")).toList();
            orderedList.set(orderedList.indexOf(zoneElements.get(0)), zoneWithSpacings);
            orderedList.removeAll(zoneElements);
        }

    }

    private static void combineTimeAndDayPeriodElementsFromList(LinkedList<String> orderedList, CldrData cldrData) {
        String dayPeriod = CldrProcessors.detectAnyDayPeriod(join(orderedList, " "), cldrData);
        if (dayPeriod.isEmpty()) {
            return;
        }

        // Combine the time period sub-components into 1 (example: 'in the morning' - split by spacing and p. m. - split by NBSP
        combineElementsFromListBySeparator(dayPeriod, orderedList, " ");
        combineElementsFromListBySeparator(dayPeriod, orderedList, "\u00A0");

        // Match the time period from the newly ordered list as it may contain special characters
        String matchedDayPeriodFromList = orderedList.stream().filter(i ->
                i.matches("^" + dayPeriod + "[,.]?$")).findFirst().orElse(EMPTY);
        int indexOfDayPeriod = orderedList.indexOf(matchedDayPeriodFromList);

        // If period of day is present, check if previous or next element is valid time and combine into 1 element
        if (indexOfDayPeriod == -1) {
            return;
        }

        boolean doesPreviousElementMatchesTime = indexOfDayPeriod > 0
                && !Matchers.getMatchedTimeOrPattern(orderedList.get(indexOfDayPeriod - 1), Matchers.formattedTimePatternsToRegexMap, false).isBlank() ;
        boolean doesNextElementMatchesTime = indexOfDayPeriod < orderedList.size() - 1 && !Matchers.getMatchedTimeOrPattern(orderedList.get(indexOfDayPeriod + 1), Matchers.formattedTimePatternsToRegexMap, false).isBlank();

        if (doesPreviousElementMatchesTime) {
            orderedList.set(indexOfDayPeriod - 1, orderedList.get(indexOfDayPeriod - 1) + " " + matchedDayPeriodFromList);
            orderedList.remove(indexOfDayPeriod);
        }
        else if (doesNextElementMatchesTime) {
            orderedList.set(indexOfDayPeriod + 1, matchedDayPeriodFromList + " " + orderedList.get(indexOfDayPeriod + 1));
            orderedList.remove(indexOfDayPeriod);
        }

    }

    private static void combineElementsFromListBySeparator(String elementToCombine, List<String> orderedList, String separator) {
        if (elementToCombine.contains(separator)) {
            List<String> elementsFromSeparator = Arrays.stream(elementToCombine.split(separator)).toList();
            orderedList.set(orderedList.indexOf(elementsFromSeparator.get(0)), elementToCombine);
            orderedList.removeAll(elementsFromSeparator);
        }
    }
}
