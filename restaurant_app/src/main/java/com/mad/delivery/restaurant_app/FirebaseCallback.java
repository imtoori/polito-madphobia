package com.mad.delivery.restaurant_app;

import com.mad.delivery.resources.Order;

import java.util.List;

public interface FirebaseCallback{
    void  onCallbak(List<Order> list);

}
