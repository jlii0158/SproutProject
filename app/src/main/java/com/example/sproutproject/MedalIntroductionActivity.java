package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MedalIntroductionActivity extends AppCompatActivity {
    private TextView tv_medal_intro_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_introduction);

        tv_medal_intro_back_button = findViewById(R.id.tv_medal_intro_back_button);
        tv_medal_intro_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
