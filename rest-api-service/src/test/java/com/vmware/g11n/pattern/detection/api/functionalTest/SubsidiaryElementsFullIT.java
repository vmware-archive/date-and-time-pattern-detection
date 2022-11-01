/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.functionalTest;

import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.springframework.util.Assert.isTrue;
import static com.vmware.g11n.pattern.detection.api.utils.PayloadBuilder.validateInput;

/**
 * Verify inputs containing subsidiary elements.
 * Subsidiary element is localized element, which doesn't correspond to CLDR pattern and needs to be escaped when being matched
 * Examples: 'г.', 'at', 'de'
 */
public class SubsidiaryElementsFullIT {

    // Verify abbreviated months which contain valid period (".") in the localized String
    @Test
    public void verifyYearInCyrillicLanguages() {
        ValidationResult responseRussian = validateInput("ru-RU", "14 апр. 2022 г., 19:46:42 GMT+3");
        isTrue(responseRussian.detectedPattern.isValidDate, "The date should be marked as valid.");
        isTrue(responseRussian.detectedPattern.pattern.equals("d MMM yyyy 'г'., HH:mm:ss O"), "The pattern should be correct.");

        ValidationResult responseBulgarian = validateInput("bg-BG", "4 дек 2022 г.");
        isTrue(responseBulgarian.detectedPattern.isValidDate, "The date should be marked as valid.");
        isTrue(responseBulgarian.detectedPattern.pattern.equals("d MMM yyyy 'г'."), "The pattern should be correct.");
    }

    // Verify time periods ("at", "de")
    @Test
    public void verifySubsidiaryTimePeriods() {
        ValidationResult responsePortuguese = validateInput("pt-PT", "sexta-feira, 9 de abril de, 2010");
        isTrue(responsePortuguese.detectedPattern.isValidDate, "The date should be marked as valid.");
        isTrue(responsePortuguese.detectedPattern.pattern.equals("EEEE, d 'de' MMMM 'de', yyyy"), "The pattern should be correct.");

        ValidationResult responseSpanish = validateInput("es-ES", "13 de abril de 2022, 21:00:00");
        isTrue(responseSpanish.detectedPattern.isValidDate, "The date should be marked as valid.");
        isTrue(responseSpanish.detectedPattern.pattern.equals("d 'de' MMMM 'de' yyyy, H:mm:ss"), "The pattern should be correct.");

        ValidationResult responseEnglish = validateInput("en-US", "Thursday, March 31, 2022 at 1:33:00 AM");
        isTrue(responseEnglish.detectedPattern.isValidDate, "The date should be marked as valid.");
        isTrue(responseEnglish.detectedPattern.pattern.equals("EEEE, MMMM d, yyyy 'at' h:mm:ss a"), "The pattern should be correct.");
    }
}
