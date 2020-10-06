package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AIIntroductionActivity extends AppCompatActivity {

    private TextView tv_ai_back_button;
    private CardView cardView_ai_introduction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_i_introduction);

        tv_ai_back_button = findViewById(R.id.tv_ai_back_button);
        cardView_ai_introduction = findViewById(R.id.cardView_ai_introduction);

        cardView_ai_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AIIntroductionActivity.this, HowToAiActivity.class);
                startActivity(intent);
            }
        });

        tv_ai_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
