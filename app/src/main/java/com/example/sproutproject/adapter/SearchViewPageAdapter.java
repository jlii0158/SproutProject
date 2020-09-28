package com.example.sproutproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sproutproject.AIIntroductionActivity;
import com.example.sproutproject.MainActivity;
import com.example.sproutproject.MedalIntroductionActivity;
import com.example.sproutproject.OnboardingItem;
import com.example.sproutproject.PlanIntroductionActivity;
import com.example.sproutproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchViewPageAdapter extends RecyclerView.Adapter<SearchViewPageAdapter.OnboardingViewHolder> {
    private List<OnboardingItem> onboardingItems;
    private Context context;

    public SearchViewPageAdapter(Context context, List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_banner_item,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnboarding;

        public OnboardingViewHolder(@NonNull final View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.tv_title);
            //textDescription = itemView.findViewById(R.id.tv_sub_title);
            imageOnboarding = itemView.findViewById(R.id.iv_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textTitle.getText().equals("Image recognition technology")) {
                        Intent intent = new Intent(context, AIIntroductionActivity.class);
                        context.startActivity(intent);
                    }

                    if (textTitle.getText().equals("An automatically generated plan")) {
                        Intent intent = new Intent(context, PlanIntroductionActivity.class);
                        context.startActivity(intent);
                    }

                    if (textTitle.getText().equals("Earn medals, make yourself proud")) {
                        Intent intent = new Intent(context, MedalIntroductionActivity.class);
                        context.startActivity(intent);
                    }

                }
            });

        }

        void setOnboardingData(OnboardingItem onboardingItem) {
            textTitle.setText(onboardingItem.getTitle());
            //textDescription.setText(onboardingItem.getDescription());
            //imageOnboarding.setImageResource(onboardingItem.getImage());
            Picasso.get()
                    .load(onboardingItem.getSearchImg())
                    .into(imageOnboarding);
        }
    }
}
