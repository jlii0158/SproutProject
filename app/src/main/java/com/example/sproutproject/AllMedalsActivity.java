package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sproutproject.adapter.ListAdapter;
import com.example.sproutproject.adapter.MedalListAdapter;
import com.example.sproutproject.networkConnection.RestClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AllMedalsActivity extends AppCompatActivity {

    private TextView tv_all_medal_title;
    RestClient restClient = new RestClient();
    private List<String> medalName, medalDesc, medalImage, medalImageGrey;
    private List<Integer> medalGrowValue, metalID;
    private MedalListAdapter lAdapter;
    private ListView lv_all_medals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medals);

        tv_all_medal_title = findViewById(R.id.tv_all_medal_title);
        lv_all_medals = findViewById(R.id.lv_all_medals);

        tv_all_medal_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new SearchMedalFromDatabase().execute();

        lv_all_medals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllMedalsActivity.this, BigMedalActivity.class);
                intent.putExtra("medalImage", medalImage.get(position))
                        .putExtra("medalImageGrey", medalImageGrey.get(position))
                        .putExtra("medalName", medalName.get(position))
                        .putExtra("medalDesc", medalDesc.get(position))
                        .putExtra("medalID", metalID.get(position));
                startActivity(intent);
            }
        });

    }


    private class SearchMedalFromDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            //if (plantName == null) {
            String result = restClient.findAllMedals();
            try {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() == 0) {
                    return null;
                }
                metalID = new ArrayList<Integer>();
                medalName = new ArrayList<String>();
                medalDesc = new ArrayList<String>();
                medalGrowValue = new ArrayList<Integer>();
                medalImage = new ArrayList<String>();
                medalImageGrey = new ArrayList<String>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    metalID.add(jsonArray.getJSONObject(i).getInt("metal_id"));
                    medalName.add(jsonArray.getJSONObject(i).getString("medel_name"));
                    medalDesc.add(jsonArray.getJSONObject(i).getString("medal_desc"));
                    medalGrowValue.add(jsonArray.getJSONObject(i).getInt("grow_vale"));
                    medalImage.add(jsonArray.getJSONObject(i).getString("medal_image"));
                    medalImageGrey.add(jsonArray.getJSONObject(i).getString("medal_image_grey"));
                }
                lAdapter = new MedalListAdapter(AllMedalsActivity.this, metalID, medalName, medalDesc, medalGrowValue, medalImage, medalImageGrey);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            //}
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            lv_all_medals.setAdapter(lAdapter);

        }
    }
}
