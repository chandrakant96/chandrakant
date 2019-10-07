package com.whr.user.com.WHR.Utils.FCM;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.whr.user.activities.SplashScreenActivity;

import java.sql.Timestamp;
import java.util.Date;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "getNotification: " + remoteMessage.getNotification().toString());
        Log.e(TAG, "getData: " + remoteMessage.getData().toString());
        Log.e(TAG, "FROM: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            String message = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
            Timestamp tsTemp = new Timestamp(new Date().getTime());
            String timestamp = tsTemp.toString();
            Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            resultIntent.putExtra("message", message);
            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}