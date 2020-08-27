package com.example.sproutproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ListAdapter extends BaseAdapter {

    Context context;
    private final List<String> plantName;
    private final List<String> waterNeed;
    private final List<String> plantImg;
    private final List<String> harvestIns;

    public ListAdapter(Context context, List<String> plantName, List<String> harvestIns, List<String> waterNeed, List<String> plantImg){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.plantName = plantName;
        this.waterNeed = waterNeed;
        this.plantImg = plantImg;
        this.harvestIns = harvestIns;
    }

    @Override
    public int getCount() {
        return plantName.size();
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
            convertView = inflater.inflate(R.layout.single_list_item, parent, false);
            viewHolder.plantName = (TextView) convertView.findViewById(R.id.textView7);
            viewHolder.plantImg = (ImageView) convertView.findViewById(R.id.plant_image);
            viewHolder.waterNeed = (TextView) convertView.findViewById(R.id.textView11);
            viewHolder.harvestIns = (TextView) convertView.findViewById(R.id.textView4);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.plantName.setText(plantName.get(position));
        viewHolder.harvestIns.setText(harvestIns.get(position));
        viewHolder.waterNeed.setText(waterNeed.get(position));
        Picasso.get()
                .load(plantImg.get(position))
                .into(viewHolder.plantImg);

        return convertView;
    }

    private static class ViewHolder {

        TextView plantName;
        TextView waterNeed;
        TextView harvestIns;
        ImageView plantImg;

    }

}