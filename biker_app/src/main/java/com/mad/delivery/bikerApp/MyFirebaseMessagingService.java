package com.mad.delivery.bikerApp;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private FirebaseAuth mAuth;

    @Override
    public void onNewToken(String s) {
        mAuth = FirebaseAuth.getInstance();
        BikerDatabase.getInstance().updateToken(mAuth.getCurrentUser().getUid(), s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(getBaseContext(), "New order!", Toast.LENGTH_SHORT).show());
    }
}
