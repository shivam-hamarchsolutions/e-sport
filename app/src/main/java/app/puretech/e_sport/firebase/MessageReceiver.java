package app.puretech.e_sport.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SplashActivity;
import app.puretech.e_sport.utill.AppPreferences;


@SuppressLint("Registered")
public class MessageReceiver extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        AppPreferences.init(this).setString("fcm_token",s);
        Log.d("xxxxxx", "onNewToken: "+s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    /*    if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("title"));
            Map<String, String> data = remoteMessage.getData();
            handleData(String.valueOf(data));
        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle());
        }// Check if message contains a notification payload.
*/

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {


            String body = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();

            Log.e("mnq", "Notification:" + title);

            handleNotification(title, body);
        }

        if (remoteMessage.getData() != null) {
            Map<String, String> data = remoteMessage.getData();


            Log.e("mnq", "data:" + data.size());

            String body = data.get("body");
            String title = data.get("title");

            handleNotification(title, body);
            //      String myCustomKey = data.get("my_custom_key");

        }

        //sendNotification(remotemsg.getNotification().getTitle());
    }

    private void handleNotification(String title, String body) {
        String newBody = body.substring(1, body.length() - 1);
        String newBody1 = newBody.replace("\"", "");

        Random random = new Random();
        int randInt = random.nextInt(9999 - 1000) + 1000;

        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {

            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this);

        mNotificationBuilder.setSmallIcon(R.drawable.notification_logo);
        mNotificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.e_sport_new_logo));
        mNotificationBuilder.setContentTitle(title);
        mNotificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(newBody1))
                .setContentText(newBody1);
        // mNotificationBuilder.setContentText(newBody);
        mNotificationBuilder.setAutoCancel(true);
        // mNotificationBuilder.setSound(defaultSoundUri);
        mNotificationBuilder.setContentIntent(pendingIntent);
        mNotificationBuilder.setSound(defaultSoundUri);
        mNotificationBuilder.setVibrate(new long[0]);
        mNotificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        mNotificationBuilder.setPriority(Notification.PRIORITY_HIGH);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "121212";
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(channelId,
                    "lng",
                    NotificationManager.IMPORTANCE_HIGH);


            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            // Configure the notification channel.

            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(defaultSoundUri, attributes);

            notificationManager.createNotificationChannel(channel);
            mNotificationBuilder.setChannelId(channelId);
        }
        notificationManager.notify(randInt, mNotificationBuilder.build());
    }

}