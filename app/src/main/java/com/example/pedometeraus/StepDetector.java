package com.example.pedometeraus;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

// StepDetector.java
public class StepDetector implements SensorEventListener {
    private static final String TAG = "StepDetector";
    private static final float STEP_THRESHOLD = 10f;
    private OnStepListener onStepListener;
    private boolean isMoving = false;
    private int stepCount = 0;

    public StepDetector(OnStepListener onStepListener) {
        this.onStepListener = onStepListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float acceleration = calculateAcceleration(event.values[0], event.values[1], event.values[2]);
            Log.i(TAG, "onSensorChanged: acceleration: "+ acceleration);
            if (!isMoving && acceleration > STEP_THRESHOLD) {
                isMoving = true;
                stepCount++;
                onStepListener.onStep();
            } else if (isMoving && acceleration < STEP_THRESHOLD) {
                isMoving = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used, but required to implement SensorEventListener
    }

    private float calculateAcceleration(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public interface OnStepListener {
        void onStep();
    }

    public int getStepCount() {
        return stepCount;
    }

    public void resetStepCount() {
        stepCount = 0;
    }
}
