package com.mad.delivery.restaurant_app;

import java.util.List;

public interface FireBaseCallBack<T> {
    void onCallback(T user);
    void onCallbackList(List<T> list);


}
