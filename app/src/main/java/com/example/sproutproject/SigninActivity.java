package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private ImageView iv_signin_back_bar;

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

        iv_signin_back_bar = findViewById(R.id.iv_signin_back_bar);


        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = ed_nickname.getText().toString().trim();
                email = ed_email.getText().toString().trim();
                signup_password = ed_signup_password.getText().toString().trim();

                if (nickname.equals("")) {
                    Toast.makeText(SigninActivity.this, "Please set a name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.equals("")) {
                    Toast.makeText(SigninActivity.this, "Please set the email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.contains("@")||!email.contains(".")) {
                    Toast.makeText(SigninActivity.this, "The email address is invalid, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.lastIndexOf(".") < email.lastIndexOf("@")) {
                    Toast.makeText(SigninActivity.this, "The email address is invalid, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (signup_password.equals("")) {
                    Toast.makeText(SigninActivity.this, "Please set the password.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SigninActivity.this, "Please input the email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!username.contains("@")||!username.contains(".")) {
                    Toast.makeText(SigninActivity.this, "The email address is invalid, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.lastIndexOf(".") < username.lastIndexOf("@")) {
                    Toast.makeText(SigninActivity.this, "The email address is invalid, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(SigninActivity.this, "Please input the password.", Toast.LENGTH_SHORT).show();
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

        iv_signin_back_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        intent.putExtra("id",1);
        startActivity(intent);
        finish();
    }


    private class getAllUserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findAllUser();
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
                    Toast.makeText(SigninActivity.this, "Account does not exist.", Toast.LENGTH_SHORT).show();
                }
                if (test == 1 && test1 != 1) {
                    Toast.makeText(SigninActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                }
                if (test == 1 && test1 == 1) {
                    Toast.makeText(SigninActivity.this, "Sign in success.", Toast.LENGTH_SHORT).show();
                    stateValue = 1;
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    intent.putExtra("id",1);
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
            return RestClient.findAllUser();
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
                    Toast.makeText(SigninActivity.this, "The email has been used, please choose another one.", Toast.LENGTH_SHORT).show();
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
            return RestClient.postUser(nickname, email, MD5.md5(signup_password));
        }
        @Override
        protected void onPostExecute(Integer result) {
            if (result == 500) {
                Toast.makeText(SigninActivity.this, "Sign Up failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SigninActivity.this, "Sign Up Success!", Toast.LENGTH_SHORT).show();
                ed_nickname.setText("");
                ed_email.setText("");
                ed_signup_password.setText("");
                signup_bottom.setVisibility(View.GONE);
                signin_top.setVisibility(View.VISIBLE);
                ed_username.setText(email);
                ed_password.setText(signup_password);
            }
        }
    }


}
