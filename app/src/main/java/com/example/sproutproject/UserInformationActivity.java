package com.example.sproutproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserInformationActivity extends AppCompatActivity {

    private TextView tv_account, tv_nickname_profile;
    private Button bt_logout;

    private TextView tv_profile_back_bar;
    SharedPreferences preferences, preferencesGrowValue;
    private ImageView iv_profile_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        //这个preference是在这个class最先建的
        preferencesGrowValue = getSharedPreferences("growValueAfterLogout", Context.MODE_PRIVATE);

        tv_account = findViewById(R.id.tv_account);
        tv_nickname_profile = findViewById(R.id.tv_nickname_profile);
        bt_logout = findViewById(R.id.bt_logout);
        tv_profile_back_bar = findViewById(R.id.tv_profile_back_bar);
        iv_profile_photo = findViewById(R.id.iv_profile_photo);

        String nickname = preferences.getString("userWelcomeName", null);
        final String userAccount = preferences.getString("userAccount", null);
        final String growValue = preferences.getString("growValue", null);

        tv_account.setText(userAccount);
        tv_nickname_profile.setText(nickname);


        Picasso.get()
                .load(preferences.getString("profilePhoto",null))
                .into(iv_profile_photo);

        iv_profile_photo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //dialog.show();
                LayoutInflater inflater = LayoutInflater.from(UserInformationActivity.this);
                View imgEntryView = inflater.inflate(R.layout.dialog_photo, null); // 加载自定义的布局文件
                final AlertDialog dialog = new AlertDialog.Builder(UserInformationActivity.this).create();
                ImageView img = (ImageView)imgEntryView.findViewById(R.id.large_image_show);
                Picasso.get().load(preferences.getString("profilePhoto",null)).into(img);
                dialog.setView(imgEntryView); // custom dialog
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        tv_profile_back_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserInformationActivity.this, MainActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
                finish();
            }
        });

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        RestClient.updateGrowValue(
                                preferences.getString("growValue", null),
                                preferences.getString("userAccount", null));
                    }
                });
                preferencesGrowValue.edit()
                        .putString("growValue", preferences.getString("growValue", null))
                        .apply();

                SigninActivity.stateValue = 0;

                preferences.edit()
                        .putInt("loginState", 0) //1 意思是登陆状态，0是没登陆
                        .apply();

                Intent intent = new Intent(UserInformationActivity.this, SigninActivity.class);
                //intent.putExtra("id",1);
                startActivity(intent);
                Toast.makeText(UserInformationActivity.this, "log out success.", Toast.LENGTH_SHORT).show();
                finish();


            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(UserInformationActivity.this, MainActivity.class);
        intent.putExtra("id",1);
        startActivity(intent);
        finish();
    }
}
