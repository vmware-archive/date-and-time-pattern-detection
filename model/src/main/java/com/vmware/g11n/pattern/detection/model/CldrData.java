/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model;

import com.vmware.g11n.pattern.detection.model.dateFields.DateFields;
import com.vmware.g11n.pattern.detection.model.calendar.GregorianCalendar;
import com.vmware.g11n.pattern.detection.model.timezones.TimezoneNames;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class CldrData {
    public GregorianCalendar gregorianCalendar;
    public DateFields dateFields;
    public TimezoneNames timezoneNames;
}
