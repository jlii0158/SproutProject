package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView tv_change_pass_back_button;
    private EditText et_original_password, et_new_password;
    private Button bt_change_password;
    SharedPreferences preferences;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        tv_change_pass_back_button = findViewById(R.id.tv_change_pass_back_button);
        et_original_password = findViewById(R.id.et_original_password);
        et_new_password = findViewById(R.id.et_new_password);
        bt_change_password = findViewById(R.id.bt_change_password);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        userEmail = preferences.getString("userAccount", null);

        tv_change_pass_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                *
                *
                *
                *
                *
                *
                *
                */
                //就在这个intent之前写改密码的逻辑
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                finish();
            }
        });




    }
}
