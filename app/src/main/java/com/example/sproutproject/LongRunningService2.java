package com.example.sproutproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class LongRunningService2 extends Service {

    public final static String TAG = "com.example.servicedemo.ServiceTwo";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        thread.start();
        return START_REDELIVER_INTENT;
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    boolean b = EditPlanActivity.isServiceWorked(LongRunningService2.this, "com.example.sproutproject.LongRunningService");
                    if(!b) {
                        Intent service = new Intent(LongRunningService2.this, LongRunningService.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
