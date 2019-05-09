package com.mad.delivery.resources;


import java.io.Serializable;

public enum OrderStatus implements Serializable {
    pending,
    canceled,
    preparing,
    ready,
    completed;
}
