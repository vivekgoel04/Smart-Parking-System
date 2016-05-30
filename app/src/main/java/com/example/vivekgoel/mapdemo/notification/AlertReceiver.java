package com.example.vivekgoel.mapdemo.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.vivekgoel.mapdemo.MapsActivity;
import com.example.vivekgoel.mapdemo.R;

/**
 * Created by vivekgoel on 4/29/16.
 */
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, "Parking Finder","Reservation Over", "Alert");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert) {
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, MapsActivity.class), 0);

        android.support.v4.app.NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(context)
                .setContentTitle(msg).setContentText(msgText)
                .setTicker(msgAlert).setSmallIcon(R.drawable.parking_icon);

        mBuilder.setContentIntent(notificIntent);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

}
