package com.inwi.clubinwi.FirebaseNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;


public class MyFireBaseMessagingService extends FirebaseMessagingService {


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Utils.LogToConsole("onMessageReceived notif From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Bundle bundle = new Bundle();
//            Utils.LogToConsole("onMessageReceived Notification data payload: " + remoteMessage.getData());


            bundle.putString("cible", remoteMessage.getData().get("cible"));
            Log.e("cible", remoteMessage.getData().get("cible"));
//            bundle.putString("type", remoteMessage.getData().get("type"));
//            bundle.putString("tag_video", remoteMessage.getData().get("tag_video"));
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("type"), remoteMessage.getData().get("sound"), bundle);
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param data
     */
    private void sendNotification(String messageBody, String title, String sound, Bundle data) {
        // if(!"false".equals(ClassUtils.readFromSharedPreferences(getApplicationContext(), Constants.SWITCHER_NOTIFICATION))){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(data);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //int rawResourceId = this.getResources().getIdentifier(sound, "raw", this.getPackageName());
        //Uri uriSound = Uri.parse("android.resource://" + getPackageName() + "/" + rawResourceId);
//        Utils.LogToConsole("resource id : "+rawResourceId +" uri Sound ==> "+uriSound);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        // }

    }


}