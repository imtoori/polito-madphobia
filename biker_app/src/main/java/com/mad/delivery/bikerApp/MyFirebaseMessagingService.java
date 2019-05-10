package com.mad.delivery.bikerApp;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        Database.getInstance().updateToken(s);
    }
}
