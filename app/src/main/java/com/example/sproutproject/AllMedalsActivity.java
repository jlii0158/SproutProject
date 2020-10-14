package com.example.sproutproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.adapter.ListAdapter;
import com.example.sproutproject.adapter.MedalListAdapter;
import com.example.sproutproject.adapter.MedalRecyclerAdapter;
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
    private MedalRecyclerAdapter mRecycleViewAdapter;
    private RecyclerView rv_all_medals;
    private LinearLayoutManager mManagerColor;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medals);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor(AllMedalsActivity.this,R.color.colorBackground); }

        tv_all_medal_title = findViewById(R.id.tv_all_medal_title);
        lv_all_medals = findViewById(R.id.lv_all_medals);
        rv_all_medals = findViewById(R.id.rv_all_medals);

        mManagerColor = new GridLayoutManager(this, 3);
        rv_all_medals.setLayoutManager(mManagerColor);

        tv_all_medal_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new SearchMedalFromDatabase().execute();

        /*
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

         */

    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(AllMedalsActivity.this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
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
                //lAdapter = new MedalListAdapter(AllMedalsActivity.this, metalID, medalName, medalDesc, medalGrowValue, medalImage, medalImageGrey);
                mRecycleViewAdapter = new MedalRecyclerAdapter(getApplicationContext(), R.layout.single_medal_photo_item, metalID, medalName, medalDesc, medalGrowValue, medalImage, medalImageGrey);

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            //}
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            //lv_all_medals.setAdapter(lAdapter);
            rv_all_medals.setAdapter(mRecycleViewAdapter);
            mRecycleViewAdapter.setOnItemClickListener(new MedalRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, String data) {
                    Intent intent = new Intent(AllMedalsActivity.this, BigMedalActivity.class);
                    intent.putExtra("medalImage", medalImage.get(position))
                            .putExtra("medalImageGrey", medalImageGrey.get(position))
                            .putExtra("medalName", medalName.get(position))
                            .putExtra("medalDesc", medalDesc.get(position))
                            .putExtra("medalID", metalID.get(position));
                    showToast(medalName.get(position));
                    startActivity(intent);
                }
            });

        }
    }

    public static void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
//            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(colorId);
//        }
    }
}
