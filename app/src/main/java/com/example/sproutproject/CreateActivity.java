package com.example.sproutproject;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.example.sproutproject.viewmodel.PlanViewModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    String startDate, endDateReal, endDateVir;
    int waterDays;
    private ImageView iv_plan_img, iv_plan_img_bottom, iv_back_to_detail1;
    private EditText ed_plan_name, ed_plan_name_bottom;
    private TextView plan_water_need, plan_harvest_time, plan_start_time, plan_end_time, plan_water_need_bottom, virtual_plan_start_time;
    private Button real_plan_choice, virtual_plan_choice, cycle_min, cycle_plus, plan_submit, virtual_plan_submit;
    private TextView cycle_day_count;
    private LinearLayout generate_plan_top_view, generate_plan_bottom_view;
    private Toast toast = null;
    private TextView tv_plan_title;
    PlanDatabase db = null;
    String planNameToDatabase, plantName, plantImg;
    SharedPreferences preferences;
    static PlanViewModel planViewModel;
    int planSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Log.d("CreateActivity", this.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(CreateActivity.this,R.color.colorBackground); }

        db = PlanDatabase.getInstance(this);

        Intent intent = getIntent();
        final Plant plant = (Plant) intent.getSerializableExtra("onePlant");
        plantName = plant.getPlant_name();
        plantImg = plant.getPlant_img();

        iv_plan_img = findViewById(R.id.iv_plan_img);

        ed_plan_name = findViewById(R.id.ed_plan_name);
        plan_water_need = findViewById(R.id.plan_water_need);
        plan_harvest_time = findViewById(R.id.plan_harvest_time);
        plan_start_time = findViewById(R.id.plan_start_time);
        plan_end_time = findViewById(R.id.plan_end_time);

        generate_plan_top_view = findViewById(R.id.generate_plan_top_view);

        tv_plan_title = findViewById(R.id.tv_plan_title);

        plan_submit = findViewById(R.id.plan_submit);

        /*
        real_plan_choice = findViewById(R.id.real_plan_choice);
        virtual_plan_choice = findViewById(R.id.virtual_plan_choice);
        iv_plan_img_bottom = findViewById(R.id.iv_plan_img_bottom);
        generate_plan_bottom_view = findViewById(R.id.generate_plan_bottom_view);
        ed_plan_name_bottom = findViewById(R.id.ed_plan_name_bottom);
        plan_water_need_bottom = findViewById(R.id.plan_water_need_bottom);
        virtual_plan_start_time= findViewById(R.id.virtual_plan_start_time);
        cycle_min = findViewById(R.id.cycle_min);
        cycle_plus = findViewById(R.id.cycle_plus);
        cycle_day_count = findViewById(R.id.cycle_day_count);
        virtual_plan_submit = findViewById(R.id.virtual_plan_submit);
        iv_back_to_detail1 = findViewById(R.id.iv_back_to_detail1);

         */



        /*
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

         */

        plan_submit.setVisibility(View.VISIBLE);

        Picasso.get().load(plant.getPlant_desc()).into(iv_plan_img);

        //set name
        String planName = plant.getPlant_name() + " planting plan";
        ed_plan_name.setHint(planName);

        //set water need
        switch (plant.getWater_need()){
            case "low":
                waterDays = 5;
                String waterNeed = "My water needs are Low. Please water me once every 5 days!";
                plan_water_need.setText(waterNeed);
                break;
            case "medium":
                waterDays = 3;
                String waterNeed1 = "My water needs are Medium. Please water me once every 3 days!";
                plan_water_need.setText(waterNeed1);
                break;
            case "high":
                waterDays = 1;
                String waterNeed2 = "My water needs are High. Please water me everyday!";
                plan_water_need.setText(waterNeed2);
                break;
        }
        //set cycle
        String cycle = "I can grow in " + plant.getGrowth_cycle() + " weeks.";
        plan_harvest_time.setText(cycle);


        if (PlanDetailActivity.info == 0) {
            //set start date
            startDate = getCurrentDate();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dfTemp = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = (Date)formatter.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String reStr = dfTemp.format(date);

            final String dateDesc = "We can start our journey on " + reStr;
            plan_start_time.setText(dateDesc);
        } else {
            //set start date
            startDate = PlanDetailActivity.startDatePass;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dfTemp = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = (Date)formatter.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String reStr = dfTemp.format(date);

            final String dateDesc = "Plan started on " + reStr;
            plan_start_time.setText(dateDesc);

            String planNamePass = (String) intent.getSerializableExtra("planName");
            ed_plan_name.setText(planNamePass);
            ed_plan_name.setFocusable(false);
            plan_submit.setVisibility(View.GONE);
        }



        //set end date for real plan
        try {
            String endDate = getWorkDay(startDate, Integer.parseInt(plant.getGrowth_cycle()) * 7);
            endDateReal = endDate;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dfTemp = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = (Date)formatter.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String reStr = dfTemp.format(date);

            String endDateDesc = "Our journey will end on " + reStr;
            plan_end_time.setText(endDateDesc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        planViewModel.initalizeVars(getApplication());
        planViewModel.getAllPlans().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(@Nullable final List<Plan> plans) {
                if (plans == null) {
                    planSize = 0;
                } else {
                    planSize = plans.size();
                }

            }
        });

        plan_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planNameToDatabase = ed_plan_name.getText().toString().trim();
                if (planNameToDatabase.equals("")) {
                    showToast("Please set a plan name.");
                    return;
                }

                if (planSize < 10) {
                    preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                    String growValue = preferences.getString("growValue", null);
                    int intGrowValue = Integer.parseInt(growValue);
                    //if (intGrowValue >= 20) {
                        growValue = String.valueOf(Integer.parseInt(growValue) + 5);
                        preferences.edit()
                                .putString("growValue", growValue)
                                //.putInt("dailyGrow", dailyGrow)
                                .apply();

                        new InsertDatabase().execute();
                    //} else {
                        //showToast("You don't have enough growth value! Water to get more!");
                    //}
                } else {
                    showToast("You can have up to 10 plans!");
                }



            }
        });


        /*
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
        });*/

        /*
        virtual_plan_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate_plan_bottom_view.setVisibility(View.INVISIBLE);
                generate_plan_top_view.setVisibility(View.VISIBLE);
                String virtual = "Plan";
                tv_plan_title.setText(virtual);
            }
        });*/

        /*
        iv_back_to_detail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         */


        tv_plan_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanDetailActivity.info = 0;
                finish();
            }
        });

    }

    public void onBackPressed() {
        PlanDetailActivity.info = 0;
        finish();
    }

    private class InsertDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Plan plan = new Plan(planNameToDatabase, startDate, endDateReal, 0, 0, plantName, plantImg, 0, waterDays);
            long id = db.planDao().insert(plan);
            return "";
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String details) {
            if (FavoriteActivity.instance != null) {
                //FavoriteActivity.instance.finish();
            }
            //DetailActivity.instance.finish();
            showToast("Plan created successfully! Get 5 growth value");
            //MainActivity.instance.finish();

            Intent intent = new Intent(CreateActivity.this, MainActivity.class);
            //intent.putExtra("pid",1);
            MainActivity.vp_mainPage.setCurrentItem(1);
            startActivity(intent);
            //finish();
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

    public static void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
//            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(colorId);
//        }
    }
}
