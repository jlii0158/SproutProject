package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sproutproject.adapter.WaterRecordRecyclerAdapter;
import com.example.sproutproject.entity.Water;
import com.example.sproutproject.viewmodel.WaterViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterRecordActivity extends AppCompatActivity {

    private TextView tv_record_back_button;
    private RecyclerView my_recycler_view;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static WaterViewModel waterViewModel;
    private String[] planName, waterDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_record);

        tv_record_back_button = findViewById(R.id.tv_record_back_button);
        my_recycler_view = findViewById(R.id.my_recycler_view);


        tv_record_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        waterViewModel = new ViewModelProvider(this).get(WaterViewModel.class);
        waterViewModel.initalizeVars(getApplication());
        waterViewModel.getAllWaters().observe(this, new Observer<List<Water>>() {
                    @Override
                    public void onChanged(@Nullable final List<Water> waters) {
                        planName = new String[waters.size()];
                        waterDate = new String[waters.size()];
                        for (int i = 0; i < waters.size(); i++) {
                            planName[i] = waters.get(i).getPlanName();
                            waterDate[i] = waters.get(i).getWaterDate();
                        }
                        mLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                        mAdapter = new WaterRecordRecyclerAdapter(planName, waterDate);
                        my_recycler_view.setLayoutManager(mLayoutManager);
                        my_recycler_view.setAdapter(mAdapter);
                    }
                });



    }
}
