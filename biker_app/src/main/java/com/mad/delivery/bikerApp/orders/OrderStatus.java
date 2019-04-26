package com.mad.delivery.bikerApp.orders;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    pending,
    canceled,
    preparing,
    ready,
    completed
}
