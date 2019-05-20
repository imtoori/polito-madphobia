package com.mad.delivery.resources;

public interface OnDataFetched<T, E> {
    void onDataFetched(T data);

    void onError(E error);
}
