package com.example.pedometeraus;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

// StepDetector.java
public class StepDetector implements SensorEventListener {
    private int stepCount = 0;
    private StepUpdateListener listener;

    public interface StepUpdateListener {
        void onStepCountUpdated(int stepCount);
    }

    public void setStepUpdateListener(StepUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            if (listener != null) {
                listener.onStepCountUpdated(stepCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can implement this if you want to respond to changes in sensor accuracy
    }
}