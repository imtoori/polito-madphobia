package com.mad.delivery.restaurant_app;

import org.joda.time.LocalTime;

public class TimeRange {
    LocalTime from;
    LocalTime to;
    public TimeRange(LocalTime from ,LocalTime to) {
        this.from = from;
        this.to = to;
    }
}
