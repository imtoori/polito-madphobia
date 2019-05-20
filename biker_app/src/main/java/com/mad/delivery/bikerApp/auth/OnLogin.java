package com.mad.delivery.bikerApp.auth;

public interface OnLogin<T> {
    void onSuccess(T user);
    void onFailure();

}
