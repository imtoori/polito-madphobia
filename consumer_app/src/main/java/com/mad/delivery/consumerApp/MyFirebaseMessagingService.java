package com.mad.delivery.consumerApp;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {}

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(getBaseContext(), remoteMessage.getNotification().getTitle(), Toast.LENGTH_SHORT).show());
    }
}
