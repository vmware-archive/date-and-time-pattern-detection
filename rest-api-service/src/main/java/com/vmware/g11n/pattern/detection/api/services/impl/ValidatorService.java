/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.services.impl;

import com.vmware.g11n.pattern.detection.api.services.api.IValidatorService;
import com.vmware.g11n.pattern.detection.library.DateTimePatternDetection;
import com.vmware.g11n.pattern.detection.model.serviceData.BatchValidationRequest;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidatorService implements IValidatorService {

    private final DateTimePatternDetection dateTimePatternDetection;

    public ValidatorService() {
        dateTimePatternDetection = new DateTimePatternDetection();
    }

    @Override
    public ValidationResult validateInput(String input, String locale) {
        return dateTimePatternDetection.validateLocalizedInput(input, locale);
    }

    @Override
    public List<ValidationResult> validateInput(String input) {
        return dateTimePatternDetection.validateLocalizedInput(input);
    }

    @Override
    public List<ValidationResult> bulkValidateLocalizedInput(List<BatchValidationRequest> batchValidationRequests) {
        return dateTimePatternDetection.batchValidateLocalizedInput(batchValidationRequests);
    }
}