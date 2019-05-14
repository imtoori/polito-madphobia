package com.mad.delivery.restaurant_app;

public interface OnFirebaseData<T> {
    void onReceived(T item);
}
