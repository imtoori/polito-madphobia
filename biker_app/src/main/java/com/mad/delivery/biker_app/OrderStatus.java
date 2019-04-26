package com.mad.delivery.biker_app;

import android.os.Parcelable;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    pending,
    canceled,
    preparing,
    ready,
    completed
}
