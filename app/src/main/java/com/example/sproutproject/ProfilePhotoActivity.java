package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.adapter.ProfilePhotoRecyclerAdapter;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProfilePhotoActivity extends AppCompatActivity {
    private TextView tv_profile_photo_back_bar, tv_nick_name_profile, tv_account_profile;
    private ImageView iv_profile_photo_display;
    private LinearLayoutManager mManagerColor;
    private RecyclerView rv_profile_photo;
    private List<String> headId, headName, headImg;
    private ProfilePhotoRecyclerAdapter mRecycleViewAdapter;
    private String result;
    private Button bt_change_profile_photo;
    private Toast toast;
    private String head_image, head_id, nickname, userAccount;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(ProfilePhotoActivity.this,R.color.colorBackground); }

        tv_profile_photo_back_bar = findViewById(R.id.tv_profile_photo_back_bar);
        iv_profile_photo_display = findViewById(R.id.iv_profile_photo_display);
        tv_nick_name_profile = findViewById(R.id.tv_nick_name_profile);
        tv_account_profile = findViewById(R.id.tv_account_profile);
        rv_profile_photo = findViewById(R.id.rv_profile_photo);
        bt_change_profile_photo = findViewById(R.id.bt_change_profile_photo);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        nickname = "Hello, " + preferences.getString("userWelcomeName", null);
        userAccount = preferences.getString("userAccount", null);

        tv_nick_name_profile.setText(nickname);
        tv_account_profile.setText(userAccount);

        Picasso.get()
                .load(preferences.getString("profilePhoto",null))
                .into(iv_profile_photo_display);

        mManagerColor = new GridLayoutManager(this, 3);
        rv_profile_photo.setLayoutManager(mManagerColor);

        headId = new ArrayList<String>();
        headName = new ArrayList<String>();
        headImg = new ArrayList<String>();

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                result = RestClient.findAllProfilePhoto();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        headId.add(jsonArray.getJSONObject(i).getString("headId"));
                        headName.add(jsonArray.getJSONObject(i).getString("headName"));
                        headImg.add(jsonArray.getJSONObject(i).getString("headImg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecycleViewAdapter = new ProfilePhotoRecyclerAdapter(getApplicationContext(), R.layout.single_profile_photo_item, headId, headName, headImg);
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        rv_profile_photo.setAdapter(mRecycleViewAdapter);

                        mRecycleViewAdapter.setOnItemClickListener(new ProfilePhotoRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(RecyclerView parent, View view, int position, String data) {
                                bt_change_profile_photo.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(headImg.get(position))
                                        .into(iv_profile_photo_display);
                                head_image = headImg.get(position);
                                head_id = headId.get(position);
                                showToast(headName.get(position));
                            }
                        });
                    }
                });
            }
        });

        bt_change_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit()
                        .putString("profilePhoto", head_image)
                        .apply();

                //这里加一段更新数据库值就行
                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        RestClient.updateHeadId(userAccount, head_id);
                    }
                });

                showToast("Change successful.");
                finish();
            }
        });


        tv_profile_photo_back_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            toast = Toast.makeText(ProfilePhotoActivity.this,msg,Toast.LENGTH_SHORT);
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
