package com.mad.delivery.restaurant_app;

import android.os.Parcelable;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    PENDING,
    CANCELED,
    PREPARING,
    READY,
    COMPLETED
}
