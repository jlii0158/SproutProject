package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.sproutproject.adapter.WaterRecordRecyclerAdapter;
import com.example.sproutproject.entity.Water;
import com.example.sproutproject.viewmodel.WaterViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterRecordActivity extends AppCompatActivity {

    private TextView tv_record_back_button;
    private RecyclerView my_recycler_view;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static WaterViewModel waterViewModel;
    private String[] planName, waterDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_record);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(WaterRecordActivity.this,R.color.colorBackground); }

        tv_record_back_button = findViewById(R.id.tv_record_back_button);
        my_recycler_view = findViewById(R.id.my_recycler_view);


        tv_record_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String pName = getIntent().getStringExtra("pName");

        waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        waterViewModel.initalizeVars(getApplication());
        final String finalPName = pName;
        waterViewModel.getAllWaters().observe(this, new Observer<List<Water>>() {
                    @Override
                    public void onChanged(@Nullable final List<Water> waters) {
                        planName = new String[waters.size()];
                        waterDate = new String[waters.size()];
                        int a = 0;
                        if (finalPName != null) {
                            for (int i = 0; i < waters.size(); i++) {
                                if (finalPName.equals(waters.get(i).getPlanName())) {
                                    planName[a] = waters.get(i).getPlanName();
                                    waterDate[a] = waters.get(i).getWaterDate();
                                     a += 1;
                                }
                            }
                        } else {
                            for (int i = 0; i < waters.size(); i++) {
                                planName[i] = waters.get(i).getPlanName();
                                waterDate[i] = waters.get(i).getWaterDate();
                                a += 1;
                            }
                        }

                        mLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                        mAdapter = new WaterRecordRecyclerAdapter(planName, waterDate, a);
                        my_recycler_view.setLayoutManager(mLayoutManager);
                        my_recycler_view.setAdapter(mAdapter);
                    }
                });
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
