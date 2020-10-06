package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HowToAiActivity extends AppCompatActivity {
    private TextView tv_take_photo_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_ai);

        tv_take_photo_back_button = findViewById(R.id.tv_take_photo_back_button);

        tv_take_photo_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
