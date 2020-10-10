package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TakeCareActivity extends AppCompatActivity {

    private TextView tv_take_care_back_title;

    private CardView cv_infection;
    private ImageView infection_downButton, infection_upButton;
    private LinearLayout ll_infection, ll_infection_title;
    private TextView tv_infection_title;

    private CardView cv_too_much_water;
    private ImageView too_much_water_downButton, too_much_water_upButton;
    private LinearLayout ll_much_water, ll_water_title;
    private TextView tv_water_much_title;

    private CardView cv_too_less_water;
    private ImageView too_less_water_downButton, too_less_water_upButton;
    private LinearLayout ll_less_water, ll_dehy_title;
    private TextView tv_dehy_title;

    private CardView cv_no_nutrition;
    private ImageView no_nutrition_downButton, no_nutrition_upButton;
    private LinearLayout ll_no_nutrition, ll_nutrition_title;
    private TextView tv_nutrition_title;

    private CardView cv_sunlight;
    private ImageView sunlight_downButton, sunlight_upButton;
    private LinearLayout ll_sunlight, ll_sunlight_title;
    private TextView tv_sunlight_title;

    private CardView cv_plant_guard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_care);

        tv_take_care_back_title = findViewById(R.id.tv_take_care_back_title);

        cv_infection = findViewById(R.id.cv_infection);
        infection_downButton = findViewById(R.id.infection_downButton);
        infection_upButton = findViewById(R.id.infection_upButton);
        ll_infection = findViewById(R.id.ll_infection);
        ll_infection_title = findViewById(R.id.ll_infection_title);
        tv_infection_title = findViewById(R.id.tv_infection_title);

        cv_too_much_water = findViewById(R.id.cv_too_much_water);
        too_much_water_downButton = findViewById(R.id.too_much_water_downButton);
        too_much_water_upButton = findViewById(R.id.too_much_water_upButton);
        ll_much_water = findViewById(R.id.ll_much_water);
        ll_water_title = findViewById(R.id.ll_water_title);
        tv_water_much_title = findViewById(R.id.tv_water_much_title);


        cv_too_less_water = findViewById(R.id.cv_too_less_water);
        too_less_water_downButton = findViewById(R.id.too_less_water_downButton);
        too_less_water_upButton = findViewById(R.id.too_less_water_upButton);
        ll_less_water = findViewById(R.id.ll_less_water);
        ll_dehy_title = findViewById(R.id.ll_dehy_title);
        tv_dehy_title = findViewById(R.id.tv_dehy_title);

        cv_no_nutrition = findViewById(R.id.cv_no_nutrition);
        no_nutrition_downButton = findViewById(R.id.no_nutrition_downButton);
        no_nutrition_upButton = findViewById(R.id.no_nutrition_upButton);
        ll_no_nutrition = findViewById(R.id.ll_no_nutrition);
        ll_nutrition_title = findViewById(R.id.ll_nutrition_title);
        tv_nutrition_title = findViewById(R.id.tv_nutrition_title);

        cv_sunlight = findViewById(R.id.cv_sunlight);
        sunlight_downButton = findViewById(R.id.sunlight_downButton);
        sunlight_upButton = findViewById(R.id.sunlight_upButton);
        ll_sunlight = findViewById(R.id.ll_sunlight);
        ll_sunlight_title = findViewById(R.id.ll_sunlight_title);
        tv_sunlight_title = findViewById(R.id.tv_sunlight_title);

        cv_plant_guard = findViewById(R.id.cv_plant_guard);



        cv_plant_guard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeCareActivity.this, PlantGuardActivity.class);
                startActivity(intent);
            }
        });

        tv_take_care_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cv_infection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infection_downButton.getVisibility() == View.VISIBLE) {
                    ll_infection.setVisibility(View.VISIBLE);
                    infection_downButton.setVisibility(View.GONE);
                    infection_upButton.setVisibility(View.VISIBLE);
                    ll_infection_title.setBackgroundColor(Color.parseColor("#A2D89F"));
                    tv_infection_title.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_infection_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                } else {
                    ll_infection.setVisibility(View.GONE);
                    infection_downButton.setVisibility(View.VISIBLE);
                    infection_upButton.setVisibility(View.GONE);
                    ll_infection_title.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    tv_infection_title.setTextColor(Color.parseColor("#808080"));
                    tv_infection_title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        });


        cv_too_much_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (too_much_water_downButton.getVisibility() == View.VISIBLE) {
                    ll_much_water.setVisibility(View.VISIBLE);
                    too_much_water_downButton.setVisibility(View.GONE);
                    too_much_water_upButton.setVisibility(View.VISIBLE);
                    ll_water_title.setBackgroundColor(Color.parseColor("#A2D89F"));
                    tv_water_much_title.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_water_much_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    ll_much_water.setVisibility(View.GONE);
                    too_much_water_downButton.setVisibility(View.VISIBLE);
                    too_much_water_upButton.setVisibility(View.GONE);
                    ll_water_title.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    tv_water_much_title.setTextColor(Color.parseColor("#808080"));
                    tv_water_much_title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        });

        cv_too_less_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (too_less_water_downButton.getVisibility() == View.VISIBLE) {
                    ll_less_water.setVisibility(View.VISIBLE);
                    too_less_water_downButton.setVisibility(View.GONE);
                    too_less_water_upButton.setVisibility(View.VISIBLE);
                    ll_dehy_title.setBackgroundColor(Color.parseColor("#A2D89F"));
                    tv_dehy_title.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_dehy_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    ll_less_water.setVisibility(View.GONE);
                    too_less_water_downButton.setVisibility(View.VISIBLE);
                    too_less_water_upButton.setVisibility(View.GONE);
                    ll_dehy_title.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    tv_dehy_title.setTextColor(Color.parseColor("#808080"));
                    tv_dehy_title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        });

        cv_no_nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (no_nutrition_downButton.getVisibility() == View.VISIBLE) {
                    ll_no_nutrition.setVisibility(View.VISIBLE);
                    no_nutrition_downButton.setVisibility(View.GONE);
                    no_nutrition_upButton.setVisibility(View.VISIBLE);
                    ll_nutrition_title.setBackgroundColor(Color.parseColor("#A2D89F"));
                    tv_nutrition_title.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_nutrition_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    ll_no_nutrition.setVisibility(View.GONE);
                    no_nutrition_downButton.setVisibility(View.VISIBLE);
                    no_nutrition_upButton.setVisibility(View.GONE);
                    ll_nutrition_title.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    tv_nutrition_title.setTextColor(Color.parseColor("#808080"));
                    tv_nutrition_title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        });

        cv_sunlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunlight_downButton.getVisibility() == View.VISIBLE) {
                    ll_sunlight.setVisibility(View.VISIBLE);
                    sunlight_downButton.setVisibility(View.GONE);
                    sunlight_upButton.setVisibility(View.VISIBLE);
                    ll_sunlight_title.setBackgroundColor(Color.parseColor("#A2D89F"));
                    tv_sunlight_title.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_sunlight_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    ll_sunlight.setVisibility(View.GONE);
                    sunlight_downButton.setVisibility(View.VISIBLE);
                    sunlight_upButton.setVisibility(View.GONE);
                    ll_sunlight_title.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    tv_sunlight_title.setTextColor(Color.parseColor("#808080"));
                    tv_sunlight_title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        });
    }
}
