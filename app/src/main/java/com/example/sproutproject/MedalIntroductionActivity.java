package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MedalIntroductionActivity extends AppCompatActivity {
    private TextView tv_medal_intro_back_button;
    private CardView cardView_medal_introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_introduction);

        tv_medal_intro_back_button = findViewById(R.id.tv_medal_intro_back_button);
        cardView_medal_introduction = findViewById(R.id.cardView_medal_introduction);

        cardView_medal_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedalIntroductionActivity.this, HowToMedalActivity.class);
                startActivity(intent);
            }
        });

        tv_medal_intro_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
