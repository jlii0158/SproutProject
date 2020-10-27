package com.example.sproutproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.example.sproutproject.R;

public class WaterRecordRecyclerAdapter extends RecyclerView.Adapter<WaterRecordRecyclerAdapter.ViewHolder>{

    private String[] mData, mWaterDate;
    private int a;

    public WaterRecordRecyclerAdapter(String[] data, String[] mWaterDate, int a) {
        this.mData = data;
        this.mWaterDate = mWaterDate;
        this.a = a;
    }

    public void updateData(String[] data, String[] dateData) {
        this.mData = data;
        this.mWaterDate = dateData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_record_card, parent, false);
        // 实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.tv_water_plan_name.setText(mData[position]);
        holder.tv_water_plan_date.setText(mWaterDate[position]);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : a;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_water_plan_name;
        TextView tv_water_plan_date;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_water_plan_name = (TextView) itemView.findViewById(R.id.tv_water_plan_name);
            tv_water_plan_date = (TextView) itemView.findViewById(R.id.tv_water_plan_date);
        }
    }
}
