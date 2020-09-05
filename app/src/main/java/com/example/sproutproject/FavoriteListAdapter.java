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

public class FavoriteListAdapter extends BaseAdapter {

    Context context;
    private final String[] plantImg;
    private final String[] plantName;

    public FavoriteListAdapter(Context context, String[] plantImg, String[] plantName){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.plantImg = plantImg;
        this.plantName = plantName;
    }

    @Override
    public int getCount() {
        return plantName.length;
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
            convertView = inflater.inflate(R.layout.single_collect_plant_item, parent, false);
            viewHolder.plantImg = (ImageView) convertView.findViewById(R.id.single_plant_img);
            viewHolder.plantName = (TextView) convertView.findViewById(R.id.single_plant_name);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Picasso.get()
                .load(plantImg[position])
                .into(viewHolder.plantImg);
        viewHolder.plantName.setText(plantName[position]);

        return convertView;
    }

    private static class ViewHolder {

        ImageView plantImg;
        TextView plantName;

    }
}
