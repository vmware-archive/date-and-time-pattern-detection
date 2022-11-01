/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.endpoints;

import com.vmware.g11n.pattern.detection.api.services.api.IValidatorService;
import com.vmware.g11n.pattern.detection.model.serviceData.BatchValidationRequest;
import com.vmware.g11n.pattern.detection.model.serviceData.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/validate/")
public class DateTimePatternValidationController {

    private final IValidatorService validatorService;

    @Autowired
    public DateTimePatternValidationController(final IValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/localizedInput/{locale}", consumes = "application/text", produces = "application/json")
    public ValidationResult validateLocalizedInput(
            @PathVariable String locale,
            @RequestBody String localizedInput) {
        return validatorService.validateInput(localizedInput.trim(), locale);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/localizedInputs/", consumes = "application/json", produces = "application/json")
    public List<ValidationResult> bulkValidateLocalizedInput(
            @RequestBody List<BatchValidationRequest> batchValidationRequest) {
        return validatorService.bulkValidateLocalizedInput(batchValidationRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/localizedInput/", consumes = "application/text", produces = "application/json")
    public List<ValidationResult> validateLocalizedInput(
            @RequestBody String localizedInput) {
        return validatorService.validateInput(localizedInput.trim());
    }
}
