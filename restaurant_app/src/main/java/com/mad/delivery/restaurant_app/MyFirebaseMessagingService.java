package com.mad.delivery.restaurant_app;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String id;

    public MyFirebaseMessagingService(String id) {
        this.id = id;
    }
    @Override
    public void onNewToken(String s) {
        RestaurantDatabase.getInstance().updateToken(id, s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(getBaseContext(), "New order!", Toast.LENGTH_SHORT).show());
    }
}
