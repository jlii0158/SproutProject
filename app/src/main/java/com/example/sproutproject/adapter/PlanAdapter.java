package com.example.sproutproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sproutproject.R;
import com.squareup.picasso.Picasso;


public class PlanAdapter extends BaseAdapter {
    Context context;
    private final String[] plantImg;
    private final String[] planName;
    private final String[] startDate;
    private final String[] daysToNow;


    public PlanAdapter(Context context, String[] plantImg, String[] plantName, String[] startDate, String[] daysToNow){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.plantImg = plantImg;
        this.planName = plantName;
        this.startDate = startDate;
        this.daysToNow = daysToNow;
    }

    @Override
    public int getCount() {
        return planName.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_plan_item, parent, false);
            viewHolder.plantImg = (ImageView) convertView.findViewById(R.id.iv_plan_plant_img);
            viewHolder.planName = (TextView) convertView.findViewById(R.id.tv_plan_name);
            viewHolder.startDate = (TextView) convertView.findViewById(R.id.tv_start_date);
            viewHolder.daysToNow = (TextView) convertView.findViewById(R.id.tv_days_to_now);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Picasso.get()
                .load(plantImg[position])
                .into(viewHolder.plantImg);
        viewHolder.planName.setText(planName[position]);
        viewHolder.startDate.setText(startDate[position]);
        viewHolder.daysToNow.setText(daysToNow[position]);

        return convertView;
    }

    private static class ViewHolder {

        ImageView plantImg;
        TextView planName;
        TextView startDate;
        TextView daysToNow;

    }
}
