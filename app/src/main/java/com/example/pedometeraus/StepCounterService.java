package com.example.pedometeraus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class StepCounterService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    private int initialStepCount = -1;
    private static final String CHANNEL_ID = "StepCounterChannel";
    private static final int NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, getNotification("Steps: 0"));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepCount < 0) {
                initialStepCount = (int) event.values[0];
            }
            stepCount = (int) event.values[0] - initialStepCount;
            updateNotification("Steps: " + stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can react to changes in sensor accuracy here if needed
    }

    private void updateNotification(String contentText) {
        Notification notification = getNotification(contentText);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private Notification getNotification(String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step Counter")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.pedometer_icon) // Replace with your own icon
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }

    private void createNotificationChannel() {

            CharSequence name = "Step Counter Channel";
            String description = "Channel for step counter service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }
}
