/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.exceptions;

public class DateTimePatternDetectionException extends RuntimeException {

    private static final long serialVersionUID = 121L;

    public DateTimePatternDetectionException(String message) {
        super(message);
    }
}
