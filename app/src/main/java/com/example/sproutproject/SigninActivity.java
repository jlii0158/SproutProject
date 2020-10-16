package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.networkConnection.MD5;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;

public class SigninActivity extends AppCompatActivity {

    private EditText ed_username, ed_password, ed_nickname, ed_email, ed_signup_password, ed_signup_password_confirm;
    private Button bt_signin,bt_signup;
    private TextView tv_signup,tv_signin;
    private ScrollView signin_top,signup_bottom;
    String username, password, nickname, email, signup_password, res;
    //This value is used to indicate the sign in state, 0 means no state, 1 means login.
    public static int stateValue = 0;
    public static String userAccount, userWelcomeName, growValue, userID, prePassword, profilePhoto;
    //private ImageView iv_signin_back_bar;
    private Toast toast = null;
    SharedPreferences preferences, preferencesGrowValue;
    RestClient restClient = new RestClient();
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(SigninActivity.this,R.color.colorBackground); }

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
        ed_signup_password_confirm = findViewById(R.id.ed_signup_password_confirm);
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
                    showToast("Please set an account.");
                    return;
                }
                if (!isNumeric(email)) {
                    showToast("The account number should be 6-8 digits");
                    return;
                }
                if (email.length() > 8 || email.length() < 6) {
                    showToast("The account number should be 6-8 digits");
                    return;
                }
                if (signup_password.equals("")) {
                    showToast("Please set the password.");
                    return;
                }
                if (!ed_signup_password_confirm.getText().toString().equals(ed_signup_password.getText().toString())) {
                    showToast("Both passwords must be the same.");
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
                    showToast("Please input the account.");
                    return;
                }
                if (!isNumeric(username)) {
                    showToast("The account number should be 6-8 digits");
                    return;
                }
                if (username.length() > 8 || username.length() < 6) {
                    showToast("The account number should be 6-8 digits");
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
                        prePassword = jsonArray.getJSONObject(i).getString("password_hash");
                        profilePhoto = jsonArray.getJSONObject(i).getString("head_id");
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
                    if (growValueFromNative == null) {
                        growValueFromNative = "0";
                    }

                    if (Integer.parseInt(growValueFromNative) > Integer.parseInt(growValue)) {
                        preferences.edit()
                                .putString("userAccount", userAccount)
                                .putString("userWelcomeName", userWelcomeName)
                                .putString("growValue", growValueFromNative)
                                .putString("prePassword", prePassword)
                                .putInt("loginState", 1) //1 意思是登陆状态，0是没登陆
                                //.putInt("dailyGrow", 0)
                                .apply();
                    } else {
                        preferences.edit()
                                .putString("userAccount", userAccount)
                                .putString("userWelcomeName", userWelcomeName)
                                .putString("growValue", growValue)
                                .putString("prePassword", prePassword)
                                .putInt("loginState", 1) //1 意思是登陆状态，0是没登陆
                                //.putInt("dailyGrow", 0)
                                .apply();
                    }

                    ThreadUtils.runInThread(new Runnable() {
                        @Override
                        public void run() {
                            res = RestClient.findAllProfilePhoto();
                            try {
                                JSONArray jsonArray = new JSONArray(res);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (profilePhoto.equals(jsonArray.getJSONObject(i).getString("headId"))) {
                                        preferences.edit()
                                                .putString("profilePhoto", jsonArray.getJSONObject(i).getString("headImg"))
                                                .apply();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

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
                    showToast("The account has been used, please choose another one.");
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


                /*
                String growValueFromNative = preferencesGrowValue.getString("growValue", null);
                if (growValueFromNative == null) {
                    growValueFromNative = "20";
                }

                 */

                preferences.edit()
                        .putString("userAccount", userAccount)
                        .putString("userWelcomeName", userWelcomeName)
                        .putString("growValue", growValue)
                        .putString("prePassword", MD5.md5(signup_password))
                        .putString("profilePhoto", "https://iconfont.alicdn.com/t/73350a49-9f0c-4387-b396-7ba6e26f60bc.png")
                        .putInt("loginState", 1) //1 意思是登陆状态，0是没登陆
                        //.putInt("dailyGrow", 0)
                        .apply();

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

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
