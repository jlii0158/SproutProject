package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;
import com.example.sproutproject.utils.ToastUtils;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class PlantGuardActivity extends AppCompatActivity {

    private TextView tv_plant_guard_back_title;
    private ImageView iv_bugs, iv_water, iv_nutrition, iv_sun, iv_rain;
    private ImageView iv_bugs_grey, iv_water_grey, iv_nutrition_grey, iv_sun_grey, iv_rain_grey, iv_plant_normal;
    private ImageView iv_bug_real, iv_cloud, iv_rain_water, iv_refresh;
    private Button bt_check, bt_next;
    private LinearLayout ll_rule;
    private Toast toast = null;
    private List<Integer> generateList, userList;
    private SharedPreferences preferencesGrowValue;
    private ToastUtils toastUtils = new ToastUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_guard);

        tv_plant_guard_back_title = findViewById(R.id.tv_plant_guard_back_title);

        iv_bugs = findViewById(R.id.iv_bugs);
        iv_water = findViewById(R.id.iv_water);
        iv_nutrition = findViewById(R.id.iv_nutrition);
        iv_sun = findViewById(R.id.iv_sun);
        iv_rain = findViewById(R.id.iv_rain);
        iv_plant_normal = findViewById(R.id.iv_plant_normal);

        iv_bugs_grey = findViewById(R.id.iv_bugs_grey);
        iv_water_grey = findViewById(R.id.iv_water_grey);
        iv_nutrition_grey = findViewById(R.id.iv_nutrition_grey);
        iv_sun_grey = findViewById(R.id.iv_sun_grey);
        iv_rain_grey = findViewById(R.id.iv_rain_grey);

        iv_bug_real = findViewById(R.id.iv_bug_real);
        iv_cloud = findViewById(R.id.iv_cloud);
        iv_rain_water = findViewById(R.id.iv_rain_water);

        bt_check = findViewById(R.id.bt_check);
        bt_next = findViewById(R.id.bt_next);
        iv_refresh = findViewById(R.id.iv_refresh);
        ll_rule = findViewById(R.id.ll_rule);

        userList = new ArrayList<>();
        preferencesGrowValue = getSharedPreferences("login", Context.MODE_PRIVATE);

        setRandomPlant();


        ll_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BubbleDialog(PlantGuardActivity.this)
                        .addContentView(LayoutInflater.from(PlantGuardActivity.this).inflate(R.layout.plant_guard_bubble_rule, null))
                        .setClickedView(ll_rule)
                        .setPosition(BubbleDialog.Position.BOTTOM)
                        .setOffsetY(-10)
                        .calBar(true)
                        .show();
            }
        });

        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_cloud.setBackgroundResource(0);
                iv_plant_normal.setBackgroundResource(0);
                iv_bug_real.setBackgroundResource(0);
                iv_rain_water.setBackgroundResource(0);
                setRandomPlant();
                userList.clear();
                iv_bugs.setVisibility(View.GONE);
                iv_bugs_grey.setVisibility(View.VISIBLE);
                iv_water.setVisibility(View.GONE);
                iv_water_grey.setVisibility(View.VISIBLE);
                iv_nutrition.setVisibility(View.GONE);
                iv_nutrition_grey.setVisibility(View.VISIBLE);
                iv_sun.setVisibility(View.GONE);
                iv_sun_grey.setVisibility(View.VISIBLE);
                iv_rain.setVisibility(View.GONE);
                iv_rain_grey.setVisibility(View.VISIBLE);
                bt_next.setVisibility(View.GONE);
                bt_check.setVisibility(View.VISIBLE);
            }
        });

        bt_next. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_cloud.setBackgroundResource(0);
                iv_plant_normal.setBackgroundResource(0);
                iv_bug_real.setBackgroundResource(0);
                iv_rain_water.setBackgroundResource(0);
                setRandomPlant();
                userList.clear();
                iv_bugs.setVisibility(View.GONE);
                iv_bugs_grey.setVisibility(View.VISIBLE);
                iv_water.setVisibility(View.GONE);
                iv_water_grey.setVisibility(View.VISIBLE);
                iv_nutrition.setVisibility(View.GONE);
                iv_nutrition_grey.setVisibility(View.VISIBLE);
                iv_sun.setVisibility(View.GONE);
                iv_sun_grey.setVisibility(View.VISIBLE);
                iv_rain.setVisibility(View.GONE);
                iv_rain_grey.setVisibility(View.VISIBLE);
                bt_next.setVisibility(View.GONE);
                bt_check.setVisibility(View.VISIBLE);
            }
        });

        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sortList(userList);
                if (userList.containsAll(generateList) && generateList.containsAll(userList)) {
                    bt_next.setVisibility(View.VISIBLE);
                    bt_check.setVisibility(View.GONE);

                    iv_cloud.setBackgroundResource(0);
                    iv_plant_normal.setBackgroundResource(0);
                    iv_bug_real.setBackgroundResource(0);
                    iv_rain_water.setBackgroundResource(0);
                    iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                    iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_normal));

                    String growValue = preferencesGrowValue.getString("growValue", null);
                    growValue = String.valueOf(Integer.parseInt(growValue) + 1);
                    preferencesGrowValue.edit()
                            .putString("growValue", growValue)
                            .apply();

                    ImageView toastImage = new ImageView(getApplicationContext());
                    toastImage.setImageResource(R.drawable.ic_90crown);
                    toastUtils.Short(PlantGuardActivity.this,"The guards success! Get 1 growth value!")
                            .addView(toastImage,0)
                            .show();

                    ThreadUtils.runInThread(new Runnable() {
                        @Override
                        public void run() {
                            RestClient.updateGrowValue(
                                    preferencesGrowValue.getString("growValue", null),
                                    preferencesGrowValue.getString("userAccount", null));
                        }
                    });

                } else {
                    showToast("The guards failed! Try again.");
                }
            }
        });

        iv_bugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_bugs.setVisibility(View.GONE);
                iv_bugs_grey.setVisibility(View.VISIBLE);
                userList.remove((Integer) 4);
            }
        });
        iv_bugs_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_bugs.setVisibility(View.VISIBLE);
                iv_bugs_grey.setVisibility(View.GONE);
                userList.add(4);
            }
        });

        iv_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_water.setVisibility(View.GONE);
                iv_water_grey.setVisibility(View.VISIBLE);
                userList.remove((Integer) 2);
            }
        });
        iv_water_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_water.setVisibility(View.VISIBLE);
                iv_water_grey.setVisibility(View.GONE);
                userList.add(2);

            }
        });

        iv_nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_nutrition.setVisibility(View.GONE);
                iv_nutrition_grey.setVisibility(View.VISIBLE);
                userList.remove((Integer) 3);
            }
        });
        iv_nutrition_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_nutrition.setVisibility(View.VISIBLE);
                iv_nutrition_grey.setVisibility(View.GONE);
                userList.add(3);
            }
        });

        iv_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_sun.setVisibility(View.GONE);
                iv_sun_grey.setVisibility(View.VISIBLE);
                userList.remove((Integer) 1);
            }
        });
        iv_sun_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_sun.setVisibility(View.VISIBLE);
                iv_sun_grey.setVisibility(View.GONE);
                userList.add(1);
            }
        });

        iv_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rain.setVisibility(View.GONE);
                iv_rain_grey.setVisibility(View.VISIBLE);
                userList.remove((Integer) 5);
            }
        });
        iv_rain_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rain.setVisibility(View.VISIBLE);
                iv_rain_grey.setVisibility(View.GONE);
                userList.add(5);
            }
        });

        tv_plant_guard_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setRandomPlant() {
        generateList = new ArrayList<>();
        int random = new Random().nextInt( 12 ) + 1;
        switch (random) {
            case 1:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_much_cloud));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_sunlight));
                iv_bug_real.setBackground(getResources().getDrawable(R.drawable.ic_bugs_real));
                generateList.add(1);
                generateList.add(4);
                break;
            case 2:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_much_cloud));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_sunlight));
                generateList.add(1);
                break;
            case 3:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_much_cloud));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_sunlight));
                iv_rain_water.setBackground(getResources().getDrawable(R.drawable.ic_wet));
                iv_bug_real.setBackground(getResources().getDrawable(R.drawable.ic_bugs_real));
                generateList.add(1);
                generateList.add(4);
                generateList.add(5);
                break;
            case 4:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_much_cloud));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_sunlight));
                iv_rain_water.setBackground(getResources().getDrawable(R.drawable.ic_wet));
                generateList.add(1);
                generateList.add(5);
                break;
            case 5:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_sunlight));
                iv_rain_water.setBackground(getResources().getDrawable(R.drawable.ic_wet));
                iv_bug_real.setBackground(getResources().getDrawable(R.drawable.ic_bugs_real));
                generateList.add(4);
                generateList.add(5);
                break;
            case 6:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_sunlight));
                iv_rain_water.setBackground(getResources().getDrawable(R.drawable.ic_wet));
                generateList.add(5);
                break;
            case 7:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_much_water));
                iv_bug_real.setBackground(getResources().getDrawable(R.drawable.ic_bugs_real));
                generateList.add(2);
                generateList.add(4);
                break;
            case 8:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_much_water));
                generateList.add(2);
                break;
            case 9:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_nutrition_water));
                iv_bug_real.setBackground(getResources().getDrawable(R.drawable.ic_bugs_real));
                generateList.add(3);
                generateList.add(4);
                break;
            case 10:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_no_nutrition_water));
                generateList.add(3);
                break;
            case 11:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_normal));
                iv_bug_real.setBackground(getResources().getDrawable(R.drawable.ic_bugs_real));
                generateList.add(4);
                break;
            case 12:
                iv_cloud.setBackground(getResources().getDrawable(R.drawable.ic_sunlight));
                iv_plant_normal.setBackground(getResources().getDrawable(R.drawable.ic_normal));
                break;
        }
    }

    class ComparatorList implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1>o2 ? 1:-1;//这里返回的值，1升序 -1降序
        }
    }

    public void sortList(List<Integer> list){
        ComparatorList cl = new ComparatorList();
        Collections.sort(list, cl);
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(PlantGuardActivity.this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
