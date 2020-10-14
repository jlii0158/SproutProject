package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class HowToMedalActivity extends AppCompatActivity {
    private TextView tv_medal2_intro_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_medal);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(HowToMedalActivity.this,R.color.colorBackground); }

        tv_medal2_intro_back_button = findViewById(R.id.tv_medal2_intro_back_button);
        tv_medal2_intro_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
