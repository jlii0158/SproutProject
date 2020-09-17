package com.example.sproutproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sproutproject.R;
import com.example.sproutproject.databse.PlanDatabase;
import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.utils.ThreadUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PlanAdapter extends BaseAdapter {
    Context context;
    private final String[] plantImg;
    private final String[] planName;
    private final String[] startDate;
    private final String[] daysToNow;
    private final int[] waterState;
    private final int[] waterDays;
    private final String[] endDate;
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    public PlanAdapter(Context context, String[] plantImg, String[] plantName, String[] startDate, String[] daysToNow, int[] waterState, int[] waterDays, String[] endDate){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.plantImg = plantImg;
        this.planName = plantName;
        this.startDate = startDate;
        this.daysToNow = daysToNow;
        this.waterState = waterState;
        this.waterDays = waterDays;
        this.endDate = endDate;
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


        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_plan_item, parent, false);
            viewHolder.plantImg = (ImageView) convertView.findViewById(R.id.iv_plan_plant_img);
            viewHolder.planName = (TextView) convertView.findViewById(R.id.tv_plan_name);
            viewHolder.startDate = (TextView) convertView.findViewById(R.id.tv_start_date);
            viewHolder.daysToNow = (TextView) convertView.findViewById(R.id.tv_days_to_now);
            viewHolder.daysBackground = (TextView) convertView.findViewById(R.id.tv_days);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        if (waterState[position] == 0) {
            if (Integer.parseInt(daysToNow[position]) % waterDays[position] == 0) {
                viewHolder.daysToNow.setBackgroundColor(Color.parseColor("#FED46E"));
                viewHolder.daysBackground.setBackgroundColor(Color.parseColor("#fcc02d"));
            }
        }


        try {
            Date date = df.parse(getCurrentDate());
            Date date1 = df.parse(endDate[position]);
            if (date == date1) {
                viewHolder.daysToNow.setBackgroundColor(Color.parseColor("#C1C1C1"));
                viewHolder.daysBackground.setBackgroundColor(Color.parseColor("#989898"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
        TextView daysBackground;
    }

    public static String getCurrentDate() {
        long cur_time = System.currentTimeMillis();
        String datetime = df.format(new Date(cur_time));
        return datetime;
    }
}
