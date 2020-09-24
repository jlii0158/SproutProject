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

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;
import com.example.sproutproject.R;

import java.util.List;

public class MedalListAdapter extends BaseAdapter {

    Context context;
    private final List<Integer> metalID;
    private final List<String> medalName;
    private final List<String> medalDesc;
    private final List<Integer> medalGrowValue;
    private final List<String> medalImage;
    private final List<String> medalImageGrey;

    public MedalListAdapter(Context context, List<Integer> metalID, List<String> medalName, List<String> medalDesc, List<Integer> medalGrowValue, List<String> medalImage, List<String> medalImageGrey){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.metalID = metalID;
        this.medalName = medalName;
        this.medalDesc = medalDesc;
        this.medalGrowValue = medalGrowValue;
        this.medalImage = medalImage;
        this.medalImageGrey = medalImageGrey;
    }

    @Override
    public int getCount() {
        return metalID.size();
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
            convertView = inflater.inflate(R.layout.single_medal_card, parent, false);
            viewHolder.medalImage = (ImageView) convertView.findViewById(R.id.medal_img);
            viewHolder.medalName = (TextView) convertView.findViewById(R.id.tv_medal_name);
            viewHolder.medalDesc = (TextView) convertView.findViewById(R.id.tv_medal_desc);
            viewHolder.medalState = (TextView) convertView.findViewById(R.id.tv_medal_state);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        Picasso.get().load(medalImageGrey.get(position)).into(viewHolder.medalImage);
        viewHolder.medalImage.setAlpha((float) 0.3);
        viewHolder.medalName.setText(medalName.get(position));
        viewHolder.medalDesc.setText(medalDesc.get(position));
        String unlock = "Locked";
        viewHolder.medalState.setText(unlock);


        return convertView;
    }

    private static class ViewHolder {

        ImageView medalImage;
        TextView medalName;
        TextView medalDesc;
        TextView medalState;

    }
}
