package com.example.pedometeraus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.pedometeraus.utils.Constants;

public class AppService extends Service  {
    private static final String TAG = "AppService";
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
       startForeground(Constants.foregroundServiceId,createNotification());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
//        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Notification createNotification(){
        NotificationManager notificationManager =  getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel = new NotificationChannel(Constants.notificationID,"Default",NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder   builder = new NotificationCompat.Builder(this,Constants.notificationID)
                .setSmallIcon(R.mipmap.pedometer_icon)
                .setContentTitle(Constants.notificationContentTitle)
                .setContentText(Constants.notificationContentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
//        builder.setContentIntent(pendingIntent);

              return   builder.build();


    }
}
