package com.mad.delivery.restaurant_app;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        Database.getInstance().updateToken(s);
    }
}
