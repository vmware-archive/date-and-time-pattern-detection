/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.serviceData;

public enum SuggestionsType {
    LOCALIZED_DATA, NON_STANDARD_DATE, NON_STANDARD_TIME, NON_STANDARD_DATE_TIME, RELATIVE_TIME,
    ASIAN_TO_NON_ASIAN_INPUTS_CONVERSION, NON_CONVERTABLE_COMPONENTS, INCORRECT_FORMAT_SEPARATOR
}
