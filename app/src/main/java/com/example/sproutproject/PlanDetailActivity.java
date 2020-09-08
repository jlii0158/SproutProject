package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sproutproject.database_entity.PlanDisplay;
import com.example.sproutproject.database_entity.Plant;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlanDetailActivity extends AppCompatActivity {
    private ImageView iv_plan_back_button;
    private TextView tv_plan_name, tv_number_of_days, tv_plan_start_date, tv_grandTotal, tv_real_water_times, tv_total_days_of_plan;
    private ProgressBar pb_true_value, pb_real;
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    int waterDays = 0;
    private CardView cv_generate_plan;
    String plant_img, waterNeed, harvest_time;
    public static int info = 0;
    public static String startDatePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        Intent intent = getIntent();
        final PlanDisplay planDisplay = (PlanDisplay) intent.getSerializableExtra("planDisplay");

        iv_plan_back_button = findViewById(R.id.iv_plan_back_button);
        tv_plan_name = findViewById(R.id.tv_plan_name);
        tv_number_of_days = findViewById(R.id.tv_number_of_days);
        tv_plan_start_date = findViewById(R.id.tv_plan_start_date);
        pb_true_value = findViewById(R.id.pb_true_value);
        pb_real = findViewById(R.id.pb_real);
        tv_grandTotal = findViewById(R.id.tv_grandTotal);
        tv_real_water_times = findViewById(R.id.tv_real_water_times);
        tv_total_days_of_plan = findViewById(R.id.tv_total_days_of_plan);
        cv_generate_plan = findViewById(R.id.cv_generate_plan);
        startDatePass = planDisplay.getStartDate();

        iv_plan_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanDetailActivity.this, MainActivity.class);
                intent.putExtra("pid",1);
                startActivity(intent);
                finish();
            }
        });




        tv_plan_name.setText(planDisplay.getPlanName());

        String startString = "Starting On: " + planDisplay.getStartDate();
        tv_plan_start_date.setText(startString);


        int days = 0;
        try {
            Date date = df.parse(planDisplay.getStartDate());
            Date date2 = df.parse(planDisplay.getEndDate());
            days = (int) ((date2.getTime() - date.getTime()) / (1000*3600*24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String totalDays = "/" + String.valueOf(days) + " days";
        tv_number_of_days.setText(planDisplay.getDaysToCurrentDate());
        tv_total_days_of_plan.setText(totalDays);

        final int finalDays = days;
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                RestClient restClient = new RestClient();
                String result = restClient.findPlantByName(planDisplay.getPlant_name());
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    plant_img = jsonArray.getJSONObject(0).getString("plantDesc");
                    waterNeed = jsonArray.getJSONObject(0).getString("waterNeed");
                    harvest_time = jsonArray.getJSONObject(0).getString("growthCycle");

                    switch (waterNeed){
                        case "low":
                            waterDays = 5;
                            break;
                        case "medium":
                            waterDays = 3;
                            break;
                        case "high":
                            waterDays = 1;
                            break;
                    }
                    ThreadUtils.runInUIThread(new Runnable() {
                        @Override
                        public void run() {
                            int waterSum = finalDays /waterDays;
                            //设置应该浇水多少次
                            String grandTotal = "Grand Total: " + String.valueOf(planDisplay.getRealWaterCount()) + "/" + String.valueOf(waterSum);
                            tv_grandTotal.setText(grandTotal);
                            pb_true_value.setMax(waterSum);
                            pb_true_value.setProgress(planDisplay.getRealWaterCount());
                            //设置实际浇了几次水
                            String realTotal = "Actual Water Times: " + String.valueOf(planDisplay.getWaterCount()) + "/" + String.valueOf(waterSum);
                            tv_real_water_times.setText(realTotal);
                            pb_real.setMax(waterSum);
                            pb_real.setProgress(planDisplay.getWaterCount());

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        cv_generate_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plant plant = new Plant();
                plant.setPlant_desc(plant_img);
                plant.setWater_need(waterNeed);
                plant.setGrowth_cycle(harvest_time);
                info = 1;
                Intent intent = new Intent(PlanDetailActivity.this, CreateActivity.class);
                intent.putExtra("onePlant", plant);
                startActivity(intent);
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(PlanDetailActivity.this, MainActivity.class);
        intent.putExtra("pid",1);
        startActivity(intent);
        finish();
    }
}
