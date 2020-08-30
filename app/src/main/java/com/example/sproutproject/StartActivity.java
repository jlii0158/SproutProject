package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.sproutproject.utils.ThreadUtils;

public class StartActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //在该页面停留3秒
        ThreadUtils.runInThread(new Runnable() {
            @SuppressLint("WorldReadableFiles")
            @Override
            public void run() {
                SystemClock.sleep(3000);
                preferences = getSharedPreferences("count", Context.MODE_PRIVATE);
                int count = preferences.getInt("count", 0);
                if (count == 0) {
                    Intent intent = new Intent(StartActivity.this,OnboardActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(StartActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putInt("count", ++count);
                //提交修改
                editor.apply();

            }
        });
    }
}