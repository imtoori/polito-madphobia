package com.mad.delivery.restaurant_app;

import com.mad.delivery.resources.Restaurant;

import java.util.List;

public interface FireBaseCallBack<T> {
    void onCallback(T user);
    void onCallbackList(List<T> list);
}
