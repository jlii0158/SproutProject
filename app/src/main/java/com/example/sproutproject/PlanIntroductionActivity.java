package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PlanIntroductionActivity extends AppCompatActivity {
    private TextView tv_plan_creation_back_button;
    private CardView cardView_create_plan, cardView_device_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_introduction);

        tv_plan_creation_back_button = findViewById(R.id.tv_plan_creation_back_button);
        cardView_create_plan = findViewById(R.id.cardView_create_plan);
        cardView_device_setting = findViewById(R.id.cardView_device_setting);

        tv_plan_creation_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardView_create_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanIntroductionActivity.this, HowToPlanActivity.class);
                startActivity(intent);
            }
        });

        cardView_device_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanIntroductionActivity.this, HowToDeviceActivity.class);
                startActivity(intent);
            }
        });
    }
}
