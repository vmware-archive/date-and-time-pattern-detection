/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.data;


import com.google.common.base.Strings;
import com.vmware.g11n.pattern.detection.library.patterns.PatternBuilders;
import com.vmware.g11n.pattern.detection.model.CldrData;
import com.vmware.g11n.pattern.detection.model.dateFields.DateFields;
import com.vmware.g11n.pattern.detection.library.utils.Matchers;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vmware.g11n.pattern.detection.library.data.DateFieldSymbols.*;
import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static com.vmware.g11n.pattern.detection.library.patterns.PatternBuilders.*;
import static com.vmware.g11n.pattern.detection.library.utils.Matchers.*;

public class CldrMappers {

    public static Map<String, List<String>> mapAllDatesData(CldrData cldrData) {
        Map<String, List<String>> mapToReturn = new LinkedHashMap<>();

        // DAYS
        mapToReturn.put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY + DAYS_SUFFIX, cldrData.gregorianCalendar.days.daysFormat.abbreviated.values().stream().toList());
        mapToReturn.put(FORMATTED_NARROW_CLDR_DATA_KEY + DAYS_SUFFIX, cldrData.gregorianCalendar.days.daysFormat.narrow.values().stream().toList());
        mapToReturn.put(FORMATTED_WIDE_CLDR_DATA_KEY + DAYS_SUFFIX, cldrData.gregorianCalendar.days.daysFormat.wide.values().stream().toList());
        mapToReturn.put(STANDALONE_ABBREVIATED_CLDR_DATA_KEY + DAYS_SUFFIX, cldrData.gregorianCalendar.days.daysStandalone.abbreviated.values().stream().toList());
        mapToReturn.put(STANDALONE_NARROW_CLDR_DATA_KEY + DAYS_SUFFIX, cldrData.gregorianCalendar.days.daysStandalone.narrow.values().stream().toList());
        mapToReturn.put(STANDALONE_WIDE_CLDR_DATA_KEY + DAYS_SUFFIX, cldrData.gregorianCalendar.days.daysStandalone.wide.values().stream().toList());

        // MONTHS
        mapToReturn.put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY + MONTHS_SUFFIX, cldrData.gregorianCalendar.months.monthsFormat.abbreviated.values().stream().toList());
        mapToReturn.put(FORMATTED_NARROW_CLDR_DATA_KEY + MONTHS_SUFFIX, cldrData.gregorianCalendar.months.monthsFormat.narrow.values().stream().toList());
        mapToReturn.put(FORMATTED_WIDE_CLDR_DATA_KEY + MONTHS_SUFFIX, cldrData.gregorianCalendar.months.monthsFormat.wide.values().stream().toList());
        mapToReturn.put(STANDALONE_ABBREVIATED_CLDR_DATA_KEY + MONTHS_SUFFIX, cldrData.gregorianCalendar.months.monthsStandalone.abbreviated.values().stream().toList());
        mapToReturn.put(STANDALONE_NARROW_CLDR_DATA_KEY + MONTHS_SUFFIX, cldrData.gregorianCalendar.months.monthsStandalone.narrow.values().stream().toList());
        mapToReturn.put(STANDALONE_WIDE_CLDR_DATA_KEY + MONTHS_SUFFIX, cldrData.gregorianCalendar.months.monthsStandalone.wide.values().stream().toList());

        // DAY PERIODS
        mapToReturn.put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY_AM_PM_ONLY + DAY_PERIOD_SUFFIX, cldrData.gregorianCalendar.dayPeriods.dayPeriodsFormat.getDayPeriodsTypeForAmPmOnly(cldrData.gregorianCalendar.dayPeriods.dayPeriodsFormat.wide));
        mapToReturn.put(FORMATTED_ABBREVIATED_CLDR_DATA_KEY + DAY_PERIOD_SUFFIX, cldrData.gregorianCalendar.dayPeriods.dayPeriodsFormat.getDayPeriodsTypeWithoutAmPm(cldrData.gregorianCalendar.dayPeriods.dayPeriodsFormat.abbreviated));
        mapToReturn.put(FORMATTED_NARROW_CLDR_DATA_KEY + DAY_PERIOD_SUFFIX, cldrData.gregorianCalendar.dayPeriods.dayPeriodsFormat.getNarrow().values().stream().toList());
        mapToReturn.put(FORMATTED_WIDE_CLDR_DATA_KEY + DAY_PERIOD_SUFFIX, cldrData.gregorianCalendar.dayPeriods.dayPeriodsFormat.getWide().values().stream().toList());

        return mapToReturn;
    }

    public static Map<String, String> mapAllDateFieldsData(DateFields fields) {
        Map<String, String> mapToReturn = new LinkedHashMap<>();

        // Date fields
        mapToReturn.put(YEAR_FIELD_KEY, fields.year.displayName);
        mapToReturn.put(MONTH_FIELD_KEY, fields.month.displayName);
        mapToReturn.put(DAY_FIELD_KEY, fields.day.displayName);
        mapToReturn.put(HOUR_FIELD_KEY, fields.hour.displayName);
        mapToReturn.put(MINUTE_FIELD_KEY, fields.minute.displayName);
        mapToReturn.put(SECOND_FIELD_KEY, fields.second.displayName);

        return mapToReturn;
    }

    /**
     * Skipping check for 24h standalone hours as the pattern is part of the standard CLDR formats.
     */
    public static String getPatternFromDateField(String dateField, String input) {
        if (dateField.equals(YEAR_FIELD_KEY) && !buildYearPattern(input).isEmpty()) {
            return YEAR_1_DIGIT;
        }
        if (dateField.equals(MONTH_FIELD_KEY) && !buildMonthPattern(input).isEmpty()) {
            return MONTH_1_DIGIT;
        }
        if (dateField.equals(DAY_FIELD_KEY) && !buildDayOfMonthPattern(input).isEmpty()) {
            return DAY_OF_MONTH_1_DIGIT;
        }
        if (dateField.equals(HOUR_FIELD_KEY) && detectHoursShortStandaloneFormat(input)) {
            return HOUR_SHORT_SYMBOL;
        }
        if (dateField.equals(HOUR_FIELD_KEY) && detectHoursLongStandaloneFormat(input)) {
            return HOUR_LONG_SYMBOL;
        }
        if (dateField.equals(MINUTE_FIELD_KEY) && detectMinutesOrSecondsStandaloneShortFormat(input)) {
            return MINUTES_SHORT_SYMBOL;
        }
        if (dateField.equals(MINUTE_FIELD_KEY) && detectMinutesOrSecondsStandaloneLongFormat(input)) {
            return MINUTES_LONG_SYMBOL;
        }
        if (dateField.equals(SECOND_FIELD_KEY) && detectMinutesOrSecondsStandaloneShortFormat(input)) {
            return SECONDS_SHORT_SYMBOL;
        }
        if (dateField.equals(SECOND_FIELD_KEY) && detectMinutesOrSecondsStandaloneLongFormat(input)) {
            return SECONDS_LONG_SYMBOL;
        }
        return StringUtils.EMPTY;
    }

    public static List<String> getAllSubsidiaryElements(CldrData cldrData) {
        List<String> dateTimeFormats = new ArrayList<>(cldrData.gregorianCalendar.dateFormats.getAllDateFormatsAsMap().values());
        dateTimeFormats.addAll(cldrData.gregorianCalendar.timeFormats.getAllTimeFormatsAsMap().values());
        dateTimeFormats.addAll(cldrData.gregorianCalendar.dateTimeFormats.getAllDateTimeFormats());
        dateTimeFormats.addAll(cldrData.gregorianCalendar.dateTimeFormats.getAvailableFormats().values());

        return dateTimeFormats.stream()
                .map(Matchers::detectSingleQuotationWordsAndGetMatchedGroup).toList()
                .stream()
                .distinct()
                .filter(el -> !Strings.isNullOrEmpty(el))
                .collect(Collectors.toList());
    }
}
