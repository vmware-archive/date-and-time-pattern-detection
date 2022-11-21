/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.patterns;

import com.ibm.icu.text.SimpleDateFormat;
import com.vmware.g11n.pattern.detection.library.services.ValidatorService;
import com.vmware.g11n.pattern.detection.model.CldrData;
import com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType;
import com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidatedPattern;
import org.threeten.extra.YearQuarter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.*;
import static com.vmware.g11n.pattern.detection.model.serviceData.ErrorsType.INCORRECT_DATE;
import static com.vmware.g11n.pattern.detection.model.serviceData.LocalizedType.*;
import static com.vmware.g11n.pattern.detection.model.serviceData.SuggestionsType.*;
import static java.util.Objects.requireNonNull;

public class PatternValidators {

    public static boolean isValidPattern(String pattern, String value, Locale locale, LocalizedType type) {

        switch (type) {
            case TEMPORAL -> {
                boolean caseSensitiveMatch = parseInputToFormat(YearQuarter.class, locale, value, pattern, true);
                boolean caseInsensitiveMatch = parseInputToFormat(YearQuarter.class, locale, value, pattern, false);

                addSuggestionIfIncorrectCasing(caseSensitiveMatch, caseInsensitiveMatch);

                return caseSensitiveMatch || caseInsensitiveMatch;
            }
            case DATE -> {
                boolean caseSensitiveMatch = parseInputToFormat(LocalDate.class, locale, value, pattern, true);
                boolean caseInsensitiveMatch = parseInputToFormat(LocalDate.class, locale, value, pattern, false);

                addSuggestionIfIncorrectCasing(caseSensitiveMatch, caseInsensitiveMatch);
                return caseSensitiveMatch || caseInsensitiveMatch;
            }
            case TIME -> {
                boolean caseSensitiveMatch = parseInputToFormat(LocalTime.class, locale, value, pattern, true);
                boolean caseInsensitiveMatch = parseInputToFormat(LocalTime.class, locale, value, pattern, false);

                addSuggestionIfIncorrectCasing(caseSensitiveMatch, caseInsensitiveMatch);
                return caseSensitiveMatch || caseInsensitiveMatch;
            }
            case DATE_TIME -> {
                boolean caseSensitiveMatch = parseInputToFormat(ZonedDateTime.class, locale, value, pattern, true)
                        || parseInputToFormat(LocalDateTime.class, locale, value, pattern, true);
                boolean caseInsensitiveMatch = parseInputToFormat(ZonedDateTime.class, locale, value, pattern, false)
                        || parseInputToFormat(LocalDateTime.class, locale, value, pattern, false);

                boolean simpleDateFormatMatch = parseInputToFormat(SimpleDateFormat.class, locale, value, pattern, false);

                // Don't check simpleDateFormatMatch for casing as it doesn't provide ability for case matching.
                addSuggestionIfIncorrectCasing(caseSensitiveMatch, caseInsensitiveMatch);

                return caseSensitiveMatch || caseInsensitiveMatch || simpleDateFormatMatch;
            }
        }

        return false;
    }

    public static ValidatedPattern verifyAndBuildPattern(String pattern, String input, Locale locale, CldrData cldrData) {
        if (isValidPattern(pattern, input, locale, DATE_TIME)) {
            ValidatorService.suggestions.put(NON_STANDARD_DATE_TIME, NON_STANDARD_DATE_TIME_SUGGESTION + cldrData.gregorianCalendar.getStandardDateTimeMapToString());
            return ValidatedPattern.builder().pattern(pattern).patternInfoMessage(VALID_NON_STANDARD_DATE_TIME_MESSAGE).localizedType(DATE_TIME).isValidDate(true).build();
        } else if (isValidPattern(pattern, input, locale, DATE)) {
            ValidatorService.suggestions.put(NON_STANDARD_DATE, NON_STANDARD_DATE_SUGGESTION + cldrData.gregorianCalendar.dateFormats.toString());
            return ValidatedPattern.builder().pattern(pattern).patternInfoMessage(VALID_NON_STANDARD_DATE_MESSAGE).localizedType(DATE).isValidDate(true).build();
        } else if (isValidPattern(pattern, input, locale, TIME)) {
            ValidatorService.suggestions.put(NON_STANDARD_TIME, NON_STANDARD_TIME_SUGGESTION + " " + cldrData.gregorianCalendar.timeFormats.toString());
            return ValidatedPattern.builder().pattern(pattern).patternInfoMessage(VALID_NON_STANDARD_TIME_MESSAGE).localizedType(TIME).isValidDate(false).build();
        } else if (isValidPattern(pattern, input, locale, TEMPORAL)) {
            return ValidatedPattern.builder().pattern(pattern).patternInfoMessage(VALID_TEMPORAL_MESSAGE).isValidDate(false).build();
        } else {
            return ValidatedPattern.builder().pattern(pattern).localizedType(INVALID_TEMPORAL).patternInfoMessage(INVALID_TEMPORAL_OR_DATE_MESSAGE).isValidDate(false).build();
        }
    }

    private static <T> boolean parseInputToFormat(Class<T> possibleFormatType, Locale locale, String value, String pattern, boolean caseSensitive) {
        DateTimeFormatter formatterCaseSensitive;
        DateTimeFormatter formatterCaseInsensitive;
        DateTimeFormatter formatter = null;

        try {
            formatterCaseSensitive = DateTimeFormatter.ofPattern(pattern, locale);
            formatterCaseInsensitive = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern(pattern).toFormatter(locale);
            formatter = caseSensitive ? formatterCaseSensitive : formatterCaseInsensitive;
        } catch (Exception e) {
            SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
            try {
                return !format.parse(value).toString().isEmpty();

            } catch (ParseException ignored) {
            }
        }

        try {
            if (possibleFormatType == LocalDateTime.class) {
                return !LocalDateTime.parse(value, requireNonNull(formatter)).toString().isEmpty();
            }
            if (possibleFormatType == LocalDate.class) {
                return LocalDate.parse(value, requireNonNull(formatter)).format(formatter).equalsIgnoreCase(value);
            }
            if (possibleFormatType == LocalTime.class) {
                return !LocalTime.parse(value, requireNonNull(formatter)).toString().isEmpty(); // Cannot compare as the DateTimeFormatter is not full
            }
            if (possibleFormatType == YearQuarter.class) {
                return YearQuarter.parse(value, requireNonNull(formatter)).format(formatter).equalsIgnoreCase(value);
            }
            if (possibleFormatType == ZonedDateTime.class) {
                return !ZonedDateTime.parse(value, requireNonNull(formatter)).getZone().getId().isEmpty();
            }
        } catch (DateTimeParseException e) {
            if (e.getMessage().contains("Conflict found")) {
                ValidatorService.errors.put(INCORRECT_DATE, "Incorrect date/time. " + e.getCause().getMessage());
                //throw new CustomErrorException("Incorrect date. " + e.getCause().getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private static void addSuggestionIfIncorrectCasing(boolean caseSensitiveMatch, boolean caseInsensitiveMatch) {
        if (caseSensitiveMatch ^ caseInsensitiveMatch) {
            ValidatorService.suggestions.put(SuggestionsType.LOCALIZED_DATA, INCORRECT_CASING_SUGGESTION);
        }
    }
}
