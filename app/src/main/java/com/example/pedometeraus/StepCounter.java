package com.example.pedometeraus;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepCounter implements SensorEventListener {
    private static final String TAG = "StepCounter";
    private static final int THRESHOLD = 20; // Threshold for determining significant step increase
    private int currentStep = 0;
    private int previousStepCount = 0;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    public StepCounter(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    public void start() {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public int getCurrentStep() {
        return currentStep;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int newStepCount = (int) event.values[0];
            int delta = newStepCount - previousStepCount;
            if (Math.abs(delta) > THRESHOLD) {
                currentStep += delta;
                previousStepCount = newStepCount;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this implementation
    }
}
