package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

                new getAllCredentialAsyncTask().execute();
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
    }

    private class getAllCredentialAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findAllCredential();
        }

        @Override
        protected void onPostExecute(String result) {
            String[] cre_id;

            try {

                int test = 0;
                String newCreId = "";
                JSONArray jsonArray = new JSONArray(result);
                cre_id = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    cre_id[i] = jsonArray.getJSONObject(i).getString("cre_id");
                    if (email.equals(jsonArray.getJSONObject(i).getString("user_name"))) {
                        test = 1;
                    }
                }
                newCreId = cre_id[jsonArray.length() - 1];
                if (test == 1) {
                    Toast.makeText(SigninActivity.this, "The email has been used, please choose another one.", Toast.LENGTH_SHORT).show();
                }else {
                    new PostCredentialAsyncTask().execute(newCreId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class PostCredentialAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            int tempId = Integer.parseInt(strings[0]) + 1;
            String temp = String.valueOf(tempId);
            return RestClient.postCredential(temp, nickname, email, MD5.md5(signup_password));
        }
        @Override
        protected void onPostExecute(Integer result) {
            if (result == 500) {
                Toast.makeText(SigninActivity.this, "Sign Up failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SigninActivity.this, "Sign Up Success!", Toast.LENGTH_SHORT).show();
                //SigninActivity.this.finish();
                //Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                //startActivity(intent);
            }
        }
    }


}
