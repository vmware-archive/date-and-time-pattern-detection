/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.endpoints;

import com.vmware.g11n.pattern.detection.api.services.api.ICldrService;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.DateFormats;
import com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats.TimeFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cldr")
public class CldrResourcesController {

    private final ICldrService cldrService;

    @Autowired
    public CldrResourcesController(final ICldrService cldrService) {
        this.cldrService = cldrService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/supportedLocales")
    public List<String> getSupportedLocales() throws IOException {
        return cldrService.getSupportedLocales();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/standards/time/{locale}")
    public TimeFormats getStandardCldrTimePatterns(@PathVariable String locale) {
        return cldrService.getStandardCldrTimePatterns(locale);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/standards/date/{locale}")
    public DateFormats getStandardCldrDatePatterns(@PathVariable String locale) {
        return cldrService.getStandardCldrDatePatterns(locale);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/standards/datetime/{locale}")
    public Map<String, String> getStandardCldrDateTimePatterns(@PathVariable String locale) {
        return cldrService.getStandardCldrDateTimePatterns(locale);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/version")
    public String getCldrVersion() {
        return cldrService.getCldrVersion();
    }
}
