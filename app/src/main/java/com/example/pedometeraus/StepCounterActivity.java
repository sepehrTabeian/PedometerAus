package com.example.pedometeraus;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
// StepCounter.java

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    private static final int ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE = 1001;

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private Sensor stepDetectorSensor;
    private boolean isCounterSensorPresent;
    private boolean isDetectorSensorPresent;
    private int stepCount = 0;
    private int initialStepCount = -1; // Used to store the initial step count
    private TextView stepCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stepCountTextView = findViewById(R.id.stepCountTextView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE);
        } else {
            initializeSensors();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize sensors
                initializeSensors();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied for activity recognition", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        isCounterSensorPresent = stepCounterSensor != null;
        isDetectorSensorPresent = stepDetectorSensor != null;

        if (!isCounterSensorPresent && !isDetectorSensorPresent) {
            Toast.makeText(this, "Step sensors not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCounterSensorPresent) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (isDetectorSensorPresent) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepCount < 0) {
                initialStepCount = (int) event.values[0];
            }
            stepCount = (int) event.values[0] - initialStepCount;
            updateStepCountUI();
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            updateStepCountUI();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can react to changes in sensor accuracy here if needed
    }

    private void updateStepCountUI() {
        stepCountTextView.setText("Steps: " + stepCount);
    }
}



//public class StepCounterActivity extends Activity implements SensorEventListener {
//        private static final String TAG = "StepCounterActivity";
//
//    private SensorManager sensorManager;
//    private Sensor accelerometerSensor;
//    private float[] gravity = new float[3];
//    private float[] linearAcceleration = new float[3];
//    private int stepCount = 0;
//    private TextView stepCountTextView; // Replace with your TextView ID
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main); // Set your layout XML here
//
//        // Initialize sensor manager
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//        // Find your TextView by ID
//        stepCountTextView = findViewById(R.id.stepCountTextView); // Replace with your TextView ID
//
//        // Register accelerometer listener
//        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float x = event.values[0];
//        float y = event.values[1];
//        float z = event.values[2];
//
//        // Apply low-pass filter to obtain linear acceleration
//        applyLowPassFilter(x, y, z);
//
//        // Detect peaks (you can customize this logic)
//        if (isPeak(linearAcceleration)) {
//            stepCount++;
//            updateStepCountUI();
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Handle accuracy changes if needed
//    }
//
//    // Apply low-pass filter to isolate linear acceleration
//    private void applyLowPassFilter(float x, float y, float z) {
//        float alpha = 0.8f; // Smoothing factor
//        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
//        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
//        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;
//
//        linearAcceleration[0] = x - gravity[0];
//        linearAcceleration[1] = y - gravity[1];
//        linearAcceleration[2] = z - gravity[2];
//    }
//
//    // Custom peak detection logic (you can adjust threshold)
//    private boolean isPeak(float[] acceleration) {
//        // Set a threshold for peak detection (e.g., 2.0)
//        float threshold = 2.0f;
//
//        // Calculate the magnitude of acceleration
//        float magnitude = (float) Math.sqrt(
//                acceleration[0] * acceleration[0] +
//                        acceleration[1] * acceleration[1] +
//                        acceleration[2] * acceleration[2]
//        );
//
//        // Check if the magnitude exceeds the threshold
//        if (magnitude > threshold) {
//            // Only consider it a peak if the previous state was not a peak
//            if (!isPeakDetected) {
//                isPeakDetected = true;
//                return true;
//            }
//        } else {
//            // Reset the peak detection state
//            isPeakDetected = false;
//        }
//
//        // No peak detected
//        return false;
//    }
//
//
//    // Update step count UI
//    private void updateStepCountUI() {
//        stepCountTextView.setText("Steps: " + stepCount);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Unregister sensor listener when the activity is destroyed
//        sensorManager.unregisterListener(this);
//    }
//}


//public class StepCounterActivity extends Activity implements SensorEventListener {
//    private static final String TAG = "StepCounterActivity";
//    private SensorManager sensorManager;
//    private Sensor accelerometer;
//    private float previousY, currentY;
//    private int steps;
//    private static final float STEP_THRESHOLD = 3f; // Threshold value for detecting a step
//    private TextView stepCountTextView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        stepCountTextView = findViewById(R.id.stepCountTextView);
//
//        Log.i(TAG, "onCreate: ");
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        previousY = currentY = steps = 0;
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float x = event.values[0];
//        float y = event.values[1];
//        float z = event.values[2];
//
//        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
//        float delta = acceleration - SensorManager.GRAVITY_EARTH;
//        currentY = delta;
//
//        Log.i(TAG, "onSensorChanged: " + (currentY - previousY));
//        // Detect a step based on the threshold
//        if (currentY - previousY > STEP_THRESHOLD) {
//            steps++;
//            // Update your UI with the step count
//            String stepCountText = getString(R.string.step_count_label,steps );
//            stepCountTextView.setText(stepCountText);
//        }
//        previousY = currentY;
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Handle sensor accuracy changes if needed
//        Log.i(TAG, "onAccuracyChanged: ");
//    }
//
//    @Override
//    protected void onResume() {
//        Log.i(TAG, "onResume: ");
//        super.onResume();
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onPause() {
//        Log.i(TAG, "onPause: ");
//        super.onPause();
//        sensorManager.unregisterListener(this);
//    }
//}
