package com.example.sproutproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sproutproject.AllMedalsActivity;
import com.example.sproutproject.R;

public class MetalFragment extends Fragment {

    private LinearLayout ll_all_medals;

    public MetalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metal, container, false);

        ll_all_medals = view.findViewById(R.id.ll_all_medals);

        ll_all_medals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllMedalsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
