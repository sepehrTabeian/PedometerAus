package com.example.pedometeraus;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class PedometerService extends Service {
    private StepCounter stepCounter;
    private final IBinder binder = new PedometerBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        stepCounter = new StepCounter((SensorManager) getSystemService(SENSOR_SERVICE));
        stepCounter.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class PedometerBinder extends Binder {
        PedometerService getService() {
            return PedometerService.this;
        }
    }

    public int getStepCount() {
        return stepCounter.getCurrentStep();
    }
}
