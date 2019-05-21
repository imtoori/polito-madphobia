package com.mad.delivery.consumerApp;

import java.io.IOException;

public interface firebaseCallback<T>{
    void onCallBack(T item) throws IOException;
}
