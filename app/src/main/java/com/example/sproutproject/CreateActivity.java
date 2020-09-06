package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.database_entity.Plant;
import com.example.sproutproject.databse.PlanDatabase;
import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.fragment.SearchFragment;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateActivity extends AppCompatActivity {

    String startDate, endDateReal, endDateVir;
    int waterDays;
    private ImageView iv_plan_img, iv_plan_img_bottom, iv_back_to_detail, iv_back_to_detail1;
    private EditText ed_plan_name, ed_plan_name_bottom;
    private TextView plan_water_need, plan_harvest_time, plan_start_time, plan_end_time, plan_water_need_bottom, virtual_plan_start_time;
    private Button real_plan_choice, virtual_plan_choice, cycle_min, cycle_plus, plan_submit, virtual_plan_submit;
    private TextView cycle_day_count;
    private LinearLayout generate_plan_top_view, generate_plan_bottom_view;
    private Toast toast = null;
    private TextView tv_plan_title;
    PlanDatabase db = null;
    String planNameToDatabase, plantName, plantImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        db = PlanDatabase.getInstance(this);

        Intent intent = getIntent();
        final Plant plant = (Plant) intent.getSerializableExtra("onePlant");
        plantName = plant.getPlant_name();
        plantImg = plant.getPlant_img();

        iv_plan_img = findViewById(R.id.iv_plan_img);
        iv_plan_img_bottom = findViewById(R.id.iv_plan_img_bottom);
        ed_plan_name = findViewById(R.id.ed_plan_name);
        plan_water_need = findViewById(R.id.plan_water_need);
        plan_harvest_time = findViewById(R.id.plan_harvest_time);
        plan_start_time = findViewById(R.id.plan_start_time);
        plan_end_time = findViewById(R.id.plan_end_time);
        real_plan_choice = findViewById(R.id.real_plan_choice);
        virtual_plan_choice = findViewById(R.id.virtual_plan_choice);
        generate_plan_top_view = findViewById(R.id.generate_plan_top_view);
        generate_plan_bottom_view = findViewById(R.id.generate_plan_bottom_view);
        ed_plan_name_bottom = findViewById(R.id.ed_plan_name_bottom);
        plan_water_need_bottom = findViewById(R.id.plan_water_need_bottom);
        virtual_plan_start_time= findViewById(R.id.virtual_plan_start_time);
        cycle_min = findViewById(R.id.cycle_min);
        cycle_plus = findViewById(R.id.cycle_plus);
        cycle_day_count = findViewById(R.id.cycle_day_count);
        iv_back_to_detail = findViewById(R.id.iv_back_to_detail);
        tv_plan_title = findViewById(R.id.tv_plan_title);
        iv_back_to_detail1 = findViewById(R.id.iv_back_to_detail1);
        plan_submit = findViewById(R.id.plan_submit);
        virtual_plan_submit = findViewById(R.id.virtual_plan_submit);


        cycle_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cycle_count = Integer.parseInt(cycle_day_count.getText().toString());
                if (cycle_count > 3) {
                    cycle_count -= 1;
                    cycle_day_count.setText(String.valueOf(cycle_count));
                }else {
                    String back = "We can't go any lower";
                    showToast(back);
                }
            }
        });

        cycle_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cycle_count = Integer.parseInt(cycle_day_count.getText().toString());
                if (cycle_count < 10) {
                    cycle_count += 1;
                    cycle_day_count.setText(String.valueOf(cycle_count));
                }else {
                    String back = "We can't go any higher";
                    showToast(back);
                }
            }
        });


        Picasso.get().load(plant.getPlant_desc()).into(iv_plan_img);

        //set name
        String planName = plant.getPlant_name() + " planting plan";
        ed_plan_name.setHint(planName);

        //set water need
        switch (plant.getWater_need()){
            case "low":
                waterDays = 5;
                String waterNeed = "My water need is Low. Please water me once every 5 days!";
                plan_water_need.setText(waterNeed);
                break;
            case "medium":
                waterDays = 3;
                String waterNeed1 = "My water need is Medium. Please water me once every 3 days!";
                plan_water_need.setText(waterNeed1);
                break;
            case "high":
                waterDays = 1;
                String waterNeed2 = "My water need is High. Please water me everyday!";
                plan_water_need.setText(waterNeed2);
                break;
        }
        //set cycle
        String cycle = "I will grow with you for the next " + plant.getGrowth_cycle() + " weeks.";
        plan_harvest_time.setText(cycle);
        //set start date
        startDate = getCurrentDate();
        final String dateDesc = "We will start our journey at " + startDate;
        plan_start_time.setText(dateDesc);
        //set end date for real plan
        try {
            String endDate = getWorkDay(startDate, Integer.parseInt(plant.getGrowth_cycle()) * 7);
            endDateReal = endDate;
            String endDateDesc = "Our journey will end on " + endDate;
            plan_end_time.setText(endDateDesc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        plan_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planNameToDatabase = ed_plan_name.getText().toString().trim();
                if (planNameToDatabase.equals("")) {
                    showToast("Please set a plan name.");
                    return;
                }
                new InsertDatabase().execute();

            }
        });


        real_plan_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate_plan_top_view.setVisibility(View.INVISIBLE);
                generate_plan_bottom_view.setVisibility(View.VISIBLE);
                String virtual = "Virtual Plan";
                tv_plan_title.setText(virtual);
                Picasso.get().load(plant.getPlant_desc()).into(iv_plan_img_bottom);
                String planName = plant.getPlant_name() + " planting virtual plan";
                ed_plan_name_bottom.setHint(planName);
                switch (plant.getWater_need()){
                    case "low":
                        waterDays = 5;
                        String waterNeed = "My water need is Low. Enjoy!";
                        plan_water_need_bottom.setText(waterNeed);
                        break;
                    case "medium":
                        waterDays = 3;
                        String waterNeed1 = "My water need is Medium. Enjoy!";
                        plan_water_need_bottom.setText(waterNeed1);
                        break;
                    case "high":
                        waterDays = 1;
                        String waterNeed2 = "My water need is High. Enjoy!";
                        plan_water_need_bottom.setText(waterNeed2);
                        break;
                }
                final String dateDesc = "You will start your trial at " + startDate;
                virtual_plan_start_time.setText(dateDesc);
                //set end date for virtual plan
                try {
                    String endDate = getWorkDay(startDate, Integer.parseInt(cycle_day_count.getText().toString()));
                    endDateVir = endDate;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        virtual_plan_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate_plan_bottom_view.setVisibility(View.INVISIBLE);
                generate_plan_top_view.setVisibility(View.VISIBLE);
                String virtual = "Plan";
                tv_plan_title.setText(virtual);
            }
        });

        iv_back_to_detail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        iv_back_to_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class InsertDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Plan plan = new Plan(planNameToDatabase, startDate, endDateReal, 0, 0, plantName, plantImg);
            long id = db.planDao().insert(plan);
            return "";
        }
        @Override
        protected void onPostExecute(String details) {
            showToast("Plan created successfully");
            Intent intent = new Intent(CreateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static String getCurrentDate() {
        long cur_time = System.currentTimeMillis();
        String datetime = df.format(new Date(cur_time));
        return datetime;
    }

    @SuppressLint("WrongConstant")
    public static String getWorkDay(String startDate, int workDay) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date dt = df.parse(startDate);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dt);
        c1.add(Calendar.DAY_OF_YEAR,workDay);
        Date dt1 = c1.getTime();
        String reStr = df.format(dt1);
        return reStr;
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
}
