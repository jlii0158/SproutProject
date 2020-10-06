package com.example.sproutproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sproutproject.R;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sproutproject.databse.UserMedalDatabase;
import com.example.sproutproject.entity.GetMedal;
import com.example.sproutproject.utils.ThreadUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MedalRecyclerAdapter extends RecyclerView.Adapter<MedalRecyclerAdapter.MyViewHolder> implements View.OnClickListener{
    Context context;
    private LayoutInflater mLayoutInflater;
    private List<String> medalName, medalImage, medalImageGrey;
    private List<Integer> metalID;
    private int mItemLayout;
    private RecyclerView recyclerView;
    UserMedalDatabase userMedalDb = null;

    public MedalRecyclerAdapter(Context context, int itemLayout, List<Integer> metalID, List<String> medalName, List<String> medalDesc, List<Integer> medalGrowValue, List<String> medalImage, List<String> medalImageGrey) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayout = itemLayout;
        this.medalName = medalName;
        this.medalImage = medalImage;
        this.medalImageGrey = medalImageGrey;
        this.metalID = metalID;
    }

    @Override
    public MedalRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(mItemLayout, parent, false);
        view.setOnClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MedalRecyclerAdapter.MyViewHolder holder, final int position) {

        userMedalDb = UserMedalDatabase.getInstance(context);

        holder.mTextView.setText(medalName.get(position));

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                final GetMedal existMedal = userMedalDb.userMedalDAO().findById(metalID.get(position));
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (existMedal == null) {
                            Picasso.get().load(medalImageGrey.get(position)).into(holder.iv_single_profile_photo);
                            holder.iv_single_profile_photo.setAlpha((float) 0.3);
                        } else {
                            Picasso.get().load(medalImage.get(position)).into(holder.iv_single_profile_photo);
                            holder.iv_single_profile_photo.setAlpha((float) 1);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return medalName.size();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(recyclerView, view, position, medalName.get(position));
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
