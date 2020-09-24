package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sproutproject.databse.UserMedalDatabase;
import com.example.sproutproject.entity.GetMedal;
import com.example.sproutproject.utils.ThreadUtils;
import com.squareup.picasso.Picasso;

public class BigMedalActivity extends AppCompatActivity {
    private TextView tv_big_medal_title, tv_big_medal_name, tv_big_medal_desc;
    private ImageView iv_big_medal_img;
    UserMedalDatabase userMedalDb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_medal);

        tv_big_medal_title = findViewById(R.id.tv_big_medal_title);
        iv_big_medal_img = findViewById(R.id.iv_big_medal_img);
        tv_big_medal_name = findViewById(R.id.tv_big_medal_name);
        tv_big_medal_desc = findViewById(R.id.tv_big_medal_desc);
        userMedalDb = UserMedalDatabase.getInstance(this);


        tv_big_medal_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String medalImage = intent.getStringExtra("medalImage");
        final String medalImageGrey = intent.getStringExtra("medalImageGrey");
        final String medalName = intent.getStringExtra("medalName");
        final String medalDesc = intent.getStringExtra("medalDesc");
        final int medalID = intent.getIntExtra("medalID",0);


        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                final GetMedal existMedal = userMedalDb.userMedalDAO().findById(medalID);
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (existMedal == null) {
                            tv_big_medal_name.setText(medalName);
                            tv_big_medal_desc.setText(medalDesc);
                            Picasso.get()
                                    .load(medalImageGrey)
                                    .into(iv_big_medal_img);
                            iv_big_medal_img.setAlpha((float) 0.3);
                        } else {
                            tv_big_medal_name.setText(medalName);
                            tv_big_medal_desc.setText(medalDesc);
                            Picasso.get()
                                    .load(medalImage)
                                    .into(iv_big_medal_img);
                            iv_big_medal_img.setAlpha((float) 1);
                        }
                    }
                });
            }
        });



        tv_big_medal_name.setText(medalName);
        tv_big_medal_desc.setText(medalDesc);
        Picasso.get()
                .load(medalImageGrey)
                .into(iv_big_medal_img);
        iv_big_medal_img.setAlpha((float) 0.3);

    }
}
