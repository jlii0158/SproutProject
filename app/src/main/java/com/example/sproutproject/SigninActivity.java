package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.networkConnection.MD5;
import com.example.sproutproject.networkConnection.RestClient;

import org.json.JSONArray;
import org.json.JSONException;

public class SigninActivity extends AppCompatActivity {

    private EditText ed_username, ed_password, ed_nickname, ed_email, ed_signup_password;
    private Button bt_signin,bt_signup;
    private TextView tv_signup,tv_signin;
    private RelativeLayout signin_top,signup_bottom;
    String username, password, nickname, email, signup_password;
    //This value is used to indicate the sign in state, 0 means no state, 1 means login.
    public static int stateValue = 0;
    public static String userAccount, userWelcomeName, growValue, userID;
    //private ImageView iv_signin_back_bar;
    private Toast toast = null;
    SharedPreferences preferences, preferencesGrowValue;
    RestClient restClient = new RestClient();
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signin_top = findViewById(R.id.signin_top);
        signup_bottom = findViewById(R.id.signup_bottom);

        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        bt_signin = findViewById(R.id.bt_signin);

        tv_signup = findViewById(R.id.tv_signup);
        tv_signin = findViewById(R.id.tv_signin);

        ed_nickname = findViewById(R.id.ed_nickname);
        ed_email = findViewById(R.id.ed_email);
        ed_signup_password = findViewById(R.id.ed_signup_password);
        bt_signup = findViewById(R.id.bt_signup);

        //iv_signin_back_bar = findViewById(R.id.iv_signin_back_bar);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        preferencesGrowValue = getSharedPreferences("growValueAfterLogout", Context.MODE_PRIVATE);

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = ed_nickname.getText().toString().trim();
                email = ed_email.getText().toString().trim();
                signup_password = ed_signup_password.getText().toString().trim();

                if (nickname.equals("")) {
                    showToast("Please set a name.");
                    return;
                }
                if (email.equals("")) {
                    showToast("Please set the email.");
                    return;
                }
                if (!email.contains("@")||!email.contains(".")) {
                    showToast("The email address is invalid, please try again.");
                    return;
                }
                if (email.lastIndexOf(".") < email.lastIndexOf("@")) {
                    showToast("The email address is invalid, please try again.");
                    return;
                }
                if (signup_password.equals("")) {
                    showToast("Please set the password.");
                    return;
                }

                new getAllAsyncTask().execute();
            }
        });

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed_username.getText().toString().trim();
                password = ed_password.getText().toString().trim();
                if (username.equals("")) {
                    showToast("Please input the email.");
                    return;
                }
                if (!username.contains("@")||!username.contains(".")) {
                    showToast("The email address is invalid, please try again.");
                    return;
                }
                if (username.lastIndexOf(".") < username.lastIndexOf("@")) {
                    showToast("The email address is invalid, please try again.");
                    return;
                }
                if (password.equals("")) {
                    showToast("Please input the password.");
                    return;
                }
                new getAllUserAsyncTask().execute();
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_top.setVisibility(View.GONE);
                signup_bottom.setVisibility(View.VISIBLE);
            }
        });

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_bottom.setVisibility(View.GONE);
                signin_top.setVisibility(View.VISIBLE);
            }
        });

        /*
        iv_signin_back_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
                finish();
            }
        });

         */
    }

    /*
    public void onBackPressed() {
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        intent.putExtra("id",1);
        startActivity(intent);
        finish();
    }

     */

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {
                showToast("Click once again to exit");
                firstTime = secondTime;
                return true;
            } else {
                finish();
            }
        }

        return super.onKeyUp(keyCode, event);
    }


    private class getAllUserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return restClient.findAllUser();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                int test = 0;
                int test1 = 0;
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (username.equals(jsonArray.getJSONObject(i).getString("user_name"))) {
                        userAccount = username;
                        userWelcomeName = jsonArray.getJSONObject(i).getString("user_nick");
                        growValue = jsonArray.getJSONObject(i).getString("user_grow");
                        userID = jsonArray.getJSONObject(i).getString("user_id");
                        test = 1;
                        if (MD5.md5(password).equals(jsonArray.getJSONObject(i).getString("password_hash"))) {
                            test1 = 1;
                        }
                    }
                }
                if (test != 1) {
                    showToast("Account does not exist.");
                }
                if (test == 1 && test1 != 1) {
                    showToast("Password is incorrect.");
                }
                if (test == 1 && test1 == 1) {
                    showToast("Sign in success.");
                    stateValue = 1;
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    //intent.putExtra("id",1);

                    String growValueFromNative = preferencesGrowValue.getString("growValue", null);

                    if (Integer.parseInt(growValueFromNative) > Integer.parseInt(growValue)) {
                        preferences.edit()
                                .putString("userAccount", userAccount)
                                .putString("userWelcomeName", userWelcomeName)
                                .putString("growValue", growValueFromNative)
                                .putInt("loginState", 1) //1 意思是登陆状态，0是没登陆
                                .apply();
                    } else {
                        preferences.edit()
                                .putString("userAccount", userAccount)
                                .putString("userWelcomeName", userWelcomeName)
                                .putString("growValue", growValue)
                                .putInt("loginState", 1) //1 意思是登陆状态，0是没登陆
                                .apply();
                    }


                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class getAllAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return restClient.findAllUser();
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                int test = 0;
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (email.equals(jsonArray.getJSONObject(i).getString("user_name"))) {
                        test = 1;
                    }
                }
                if (test == 1) {
                    showToast("The email has been used, please choose another one.");
                }else {
                    new PostUserAsyncTask().execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class PostUserAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            userAccount = email;
            userWelcomeName = nickname;
            growValue = "0";
            return RestClient.postUser(nickname, email, MD5.md5(signup_password));
        }
        @Override
        protected void onPostExecute(Integer result) {
            if (result == 500) {
                Toast.makeText(SigninActivity.this, "Sign Up failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SigninActivity.this, "Sign Up Success!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);


                String growValueFromNative = preferencesGrowValue.getString("growValue", null);

                if (Integer.parseInt(growValueFromNative) > Integer.parseInt(growValue)) {
                    preferences.edit()
                            .putString("userAccount", userAccount)
                            .putString("userWelcomeName", userWelcomeName)
                            .putString("growValue", growValueFromNative)
                            .putInt("loginState", 1) //1 意思是登陆状态，0是没登陆
                            .apply();
                } else {
                    preferences.edit()
                            .putString("userAccount", userAccount)
                            .putString("userWelcomeName", userWelcomeName)
                            .putString("growValue", growValue)
                            .putInt("loginState", 1) //1 意思是登陆状态，0是没登陆
                            .apply();
                }

                startActivity(intent);
                finish();
                /*
                ed_nickname.setText("");
                ed_email.setText("");
                ed_signup_password.setText("");
                signup_bottom.setVisibility(View.GONE);
                signin_top.setVisibility(View.VISIBLE);
                ed_username.setText(email);
                ed_password.setText(signup_password);

                 */
            }
        }
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
