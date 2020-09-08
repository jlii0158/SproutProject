package com.example.sproutproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.sproutproject.PlanDetailActivity;
import com.example.sproutproject.R;
import com.example.sproutproject.adapter.PlanAdapter;
import com.example.sproutproject.database_entity.PlanDisplay;
import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.viewmodel.PlanViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PlanFragment extends Fragment {

    private Button bt_addPlan;
    private ListView lv_plan;
    static PlanViewModel planViewModel;
    private PlanAdapter planAdapter;
    String[] plantImg, planName, startDate, daysToNow, endDate, plantName, startDate1;
    int[] planId, waterCount, waterState, realWaterCount;

    public PlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        lv_plan = view.findViewById(R.id.lv_plan);



        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        planViewModel.initalizeVars(getActivity().getApplication());
        planViewModel.getAllPlans().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(@Nullable final List<Plan> plans) {
                plantImg = new String[plans.size()];
                planName = new String[plans.size()];
                startDate = new String[plans.size()];
                startDate1 = new String[plans.size()];
                daysToNow = new String[plans.size()];

                endDate = new String[plans.size()];
                plantName = new String[plans.size()];
                planId = new int[plans.size()];
                waterCount = new int[plans.size()];
                waterState = new int[plans.size()];
                realWaterCount = new int[plans.size()];
                for (int i = 0; i < plans.size(); i++) {
                    plantImg[i] = plans.get(i).getPlantImg();
                    planName[i] = plans.get(i).getPlanName();

                    endDate[i] = plans.get(i).getEndDate();
                    plantName[i] = plans.get(i).getPlanPlantName();
                    planId[i] = plans.get(i).getPlanId();
                    waterCount[i] = plans.get(i).getWaterCount();
                    waterState[i] = plans.get(i).getWaterState();
                    realWaterCount[i] = plans.get(i).getRealWaterCount();

                    int days = 0;
                    try {
                        Date date = df.parse(plans.get(i).getStartDate());
                        Date date2 = df.parse(getCurrentDate());
                        days = (int) ((date2.getTime() - date.getTime()) / (1000*3600*24));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    daysToNow[i] = String.valueOf(days);
                    String startString = "Starting On: " + plans.get(i).getStartDate();
                    startDate1[i] = plans.get(i).getStartDate();
                    startDate[i] = startString;

                }
                planAdapter = new PlanAdapter(getActivity(), plantImg, planName, startDate, daysToNow);
                lv_plan.setAdapter(planAdapter);
            }
        });
        //planViewModel.deleteAll();

        lv_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlanDisplay planDisplay = new PlanDisplay();
                planDisplay.setPlanId(planId[position]);
                planDisplay.setPlanName(planName[position]);
                planDisplay.setStartDate(startDate1[position]);
                planDisplay.setEndDate(endDate[position]);
                planDisplay.setWaterCount(waterCount[position]);
                planDisplay.setRealWaterCount(realWaterCount[position]);
                planDisplay.setWaterState(waterState[position]);
                planDisplay.setPlant_name(plantName[position]);
                planDisplay.setPlant_img(plantImg[position]);
                planDisplay.setDaysToCurrentDate(daysToNow[position]);

                Intent intent = new Intent(getActivity(), PlanDetailActivity.class);
                intent.putExtra("planDisplay", planDisplay);
                startActivity(intent);
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
}
