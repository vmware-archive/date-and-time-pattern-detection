/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.api.exceptions;

import com.vmware.g11n.pattern.detection.library.exceptions.DateTimePatternDetectionException;
import com.vmware.g11n.pattern.detection.model.serviceData.GenericErrorResponse;
import com.vmware.g11n.pattern.detection.library.services.ValidatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<GenericErrorResponse> handleCustomClientException(DateTimePatternDetectionException ex) {
        GenericErrorResponse response = new GenericErrorResponse(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()), ValidatorService.getErrors());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}