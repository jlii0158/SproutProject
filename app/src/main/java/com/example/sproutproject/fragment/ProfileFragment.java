package com.example.sproutproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.AboutUsActivity;
import com.example.sproutproject.FavoriteActivity;
import com.example.sproutproject.R;
import com.example.sproutproject.SettingActivity;
import com.example.sproutproject.SigninActivity;
import com.example.sproutproject.UserInformationActivity;
import com.example.sproutproject.WaterRecordActivity;
import com.example.sproutproject.databse.UserMedalDatabase;
import com.example.sproutproject.entity.GetMedal;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;
import com.kelin.banner.view.BannerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class ProfileFragment extends Fragment {

    private CardView login_card, cardView_favorite, cardView_setting, cardView_about_us, cardView_record;
    //int signState = SigninActivity.stateValue;
    private TextView tv_login_start, tv_account, tv_gift_show, tv_date_show;
    private LinearLayout ll_after_login;
    private Toast toast = null;
    SharedPreferences preferences, preferencesGrowValue;
    RestClient restClient = new RestClient();
    UserMedalDatabase userMedalDb = null;
    private ImageView iv_head;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        login_card = view.findViewById(R.id.login_card);
        cardView_favorite = view.findViewById(R.id.cardView_favorite);
        tv_login_start = view.findViewById(R.id.tv_login_start);
        tv_account = view.findViewById(R.id.tv_account);
        ll_after_login = view.findViewById(R.id.ll_after_login);
        tv_gift_show = view.findViewById(R.id.tv_gift_show);
        tv_date_show = view.findViewById(R.id.tv_date_show);
        cardView_setting = view.findViewById(R.id.cardView_setting);
        cardView_about_us = view.findViewById(R.id.cardView_about_us);
        cardView_record = view.findViewById(R.id.cardView_record);
        userMedalDb = UserMedalDatabase.getInstance(getContext());
        iv_head = view.findViewById(R.id.iv_head);



        preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        preferencesGrowValue = getActivity().getSharedPreferences("growValueAfterLogout", Context.MODE_PRIVATE);

        Picasso.get()
                .load(preferences.getString("profilePhoto",null))
                .into(iv_head);

        cardView_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WaterRecordActivity.class);
                startActivity(intent);
            }
        });

        cardView_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        int signState = preferences.getInt("loginState", 0);
        String nickname = preferences.getString("userWelcomeName", null);
        String userAccount = preferences.getString("userAccount", null);
        //这个pre是从登录界面传过来的, 每次登录都会去数据库读一边growValue值
        String growValue = preferences.getString("growValue", null);
        final int growValueFinal = Integer.parseInt(growValue);


        //判断现有成长值是否可以激活图标
        //如果可以激活，说明累计的成长值已经足够激活一个奖章
        //那就把这个奖章的ID和用户的account，也就是email存到room中
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                String result = restClient.findAllMedals();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (growValueFinal >= jsonArray.getJSONObject(i).getInt("grow_vale")){
                            GetMedal existMedal = userMedalDb.userMedalDAO().findById(jsonArray.getJSONObject(i).getInt("metal_id"));
                            if (existMedal == null) {
                                int metalID = jsonArray.getJSONObject(i).getInt("metal_id");
                                String userAccount = preferencesGrowValue.getString("userAccount",null);
                                String medalName = jsonArray.getJSONObject(i).getString("medel_name");
                                String medalDesc = jsonArray.getJSONObject(i).getString("medal_desc");
                                int growValue = jsonArray.getJSONObject(i).getInt("grow_vale");
                                String medalImage = jsonArray.getJSONObject(i).getString("medal_image");
                                String medalImageGrey = jsonArray.getJSONObject(i).getString("medal_image_grey");
                                GetMedal getMedal = new GetMedal(metalID, userAccount, medalName, medalDesc, growValue, medalImage, medalImageGrey);
                                userMedalDb.userMedalDAO().insert(getMedal);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        if (signState == 1) {
            String welcome = "Welcome back, " + nickname;
            String growString = "Growth Value: " + growValue;
            String currentDate = getCurrentDate();
            tv_login_start.setText(welcome);
            tv_account.setText(userAccount);

            tv_gift_show.setText(growString);
            tv_date_show.setText(currentDate);
            ll_after_login.setVisibility(View.VISIBLE);

            login_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UserInformationActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

        } else {
            String original = "Login";
            tv_login_start.setText(original);
            tv_account.setText("");
            ll_after_login.setVisibility(View.GONE);
            login_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SigninActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        cardView_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        cardView_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        return view;

    }

    private static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public static String getCurrentDate() {
        long cur_time = System.currentTimeMillis();
        String datetime = df.format(new Date(cur_time));
        return datetime;
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
