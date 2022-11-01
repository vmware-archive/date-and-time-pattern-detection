/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.endpoints;

import com.vmware.g11n.pattern.detection.model.serviceData.ConversionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.vmware.g11n.pattern.detection.api.services.api.IConvertorService;

@RestController
@CrossOrigin
@RequestMapping("/convert/")
public class DateTimePatternConversionController {

    private final IConvertorService convertorService;

    @Autowired
    public DateTimePatternConversionController(final IConvertorService convertorService) {
        this.convertorService = convertorService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/localizedInput/", consumes = "application/text", produces = "application/json")
    public ConversionResult validateLocalizedInput(
            @RequestParam String sourceLocale,
            @RequestParam String targetLocale,
            @RequestBody String localizedInput) {
        return convertorService.convertLocalizeInput(localizedInput.trim(), sourceLocale.trim(), targetLocale.trim());
    }

}
