package com.mad.delivery.restaurant_app.auth;

public interface OnLogin<T> {
    void onSuccess(T user);
    void onFailure();

}
