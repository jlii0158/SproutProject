package com.example.sproutproject.adapter;

import android.content.Context;
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

public class GetMedalAdapter extends BaseAdapter {
    Context context;
    private final String[] medalName;
    private final String[] medalImage;
    private final String[] medalDesc;


    public GetMedalAdapter(Context context, String[] medalName, String[] medalDesc, String[] medalImage){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.medalName = medalName;
        this.medalDesc = medalDesc;
        this.medalImage = medalImage;
    }

    @Override
    public int getCount() {
        return medalName.length;
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

        Picasso.get().load(medalImage[position]).into(viewHolder.medalImage);
        viewHolder.medalName.setText(medalName[position]);
        viewHolder.medalDesc.setText(medalDesc[position]);
        String unlock = "Unlocked";
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
