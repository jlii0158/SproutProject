package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BigMedalActivity extends AppCompatActivity {
    private TextView tv_big_medal_title, tv_big_medal_name, tv_big_medal_desc;
    private ImageView iv_big_medal_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_medal);

        tv_big_medal_title = findViewById(R.id.tv_big_medal_title);
        iv_big_medal_img = findViewById(R.id.iv_big_medal_img);
        tv_big_medal_name = findViewById(R.id.tv_big_medal_name);
        tv_big_medal_desc = findViewById(R.id.tv_big_medal_desc);


        tv_big_medal_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String medalImage = intent.getStringExtra("medalImage");
        String medalImageGrey = intent.getStringExtra("medalImageGrey");
        String medalName = intent.getStringExtra("medalName");
        String medalDesc = intent.getStringExtra("medalDesc");

        tv_big_medal_name.setText(medalName);
        tv_big_medal_desc.setText(medalDesc);
        Picasso.get()
                .load(medalImageGrey)
                .into(iv_big_medal_img);
        iv_big_medal_img.setAlpha((float) 0.3);

    }
}
