package com.example.sproutproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.database_entity.PlanDisplay;
import com.example.sproutproject.database_entity.Plant;
import com.example.sproutproject.databse.PlanDatabase;
import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.networkConnection.Ingredient;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.networkConnection.TransApi;
import com.example.sproutproject.utils.ThreadUtils;
import com.example.sproutproject.viewmodel.PlanViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class PlanDetailActivity extends AppCompatActivity {
    private ImageView iv_plan_back_button, iv_edit_plan;
    private TextView tv_plan_name, tv_number_of_days, tv_plan_start_date, tv_grandTotal, tv_real_water_times, tv_total_days_of_plan;
    private ProgressBar pb_true_value, pb_real;
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    int waterDays = 0;
    private CardView cv_generate_plan;
    String plant_img, waterNeed, harvest_time;
    public static int info = 0;
    public static String startDatePass;
    static PlanDetailActivity instance;
    private TextView tv_choose_background;
    private LinearLayout ll_plan_background;
    private static final int CHOOSE_PHOTO = 385;
    private final String TAG = getClass().getSimpleName();
    private Toast toast = null;
    PlanDatabase db = null;
    Plan plan;
    int planidPass;
    PlanViewModel planViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        instance = this;

        Intent intent = getIntent();
        final PlanDisplay planDisplay = (PlanDisplay) intent.getSerializableExtra("planDisplay");

        db = PlanDatabase.getInstance(this);
        plan = new Plan();
        planidPass = planDisplay.getPlanId();
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
        iv_edit_plan = findViewById(R.id.iv_edit_plan);
        tv_choose_background = findViewById(R.id.tv_choose_background);
        ll_plan_background = findViewById(R.id.ll_plan_background);

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        planViewModel.initalizeVars(getApplication());

        String aaa = planDisplay.getPlanBackground();
        Bitmap bitmap = stringToBitmap(planDisplay.getPlanBackground());
        if (aaa != null) {
            ll_plan_background.setBackground(new BitmapDrawable(getResources(),bitmap));
        }

        tv_choose_background.setOnClickListener(new OnClickListenerImpl());

        iv_plan_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanDetailActivity.this, MainActivity.class);
                intent.putExtra("pid",1);
                startActivity(intent);
                finish();
            }
        });

        iv_edit_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanDetailActivity.this, EditPlanActivity.class);
                intent.putExtra("planNameChange", planDisplay.getPlanName());
                intent.putExtra("planIdChange", planDisplay.getPlanId());
                intent.putExtra("wholePlanDisplay", planDisplay);
                startActivity(intent);
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
                intent.putExtra("planName", planDisplay.getPlanName());
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

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= 23) {
            if(Objects.requireNonNull(PlanDetailActivity.this).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(PlanDetailActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},222);
                return;
            }else{
                startActivityForResult(intent,CHOOSE_PHOTO);
            }
        } else {
            startActivityForResult(intent,CHOOSE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (data == null) {//如果没有选取照片，则直接返回
                    return;
                }
                Log.i(TAG, "onActivityResult: ImageUriFromAlbum: " + data.getData());
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();

                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ll_plan_background.setBackground(new BitmapDrawable(getResources(),bitmap));
                        showToast("Background change success");
                        final String backgroundBitmap = bitmapToString(bitmap);
                        ThreadUtils.runInThread(new Runnable() {
                            @Override
                            public void run() {
                                plan = db.planDao().findByID(planidPass);
                                plan.setPlanBackground(backgroundBitmap);
                                planViewModel.update(plan);
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(PlanDetailActivity.this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private String bitmapToString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string = Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }


    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private class OnClickListenerImpl implements View.OnClickListener {

        public void onClick(View v) {
            Dialog dialog = new AlertDialog.Builder(PlanDetailActivity.this)
                    .setTitle("Background")  // create title
                    .setMessage("CHANGE or DELETE?")    //create content
                    .setIcon(R.drawable.ic_album111) //set logo
                    .setNeutralButton("CHANGE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            openAlbum();
                        }
                    }).setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ThreadUtils.runInThread(new Runnable() {
                                @Override
                                public void run() {
                                    plan = db.planDao().findByID(planidPass);
                                    plan.setPlanBackground(null);
                                    planViewModel.update(plan);
                                }
                            });

                            ll_plan_background.setBackgroundColor(PlanDetailActivity.this.getResources().getColor(R.color.colorOneButton));
                            showToast("Background delete success");
                        }
                    })/*.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })*/.create();  //create dialog
            dialog.show();  //show dialog

        }

    }
}
