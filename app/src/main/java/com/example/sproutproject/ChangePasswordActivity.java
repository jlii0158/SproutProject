package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.networkConnection.MD5;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView tv_change_pass_back_button;
    private EditText et_original_password, et_new_password, et_new_password_repeat;
    private Button bt_change_password;
    SharedPreferences preferences;
    String userEmail, prePassword;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(ChangePasswordActivity.this,R.color.colorBackground); }

        tv_change_pass_back_button = findViewById(R.id.tv_change_pass_back_button);
        et_original_password = findViewById(R.id.et_original_password);
        et_new_password = findViewById(R.id.et_new_password);
        bt_change_password = findViewById(R.id.bt_change_password);
        et_new_password_repeat = findViewById(R.id.et_new_password_repeat);



        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        userEmail = preferences.getString("userAccount", null);
        prePassword = preferences.getString("prePassword", null);

        tv_change_pass_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_original_password.getText().toString().equals("")) {
                    showToast("Please enter the original password.");
                    return;
                }
                if (et_new_password.getText().toString().equals("")) {
                    showToast("New password cannot be null.");
                    return;
                }
                if (et_original_password.getText().toString().equals(et_new_password.getText().toString())) {
                    showToast("New password is same as old one, please try again.");
                    return;
                }
                if (!et_new_password_repeat.getText().toString().equals(et_new_password.getText().toString())) {
                    showToast("Both passwords must be the same.");
                    return;
                }
                if(prePassword.equals(MD5.md5(et_original_password.getText().toString()))){
                    ThreadUtils.runInThread(new Runnable() {
                        @Override
                        public void run() {
                            RestClient.updatePassword(userEmail,MD5.md5(et_new_password.getText().toString()));
                        }
                    });
                    preferences.edit()
                            .putString("prePassword", MD5.md5(et_new_password.getText().toString()))
                            .apply();
                    showToast("Password change successful.");
                    //就在这个intent之前写改密码的逻辑
                    /*
                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    intent.putExtra("id", 1);
                    startActivity(intent);

                     */
                    finish();
                }else{
                    showToast("Password is incorrect, please try again.");
                }
            }
        });

    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(ChangePasswordActivity.this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
//            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(colorId);
//        }
    }
}
