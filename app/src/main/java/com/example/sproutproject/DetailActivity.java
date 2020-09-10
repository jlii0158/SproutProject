package com.example.sproutproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.database_entity.Plant;
import com.example.sproutproject.databse.PlantDatabase;
import com.example.sproutproject.entity.FavoritePlant;
import com.example.sproutproject.utils.WaterUtils;
import com.example.sproutproject.viewmodel.PlantViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DetailActivity extends AppCompatActivity {

    String imgUrl;
    private TextView tv_plantName,tv_plantNickName, tv_space, tv_cycle, tv_sow, tv_comp, tv_water, tv_back_button;
    private WaterUtils waterUtils;
    private LinearLayout ll_water;
    ImageView iv_detailImage, iv_large, iv_nice_image;
    CardView cv_detail;
    View imgEntryView;
    private Button bt_generate, bt_collect, bt_garbage;
    PlantDatabase db = null;
    private Toast toast = null;
    String store_name, store_img;
    int signState = SigninActivity.stateValue;
    PlantViewModel plantViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        db = PlantDatabase.getInstance(this);

        final AlertDialog dialog = new AlertDialog.Builder(DetailActivity.this).create();

        tv_plantName = findViewById(R.id.textView5);
        tv_plantNickName = findViewById(R.id.textView6);
        tv_space = findViewById(R.id.tv_space);
        tv_cycle = findViewById(R.id.tv_cycle);
        tv_sow = findViewById(R.id.tv_sow);
        tv_comp = findViewById(R.id.tv_comp);
        tv_water = findViewById(R.id.tv_water);
        ll_water = findViewById(R.id.waterNeed_bar);
        iv_detailImage = findViewById(R.id.large_img);
        iv_large = findViewById(R.id.large_image_show);
        iv_nice_image = findViewById(R.id.iv_nice_image);
        cv_detail = findViewById(R.id.large_image);
        imgEntryView = View.inflate(DetailActivity.this,R.layout.dialog_photo,null);
        bt_generate = findViewById(R.id.button6);
        bt_collect = findViewById(R.id.bt_collect);
        bt_garbage = findViewById(R.id.bt_garbage);
        tv_back_button = findViewById(R.id.tv_back_button);

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        plantViewModel.initalizeVars(getApplication());

        Intent intent = getIntent();
        final Plant plant = (Plant) intent.getSerializableExtra("plant");

        /*
        if (signState == 1) {
            bt_generate.setVisibility(View.VISIBLE);
        }
        else  {
            bt_generate.setVisibility(View.GONE);
        }*/



        store_name = plant.getPlant_name();
        imgUrl = plant.getPlant_img();

        showToast(plant.getPlant_name());

        new FindPlantByName().execute();

        //load image
        Picasso.get().load(imgUrl).into(iv_detailImage);
        //set plant name
        tv_plantName.setText(plant.getPlant_name());
        //set plant nickname
        String aaa = plant.getPlant_nickname();
        if (!aaa.equals("null")) {
            String nick = "Alias: " + aaa;
            tv_plantNickName.setText(nick);
        }

        //set plant water need
        String waterTemp = "Water need is " + plant.getWater_need();
        tv_water.setText(waterTemp);
        waterUtils = new WaterUtils();
        int icon_array = R.drawable.ic_water;
        int temp = 0;
        switch (plant.getWater_need()) {
            case "low":
                temp = 1;
                break;
            case "medium":
                temp = 2;
                break;
            case "high":
                temp = 3;
                break;
        }
        waterUtils.createWaterNeed(ll_water,icon_array,temp);
        //set textView
        tv_space.setText(plant.getPlant_space_ins());
        tv_cycle.setText(plant.getPlant_harvest_ins());
        tv_sow.setText(plant.getPlant_sow_ins());
        tv_comp.setText(plant.getComp_plant());
        Picasso.get().load(plant.getPlant_desc()).into(iv_nice_image);

        cv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.show();
                LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
                View imgEntryView = inflater.inflate(R.layout.dialog_photo, null); // 加载自定义的布局文件
                final AlertDialog dialog = new AlertDialog.Builder(DetailActivity.this).create();
                ImageView img = (ImageView)imgEntryView.findViewById(R.id.large_image_show);
                Picasso.get().load(imgUrl).into(img);
                dialog.setView(imgEntryView); // custom dialog
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        imgEntryView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.cancel();
            }
        });

        bt_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, CreateActivity.class);
                intent.putExtra("onePlant",plant);
                startActivity(intent);
            }
        });

        tv_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InsertDatabase().execute();
                bt_collect.setVisibility(View.INVISIBLE);
                bt_garbage.setVisibility(View.VISIBLE);
            }
        });

        bt_garbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new DeleteFromDatabase().execute();
                plantViewModel.delete(store_name);
                bt_garbage.setVisibility(View.INVISIBLE);
                bt_collect.setVisibility(View.VISIBLE);
                showToast("Remove success");
            }
        });

    }

    private class FindPlantByName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            List<FavoritePlant> plants = db.plantDao().findByName(store_name);
            String temp = "";
            if (plants.size() != 0) {
                temp = "exist";
                return temp;
            } else {
                return temp;
            }
        }
        @Override
        protected void onPostExecute(String details) {
            if (details.equals("exist")) {
                bt_garbage.setVisibility(View.VISIBLE);
                bt_collect.setVisibility(View.INVISIBLE);
            }

        }
    }

    private class InsertDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            FavoritePlant favoritePlant = new FavoritePlant(imgUrl, store_name);
            long id = db.plantDao().insert(favoritePlant);
            return "";
        }
        @Override
        protected void onPostExecute(String details) {
            showToast("Collect success");
        }
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(DetailActivity.this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }



}
