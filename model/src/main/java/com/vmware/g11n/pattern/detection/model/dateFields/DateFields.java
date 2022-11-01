/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.model.dateFields;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class DateFields {
    @JsonProperty(value = "year")
    public RelativeData year;
    @JsonProperty(value = "year-short")
    public RelativeData yearShort;
    @JsonProperty(value = "year-narrow")
    public RelativeData yearNarrow;

    @JsonProperty(value = "month")
    public RelativeData month;
    @JsonProperty(value = "month-short")
    public RelativeData monthShort;
    @JsonProperty(value = "month-narrow")
    public RelativeData monthNarrow;

    @JsonProperty(value = "day")
    public RelativeData day;
    @JsonProperty(value = "day-short")
    public RelativeData dayShort;
    @JsonProperty(value = "day-narrow")
    public RelativeData dayNarrow;

    @JsonProperty(value = "quarter")
    public RelativeData quarter;
    @JsonProperty(value = "quarter-short")
    public RelativeData quarterShort;
    @JsonProperty(value = "quarter-narrow")
    public RelativeData quarterNarrow;
    
    @JsonProperty(value = "mon")
    public RelativeData monday;
    @JsonProperty(value = "mon-short")
    public RelativeData mondayShort;
    @JsonProperty(value = "mon-narrow")
    public RelativeData mondayNarrow;

    @JsonProperty(value = "tue")
    public RelativeData tuesday;
    @JsonProperty(value = "tue-short")
    public RelativeData tuesdayShort;
    @JsonProperty(value = "tue-narrow")
    public RelativeData tuesdayNarrow;

    @JsonProperty(value = "wed")
    public RelativeData wednesday;
    @JsonProperty(value = "wed-short")
    public RelativeData wednesdayShort;
    @JsonProperty(value = "wed-narrow")
    public RelativeData wednesdayNarrow;

    @JsonProperty(value = "thu")
    public RelativeData thursday;
    @JsonProperty(value = "thu-short")
    public RelativeData thursdayShort;
    @JsonProperty(value = "thu-narrow")
    public RelativeData thursdayNarrow;

    @JsonProperty(value = "fri")
    public RelativeData friday;
    @JsonProperty(value = "fri-short")
    public RelativeData fridayShort;
    @JsonProperty(value = "fri-narrow")
    public RelativeData fridayNarrow;

    @JsonProperty(value = "sat")
    public RelativeData saturday;
    @JsonProperty(value = "sat-short")
    public RelativeData saturdayShort;
    @JsonProperty(value = "sat-narrow")
    public RelativeData saturdayNarrow;

    @JsonProperty(value = "sun")
    public RelativeData sunday;
    @JsonProperty(value = "sun-short")
    public RelativeData sundayShort;
    @JsonProperty(value = "sun-narrow")
    public RelativeData sundayNarrow;

    @JsonProperty(value = "hour")
    public RelativeData hour;
    @JsonProperty(value = "minute")
    public RelativeData minute;
    @JsonProperty(value = "second")
    public RelativeData second;

    @JsonIgnore
    public List<String> getAllDateFieldsValues() {

        List<String> allRelativeTimeValues = new ArrayList<>();

        Arrays.stream(DateFields.class.getFields()).forEach(
                field -> {
                    if(Objects.nonNull(field) && field.getType().equals(RelativeData.class)){
                        try {
                            RelativeData data = (RelativeData) field.get(this);
                            if(Objects.nonNull(data)){
                                allRelativeTimeValues.addAll(data.getAllRelativeData());
                            }
                        } catch (IllegalAccessException ignored) {}
                    }
                }
        );

        return new ArrayList<>(allRelativeTimeValues);
    }}
