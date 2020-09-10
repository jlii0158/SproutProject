package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sproutproject.adapter.FavoriteListAdapter;
import com.example.sproutproject.database_entity.Plant;
import com.example.sproutproject.entity.FavoritePlant;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.viewmodel.PlantViewModel;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    static PlantViewModel plantViewModel;
    List<FavoritePlant> list;
    FavoriteListAdapter favoriteListAdapter;
    ListView my_listView;
    String[] plantImg, plantName;
    RestClient restClient = new RestClient();
    LinearLayout ll_noFavorite_view;
    Button bt_go_to_addFavorite;
    private TextView tv_favorite_back_button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        my_listView = findViewById(R.id.my_listView);
        ll_noFavorite_view = findViewById(R.id.ll_noFavorite_view);
        bt_go_to_addFavorite = findViewById(R.id.bt_go_to_addFavorite);
        tv_favorite_back_button = findViewById(R.id.tv_favorite_back_button);

        bt_go_to_addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                intent.putExtra("favorite", 1);
                startActivity(intent);
                finish();
            }
        });

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        plantViewModel.initalizeVars(getApplication());
        plantViewModel.getAllPlants().observe(this, new Observer<List<FavoritePlant>>() {
            @Override
            public void onChanged(@Nullable final List<FavoritePlant> plants) {
                plantImg = new String[plants.size()];
                plantName = new String[plants.size()];

                if (plants.size() != 0) {
                    my_listView.setVisibility(View.VISIBLE);
                    ll_noFavorite_view.setVisibility(View.INVISIBLE);
                } else {
                    my_listView.setVisibility(View.INVISIBLE);
                    ll_noFavorite_view.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < plants.size(); i++) {
                    plantImg[i] = plants.get(i).getPlantImg();
                    plantName[i] = plants.get(i).getPlantName();
                }
                favoriteListAdapter = new FavoriteListAdapter(getBaseContext(), plantImg, plantName);
                my_listView.setAdapter(favoriteListAdapter);
            }
        });


        my_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new SearchPlantByName().execute(plantName[position]);
            }
        });


        tv_favorite_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
        intent.putExtra("id", 1);
        startActivity(intent);
        finish();
    }

    private class SearchPlantByName extends AsyncTask<String, Void, Plant> {
        @Override
        protected Plant doInBackground(String... params) {
            //if (plantName == null) {
            String result = restClient.findPlantByName(params[0]);
            Plant plant = new Plant();
            try {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() == 0) {
                    return null;
                }
                plant.setPlant_name(jsonArray.getJSONObject(0).getString("plantName"));
                plant.setPlant_nickname(jsonArray.getJSONObject(0).getString("plantNick"));
                plant.setPlant_sow_ins(jsonArray.getJSONObject(0).getString("plantSow"));
                plant.setPlant_space_ins(jsonArray.getJSONObject(0).getString("plantSpace"));
                plant.setPlant_harvest_ins(jsonArray.getJSONObject(0).getString("harvestIns"));
                plant.setComp_plant(jsonArray.getJSONObject(0).getString("compPlants"));
                plant.setWater_need(jsonArray.getJSONObject(0).getString("waterNeed"));
                plant.setPlant_desc(jsonArray.getJSONObject(0).getString("plantDesc"));
                plant.setPlant_img(jsonArray.getJSONObject(0).getString("plantImg"));
                plant.setGrowth_cycle(jsonArray.getJSONObject(0).getString("growthCycle"));

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            //}
            return plant;
        }
        @Override
        protected void onPostExecute(Plant result) {
            Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
            intent.putExtra("plant",result);
            startActivity(intent);

        }

    }

}
