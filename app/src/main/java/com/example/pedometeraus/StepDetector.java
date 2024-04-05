package com.example.pedometeraus;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class StepDetector implements SensorEventListener {
    private int stepCount = 0;
    private int initialStepCount = -1;
    private Runnable stepCountUpdatedCallback;

    public StepDetector(Runnable stepCountUpdatedCallback) {
        
        this.stepCountUpdatedCallback = stepCountUpdatedCallback;
    }

    public void reset() {
        stepCount = 0;
        initialStepCount = -1;
    }

    public int getStepCount() {
        return stepCount;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepCount < 0) {
                initialStepCount = (int) event.values[0];
            }
            stepCount = (int) event.values[0] - initialStepCount;
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
        }
        if (stepCountUpdatedCallback != null) {
            stepCountUpdatedCallback.run();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can react to changes in sensor accuracy here if needed
    }
}
