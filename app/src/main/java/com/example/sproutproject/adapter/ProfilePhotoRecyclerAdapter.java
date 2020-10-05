package com.example.sproutproject.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sproutproject.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfilePhotoRecyclerAdapter extends RecyclerView.Adapter<ProfilePhotoRecyclerAdapter.MyViewHolder> implements View.OnClickListener{


    private LayoutInflater mLayoutInflater;
    private List<String> mDataList, headName, headImg;
    private int mItemLayout;
    private RecyclerView recyclerView;

    public ProfilePhotoRecyclerAdapter(Context context, int itemLayout, List<String> headId, List<String> headName, List<String> headImg) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayout = itemLayout;
        mDataList = headId;
        this.headName = headName;
        this.headImg = headImg;
    }

    @Override
    public ProfilePhotoRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(mItemLayout, parent, false);
        view.setOnClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ProfilePhotoRecyclerAdapter.MyViewHolder holder, int position) {
        holder.mTextView.setText(headName.get(position));
        Picasso.get()
                .load(headImg.get(position))
                .into(holder.iv_single_profile_photo);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(recyclerView, view, position, headImg.get(position));
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private ImageView iv_single_profile_photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_single_profile);
            iv_single_profile_photo = (ImageView) itemView.findViewById(R.id.iv_single_profile_photo);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position, String data);
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public  void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

}
