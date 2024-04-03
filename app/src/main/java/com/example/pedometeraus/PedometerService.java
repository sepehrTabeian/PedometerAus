package com.example.pedometeraus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class PedometerService extends Service {
    private static final String TAG = "PedometerService";
    private  static  final int NOTIFICATION_ID = 123;
    private  StepDetector stepDetector;
    public PedometerService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        stepDetector = new StepDetector(() -> {
            // Update UI or perform any other necessary actions when a step is detected
            updateNotification(stepDetector.getStepCount());
        });
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(stepDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e(TAG, "Accelerometer not available");
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    startForeground(NOTIFICATION_ID, createNotification(0));

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(stepDetector);
        stopForeground(true);
    }

    private void updateNotification(int stepCount) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, createNotification(stepCount));
    }
    private Notification createNotification(int stepCount) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return null;
        }

        // Create a notification channel if it doesn't exist
        String channelId = "default_channel";
        NotificationChannel channel = new NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.pedometer_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text) + stepCount)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
//        builder.setContentIntent(pendingIntent);

        return builder.build();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }
}