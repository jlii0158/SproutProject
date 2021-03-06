package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sproutproject.utils.ThreadUtils;

public class StartActivity extends AppCompatActivity {

    SharedPreferences preferences, signPreference;
    private ImageView imageView;
    private LinearLayout ll_start_page_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(StartActivity.this,R.color.colorBackground); }

        imageView = findViewById(R.id.imageView);
        ll_start_page_img = findViewById(R.id.ll_start_page_img);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height = point.x * 4 / 5;
        lp.width = point.x * 4 / 5;

        imageView.setLayoutParams(lp);
        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) ll_start_page_img.getLayoutParams();
        lp1.setMargins(0, point.y / 5, 0, 0);

        //在该页面停留2秒
        ThreadUtils.runInThread(new Runnable() {
            @SuppressLint("WorldReadableFiles")
            @Override
            public void run() {
                SystemClock.sleep(2000);

                preferences = getSharedPreferences("count", Context.MODE_PRIVATE);
                int count = preferences.getInt("count", 0);

                signPreference = getSharedPreferences("login", Context.MODE_PRIVATE);
                int signState = signPreference.getInt("loginState", 0);

                if (count == 0) {
                    Intent intent = new Intent(StartActivity.this, OnboardActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    if (signState == 1) {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(StartActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putInt("count", ++count);
                //提交修改
                editor.apply();

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