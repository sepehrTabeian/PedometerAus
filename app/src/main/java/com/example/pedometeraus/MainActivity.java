package com.example.pedometeraus;


import static com.example.pedometeraus.utils.Constants.PERMISSION_REQUEST_CODE;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Check if the permissions are granted
        if (!checkPermissions()) {
            // Request permissions if not granted
            requestPermissions();
        } else {
            Intent intent = new Intent(this,PedometerService.class);
            startService(intent);
        }



    }
    private boolean checkPermissions() {
        // Check for permission to access activity recognition
        boolean activityRecognitionPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;

        // Check for permission to access foreground service
        boolean foregroundServicePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED;

        // Check for permission to access foreground service for health
        boolean foregroundServiceHealthPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.FOREGROUND_SERVICE_HEALTH) == PackageManager.PERMISSION_GRANTED;

        // Check for permission to post notifications
        boolean postNotificationsPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;

        // Return true if all permissions are granted, otherwise return false
        return activityRecognitionPermission &&
                foregroundServicePermission &&
                foregroundServiceHealthPermission &&
                postNotificationsPermission;
    }

    private void requestPermissions() {
        // Request permissions
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACTIVITY_RECOGNITION,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.FOREGROUND_SERVICE_HEALTH,
                        Manifest.permission.POST_NOTIFICATIONS
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all permissions are granted
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Intent intent = new Intent(this,PedometerService.class);
                startService(intent);
            } else {
                // Permissions not granted, inform the user
                Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}







