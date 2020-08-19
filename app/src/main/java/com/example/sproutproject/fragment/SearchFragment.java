package com.example.sproutproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sproutproject.CreateActivity;
import com.example.sproutproject.R;


public class SearchFragment extends Fragment {

    private Button bt_search,bt_back,bt_collect, bt_generate;
    private View top_view, bottom_view;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        bt_search = view.findViewById(R.id.button);
        bt_back = view.findViewById(R.id.button4);
        top_view = view.findViewById(R.id.search_view);
        bottom_view = view.findViewById(R.id.plant_detail_view);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_view.setVisibility(View.GONE);
                bottom_view.setVisibility(View.VISIBLE);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_view.setVisibility(View.VISIBLE);
                bottom_view.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
