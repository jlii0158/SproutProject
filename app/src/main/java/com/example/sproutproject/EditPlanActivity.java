package com.example.sproutproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.database_entity.PlanDisplay;
import com.example.sproutproject.databse.PlanDatabase;
import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.utils.ThreadUtils;
import com.example.sproutproject.viewmodel.PlanViewModel;
import com.example.sproutproject.viewmodel.PlantViewModel;

import java.util.Calendar;
import java.util.List;

public class EditPlanActivity extends AppCompatActivity {
    private EditText et_change_name;
    private Button edit_delete, edit_save;
    PlanViewModel planViewModel;
    private Toast toast = null;
    int planidPass;
    PlanDatabase db = null;
    Plan plan;
    private TextView tv_edit_back_button;
    private Switch sw_notification_bar;
    private PendingIntent pendingIntent;
    public static final String CHANNEL_ID = "sprout";
    SharedPreferences preferences;
    final boolean falg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);


        db = PlanDatabase.getInstance(this);

        et_change_name = findViewById(R.id.et_change_name);
        edit_delete = findViewById(R.id.edit_delete);
        edit_save = findViewById(R.id.edit_save);
        tv_edit_back_button = findViewById(R.id.tv_edit_back_button);
        sw_notification_bar = findViewById(R.id.sw_notification_bar);
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        plan = new Plan();

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        planViewModel.initalizeVars(getApplication());

        Intent intent = getIntent();
        final PlanDisplay planDisplay = (PlanDisplay) intent.getSerializableExtra("wholePlanDisplay");
        final String planNameChange = planDisplay.getPlanName();
        planidPass = planDisplay.getPlanId();
        et_change_name.setText(planNameChange);

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                plan = db.planDao().findByID(planidPass);
            }
        });

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlanName = et_change_name.getText().toString().trim();
                plan.setPlanName(newPlanName);
                planDisplay.setPlanName(newPlanName);
                planViewModel.update(plan);
                showToast("Plan name change success");
                PlanDetailActivity.instance.finish();
                Intent intent = new Intent(EditPlanActivity.this, PlanDetailActivity.class);
                intent.putExtra("planDisplay", planDisplay);
                startActivity(intent);
                finish();
            }
        });

        edit_delete.setOnClickListener(new OnClickListenerImpl());

        tv_edit_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (preferences != null) {
            boolean name = preferences.getBoolean("flag", falg);
            sw_notification_bar.setChecked(name);
        }
        sw_notification_bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (isChecked) {

                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("flag", true);
                    editor.commit();

                    createNotificationChannel();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 12);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    showToast("Notification Started");
                } else {

                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("flag", false);
                    editor.commit();

                    cancelAlarm();
                }
            }
        });

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void cancelAlarm() {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        showToast("Notification Canceled");
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class OnClickListenerImpl implements View.OnClickListener {

        public void onClick(View v) {
            Dialog dialog = new AlertDialog.Builder(EditPlanActivity.this)
                    .setTitle("Delete plan")  // create title
                    .setMessage("Are you sure to delete the plan?")    //create content
                    .setIcon(R.drawable.ic_garbage) //set logo
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("Delete plan success");
                            planViewModel.delete(planidPass);
                            Intent intent = new Intent(EditPlanActivity.this, MainActivity.class);
                            intent.putExtra("pid",1);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();  //create dialog
            dialog.show();  //show dialog

        }

    }
}
