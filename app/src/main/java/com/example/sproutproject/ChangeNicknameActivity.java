package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.networkConnection.MD5;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;

public class ChangeNicknameActivity extends AppCompatActivity {

    private TextView tv_change_nick_back_button;
    private EditText et_original_nick;
    private Button bt_change_nickname;
    SharedPreferences preferences;
    String userEmail;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);

        tv_change_nick_back_button = findViewById(R.id.tv_change_nick_back_button);
        et_original_nick = findViewById(R.id.et_original_nick);
        bt_change_nickname = findViewById(R.id.bt_change_nickname);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        userEmail = preferences.getString("userAccount", null);


        et_original_nick.setHint(preferences.getString("userWelcomeName", null));

        tv_change_nick_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        bt_change_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_original_nick.getText().toString().equals("")) {
                    showToast("Nickname cannot be null");
                    return;
                }
                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        RestClient.updateUserNickname(userEmail, et_original_nick.getText().toString());
                    }
                });
                preferences.edit()
                        .putString("userWelcomeName", et_original_nick.getText().toString())
                        .apply();
                showToast("Nickname change successful.");
                finish();
            }
        });
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(ChangeNicknameActivity.this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
