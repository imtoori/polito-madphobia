package com.mad.delivery.consumerApp;

import java.io.IOException;

public interface FirebaseCallback<T>{
    void onCallBack(T item) throws IOException;
}
