/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.calendar.dateTimeFormats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class AppendItems {
    @JsonProperty("Day")
    public String day;
    @JsonProperty("Day-Of-Week")
    public String dayOfWeek;
    @JsonProperty("Era")
    public String era;
    @JsonProperty("Hour")
    public String hour;
    @JsonProperty("Minute")
    public String minute;
    @JsonProperty("Month")
    public String month;
    @JsonProperty("Quarter")
    public String quarter;
    @JsonProperty("Second")
    public String second;
    @JsonProperty("Timezone")
    public String timezone;
    @JsonProperty("Week")
    public String week;
    @JsonProperty("Year")
    public String year;
}

