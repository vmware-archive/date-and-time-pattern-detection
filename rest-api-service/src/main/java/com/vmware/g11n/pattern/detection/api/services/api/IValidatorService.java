/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.services.api;

import com.vmware.g11n.pattern.detection.model.serviceData.BatchValidationRequest;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;

import java.util.List;

public interface IValidatorService {
    ValidationResult validateInput(String input, String locale);

    List<ValidationResult> validateInput(String input);

    List<ValidationResult> bulkValidateLocalizedInput(List<BatchValidationRequest> batchValidationRequests);

}
