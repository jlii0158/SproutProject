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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.AboutUsActivity;
import com.example.sproutproject.FavoriteActivity;
import com.example.sproutproject.R;
import com.example.sproutproject.SigninActivity;
import com.example.sproutproject.UserInformationActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class ProfileFragment extends Fragment {

    private CardView login_card, cardView_favorite, cardView_setting, cardView_about_us;
    //int signState = SigninActivity.stateValue;
    private TextView tv_login_start, tv_account, tv_gift_show, tv_date_show;
    private LinearLayout ll_after_login;
    private Toast toast = null;
    SharedPreferences preferences;

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

        preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

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
        String growValue = preferences.getString("growValue", null);

        if (signState == 1) {
            String welcome = "Welcome back, " + nickname;
            String growString = "Grow Value: " + growValue;
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
                getActivity().finish();
            }
        });

        cardView_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("The settings feature is available in the future version.");
            }
        });

        return view;

    }

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

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
