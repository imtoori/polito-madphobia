package com.mad.delivery.consumerApp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {}

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("NOTIFICATION", "RECEIVED");
        Map<String, String> data = remoteMessage.getData();
        Log.d("NOTIFICATION", data.toString());
        Intent intent = new Intent(this, OrderInfoActivity.class);
        intent.putExtra("extra", data.get("extra"));
        createNotification("", data.get("title"), intent);
    }

    NotificationManager notifManager;

    public void createNotification(String aTitle, String aMessage, Intent goToIntent) {
        final int NOTIFY_ID = (int) System.currentTimeMillis();
        String name = "user_channel"; // They are hardcoded only for show it's just strings
        String id = "user_channel_1"; // The user-visible name of the channel.
        String description = "user_first_channel"; // The user-visible description of the channel.

        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            goToIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, goToIntent, 0);

            builder.setContentTitle(aTitle)  // required
                    .setContentText(aMessage)  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aTitle);
        } else {

            builder = new NotificationCompat.Builder(this, id);

            goToIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, goToIntent, 0);

            builder.setContentTitle(aTitle)    // required
                    .setContentText(aMessage)  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                    .setContentIntent(pendingIntent)
                    .setTicker(aTitle)
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }
}
