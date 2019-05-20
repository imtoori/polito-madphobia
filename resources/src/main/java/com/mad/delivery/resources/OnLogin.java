package com.mad.delivery.resources;

public interface OnLogin<T> {
    void onSuccess(T user);
    void onFailure();

}
