package com.mad.delivery.bikerApp.orders;

import org.joda.time.LocalTime;

public class TimeRange {
    LocalTime from;
    LocalTime to;
    public TimeRange(LocalTime from ,LocalTime to) {
        this.from = from;
        this.to = to;
    }
}
