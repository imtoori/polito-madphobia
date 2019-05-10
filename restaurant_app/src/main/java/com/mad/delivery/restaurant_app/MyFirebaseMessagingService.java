package com.mad.delivery.restaurant_app;

import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        Database.getInstance().updateToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Toast.makeText(getApplicationContext(), "New order!", Toast.LENGTH_SHORT).show();
    }
}
