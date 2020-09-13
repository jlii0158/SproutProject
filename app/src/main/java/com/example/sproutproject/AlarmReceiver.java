package com.example.sproutproject;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.sproutproject.databse.PlanDatabase;
import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.utils.ThreadUtils;
import com.example.sproutproject.viewmodel.PlanViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "sprout";
    PlanDatabase db = null;
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onReceive (final Context context, Intent intent) {

        db = PlanDatabase.getInstance(context);

        final int[] temp = {0};

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                List<Plan> plans = db.planDao().findAllPlan();
                for (int i = 0; i < plans.size(); i++) {
                    int days;
                    try {
                        Date date = df.parse(plans.get(i).startDate);
                        Date date2 = df.parse(getCurrentDate());
                        days = (int) ((date2.getTime() - date.getTime()) / (1000*3600*24));
                        if (days % plans.get(i).waterNeed == 0) {
                            Plan plan = plans.get(i);
                            plan.setWaterState(0);
                            db.planDao().updatePlans(plan);
                            temp[0] = 1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp[0] == 1) {
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                            Intent  repeating_intent = new Intent(context, StartActivity.class);
                            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                    .setContentIntent(pendingIntent)
                                    .setSmallIcon(R.drawable.logo_notification)
                                    .setContentTitle("Sprout")
                                    .setContentText("It's time to water your plant!")
                                    .setAutoCancel(true);
                            notificationManager.notify(0, builder.build());
                        }
                    }
                });
            }
        });

        /*
        //再次开启LongRunningService这个服务，从而可以
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
         */

    }

    public static String getCurrentDate() {
        long cur_time = System.currentTimeMillis();
        String datetime = df.format(new Date(cur_time));
        return datetime;
    }

}
