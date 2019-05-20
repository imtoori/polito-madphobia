package com.mad.delivery.resources;

public interface OnFirebaseData<T> {
    void onReceived(T item);
}
