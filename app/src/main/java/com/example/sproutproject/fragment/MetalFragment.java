package com.example.sproutproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sproutproject.AllMedalsActivity;
import com.example.sproutproject.BigMedalActivity;
import com.example.sproutproject.R;
import com.example.sproutproject.adapter.GetMedalAdapter;
import com.example.sproutproject.entity.GetMedal;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.ThreadUtils;
import com.example.sproutproject.viewmodel.UserMedalViewModel;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class MetalFragment extends Fragment {

    private LinearLayout ll_all_medals;
    static UserMedalViewModel userMedalViewModel;
    private LinearLayout ll_no_medal_view;
    private ListView lv_my_medals;
    RestClient restClient = new RestClient();
    private String[] medalName, medalDesc, medalImg, medalImageGrey;
    private int[] medalID;
    private GetMedalAdapter getMedalAdapter;

    public MetalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metal, container, false);

        ll_all_medals = view.findViewById(R.id.ll_all_medals);
        ll_no_medal_view = view.findViewById(R.id.ll_no_medal_view);
        lv_my_medals = view.findViewById(R.id.lv_my_medals);


        ll_all_medals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllMedalsActivity.class);
                startActivity(intent);
            }
        });

        userMedalViewModel = new ViewModelProvider(this).get(UserMedalViewModel.class);
        userMedalViewModel.initalizeVars(getActivity().getApplication());
        userMedalViewModel.getAllGetMedal().observe(this, new Observer<List<GetMedal>>() {
            @Override
            public void onChanged(@Nullable final List<GetMedal> getMedals) {
                if (getMedals.size() != 0) {
                    lv_my_medals.setVisibility(View.VISIBLE);
                    ll_no_medal_view.setVisibility(View.INVISIBLE);
                } else {
                    lv_my_medals.setVisibility(View.INVISIBLE);
                    ll_no_medal_view.setVisibility(View.VISIBLE);
                }

                medalName = new String[getMedals.size()];
                medalDesc = new String[getMedals.size()];
                medalImg = new String[getMedals.size()];
                medalImageGrey = new String[getMedals.size()];
                medalID = new int[getMedals.size()];

                for (int i = 0; i < getMedals.size(); i++) {
                    medalName[i] = getMedals.get(i).getMedalName();
                    medalDesc[i] = getMedals.get(i).getMedalDesc();
                    medalImg[i] = getMedals.get(i).getMedalImage();
                    medalImageGrey[i] = getMedals.get(i).getMedalImageGrey();
                    medalID[i] = getMedals.get(i).getMedalId();

                }
                getMedalAdapter = new GetMedalAdapter(getActivity(), medalName, medalDesc, medalImg);
                lv_my_medals.setAdapter(getMedalAdapter);

            }
        });

        lv_my_medals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BigMedalActivity.class);
                intent.putExtra("medalImage", medalImg[position])
                        .putExtra("medalImageGrey", medalImageGrey[position])
                        .putExtra("medalName", medalName[position])
                        .putExtra("medalDesc", medalDesc[position])
                        .putExtra("medalID", medalID[position]);
                startActivity(intent);
            }
        });

        return view;
    }
}
