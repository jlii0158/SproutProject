package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserInformationActivity extends AppCompatActivity {

    private TextView tv_account, tv_nickname_profile;
    private Button bt_logout;
    String sproutAccount = SigninActivity.userAccount;
    String nickName = SigninActivity.userWelcomeName;
    private ImageView iv_profile_back_bar;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        tv_account = findViewById(R.id.tv_account);
        tv_nickname_profile = findViewById(R.id.tv_nickname_profile);
        bt_logout = findViewById(R.id.bt_logout);
        iv_profile_back_bar = findViewById(R.id.iv_profile_back_bar);

        tv_account.setText(sproutAccount);
        tv_nickname_profile.setText(nickName);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        iv_profile_back_bar.setOnClickListener(new View.OnClickListener() {
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
