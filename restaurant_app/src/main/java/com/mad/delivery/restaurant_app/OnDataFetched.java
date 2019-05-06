package com.mad.delivery.restaurant_app;

public interface OnDataFetched<T, E> {
    void onDataFetched(T data);

    void onError(E error);
}
