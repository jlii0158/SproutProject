package com.example.sproutproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Messenger;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.example.sproutproject.utils.ToastUtils;
import com.example.sproutproject.viewmodel.PlanViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class PlanDetailActivity extends AppCompatActivity {
    private ImageView iv_edit_plan;
    private TextView tv_plan_name, tv_number_of_days, tv_plan_start_date, tv_grandTotal, tv_real_water_times, tv_total_days_of_plan;
    private ProgressBar pb_true_value, pb_real;
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    int waterDays = 0;
    private CardView cv_generate_plan;
    String plant_img, waterNeed, harvest_time;
    public static int info = 0;
    public static String startDatePass;
    static PlanDetailActivity instance;
    private TextView tv_choose_background, tv_plan_back_button;
    private LinearLayout ll_plan_background, toastView;
    private static final int CHOOSE_PHOTO = 385;
    private static final int TAKE_PHOTO = 189;
    private final String TAG = getClass().getSimpleName();
    private Toast toast = null;
    private Toast toastWithImage = null;
    private ToastUtils toastUtils = new ToastUtils();
    PlanDatabase db = null;
    Plan plan;
    int planidPass, waterSum;
    PlanViewModel planViewModel;
    private Button bt_water_main;
    SharedPreferences preferences;
    final boolean falg = false;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        tv_plan_back_button = findViewById(R.id.tv_plan_back_button);
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
        bt_water_main = findViewById(R.id.bt_water_main);
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        planViewModel.initalizeVars(getApplication());

        final String aaa = planDisplay.getPlanBackground();

        if (aaa != null) {
            final Uri uri = Uri.parse((String) aaa);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                //bitmap = compressImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cv_generate_plan.setAlpha((float) 0.8);
            ll_plan_background.setBackground(new BitmapDrawable(getResources(),bitmap));

        }


        tv_choose_background.setOnClickListener(new OnClickListenerImpl());

        tv_plan_back_button.setOnClickListener(new View.OnClickListener() {
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
        final String totalDays = "/" + String.valueOf(days) + " days";
        tv_number_of_days.setText(planDisplay.getDaysToCurrentDate());
        tv_total_days_of_plan.setText(totalDays);

        if (Integer.parseInt(planDisplay.getDaysToCurrentDate()) >= days) {
            ImageView toastImage = new ImageView(getApplicationContext());
            toastImage.setImageResource(R.drawable.ic_like);
            toastUtils.Long(PlanDetailActivity.this,"          Well done          ").addView(toastImage,0).show();
        }

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                RestClient restClient = new RestClient();
                String result = restClient.findPlantByName(planDisplay.getPlant_name());
                //plan = db.planDao().findByID(planidPass);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    plant_img = jsonArray.getJSONObject(0).getString("plantDesc");
                    waterNeed = jsonArray.getJSONObject(0).getString("waterNeed");
                    harvest_time = jsonArray.getJSONObject(0).getString("growthCycle");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if (preferences.getBoolean("flag", falg)) {
            final int finalDays = days;
            ThreadUtils.runInThread(new Runnable() {
                @Override
                public void run() {
                    plan = db.planDao().findByID(planidPass);
                    ThreadUtils.runInUIThread(new Runnable() {
                        @Override
                        public void run() {

                            waterDays = plan.waterNeed;
                            //waterSum是这个计划总共需要浇多少次水
                            waterSum = finalDays / waterDays;
                            int totalValue = 0;
                            if (Integer.parseInt(planDisplay.getDaysToCurrentDate()) < finalDays) {
                                totalValue = Integer.parseInt(planDisplay.getDaysToCurrentDate()) / waterDays + 1;
                            } else {
                                totalValue = waterSum;
                            }

                            if (Integer.parseInt(planDisplay.getDaysToCurrentDate()) < finalDays) {
                                if (plan.waterState == 0) {
                                    tv_plan_name.setBackgroundColor(Color.parseColor("#FED46E"));
                                    bt_water_main.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_shape_red));
                                }
                                if (totalValue > plan.getRealWaterCount()) {
                                    plan.setRealWaterCount(totalValue);
                                }
                            } else {
                                tv_plan_name.setBackgroundColor(Color.parseColor("#C1C1C1"));
                                bt_water_main.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_shape_grey));
                            }


                            //Required watering times
                            String grandTotal = "Required: " + totalValue + "/" + waterSum;
                            tv_grandTotal.setText(grandTotal);
                            pb_true_value.setMax(waterSum);
                            pb_true_value.setProgress(totalValue);
                            //Actual watering times
                            String realTotal = "Actual: " + String.valueOf(planDisplay.getWaterCount()) + "/" + String.valueOf(waterSum);
                            tv_real_water_times.setText(realTotal);
                            pb_real.setMax(waterSum);
                            pb_real.setProgress(planDisplay.getWaterCount());
                        }
                    });

                }
            });
        } else {
            final int finalDays = days;
            ThreadUtils.runInThread(new Runnable() {
                @Override
                public void run() {
                    RestClient restClient = new RestClient();
                    String result = restClient.findPlantByName(planDisplay.getPlant_name());
                    plan = db.planDao().findByID(planidPass);
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
                            //totalValue是到目前为止需要浇多少次水
                            //int totalValue = Integer.parseInt(planDisplay.getDaysToCurrentDate()) / waterDays + 1;
                            //waterSum是这个计划总共需要浇多少次水
                            waterSum = finalDays / waterDays;
                            int totalValue = 0;
                            if (Integer.parseInt(planDisplay.getDaysToCurrentDate()) < finalDays) {
                                totalValue = Integer.parseInt(planDisplay.getDaysToCurrentDate()) / waterDays + 1;
                            } else {
                                totalValue = waterSum;
                            }
                            //每次计算当前需要浇水多少次，然后从数据库取出来记录的需要浇水的次数
                            //如果当前的这个次数，大于数据库里存的次数
                            //就把数据库里的那个数据更新，就说明又可以浇水了
                            if (totalValue > plan.getRealWaterCount()) {
                                tv_plan_name.setBackgroundColor(Color.parseColor("#FED46E"));
                                bt_water_main.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_shape_red));
                                plan.setRealWaterCount(totalValue);
                                plan.setWaterState(0);
                                planViewModel.update(plan);
                            }

                            if (Integer.parseInt(planDisplay.getDaysToCurrentDate()) < finalDays) {
                                if (plan.waterState == 0) {
                                    if (Integer.parseInt(planDisplay.getDaysToCurrentDate()) % waterDays == 0) {
                                        tv_plan_name.setBackgroundColor(Color.parseColor("#FED46E"));
                                        bt_water_main.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_shape_red));
                                    }
                                }
                            } else {
                                tv_plan_name.setBackgroundColor(Color.parseColor("#C1C1C1"));
                                bt_water_main.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_shape_grey));
                            }

                            //waterSum是这个计划总共需要浇多少次水
                            waterSum = finalDays /waterDays;
                            //Required watering times
                            String grandTotal = "Required: " + totalValue + "/" + waterSum;
                            tv_grandTotal.setText(grandTotal);
                            pb_true_value.setMax(waterSum);
                            pb_true_value.setProgress(totalValue);
                            //Actual watering times
                            String realTotal = "Actual: " + String.valueOf(planDisplay.getWaterCount()) + "/" + String.valueOf(waterSum);
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
        }




        bt_water_main.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(planDisplay.getDaysToCurrentDate()) % waterDays == 0) {
                    if (plan.getWaterState() == 1) {
                        ImageView toastImage = new ImageView(getApplicationContext());
                        toastImage.setImageResource(R.drawable.ic_rain);
                        centralToast("   It's been watered today   ", toastImage);
                    } else {
                        int currentWaterCount = plan.getWaterCount() + 1;
                        plan.setWaterCount(currentWaterCount);
                        plan.setWaterState(1);
                        planViewModel.update(plan);
                        pb_real.setProgress(plan.getWaterCount());
                        String realTotal = "Actual Water Times: " + String.valueOf(plan.getWaterCount()) + "/" + String.valueOf(waterSum);
                        tv_real_water_times.setText(realTotal);
                        tv_plan_name.setBackgroundColor(Color.parseColor("#A2D89F"));
                        bt_water_main.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_shape));

                        ImageView toastImage = new ImageView(getApplicationContext());
                        toastImage.setImageResource(R.drawable.ic_90crown);
                        toastUtils.Long(PlanDetailActivity.this,"          Well done          ").addView(toastImage,0).show();
                    }
                } else {
                    ImageView toastImage = new ImageView(getApplicationContext());
                    toastImage.setImageResource(R.drawable.ic_forbiden);
                    centralToast("     Need no water today     ", toastImage);
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= 23) {
            if(Objects.requireNonNull(PlanDetailActivity.this).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(PlanDetailActivity.this,new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{
                startActivityForResult(intent,TAKE_PHOTO);
            }
        } else {
            startActivityForResult(intent,TAKE_PHOTO);
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
                        final Uri imageUri = data.getData();
                        final String uriString = imageUri.toString();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //bitmap = compressImage(bitmap);
                        final Bitmap finalBitmap = bitmap;
                        ThreadUtils.runInThread(new Runnable() {
                            @Override
                            public void run() {
                                plan = db.planDao().findByID(planidPass);
                                plan.setPlanBackground(uriString);
                                planViewModel.update(plan);
                                ThreadUtils.runInUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ll_plan_background.setBackground(new BitmapDrawable(getResources(), finalBitmap));
                                        cv_generate_plan.setAlpha((float) 0.8);
                                        showToast("Background change success");
                                    }
                                });
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

    public void centralToast(String msg, ImageView imageView){
        if(toastWithImage!=null){
            //toastWithImage.setText(msg);
            //toastWithImage.setDuration(Toast.LENGTH_LONG);
            toastWithImage.show();
        }else{
            toastWithImage = Toast.makeText(PlanDetailActivity.this,msg,Toast.LENGTH_SHORT);
            toastWithImage.setGravity(Gravity.CENTER, 0, 0);
            toastView = (LinearLayout) toastWithImage.getView();
            toastView.addView(imageView,0);
            toastWithImage.show();
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


    private class OnClickListenerImpl implements View.OnClickListener {

        public void onClick(View v) {
            Dialog dialog = new AlertDialog.Builder(PlanDetailActivity.this)
                    .setTitle("Background")  // create title
                    .setMessage("CHANGE or DELETE?")    //create content
                    .setIcon(R.drawable.ic_album111) //set logo
                    .setNeutralButton("CHANGE", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                            cv_generate_plan.setAlpha(1);
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

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024>1024) {  //循环判断如果压缩后图片是否大于1024kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 1;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
