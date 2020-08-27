package com.example.sproutproject.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.CreateActivity;
import com.example.sproutproject.ListAdapter;
import com.example.sproutproject.R;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.utils.WaterUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private Button bt_back,bt_collect, bt_generate;
    private View top_view, bottom_view;
    String plant_name;
    private EditText et_plant;
    private ListView lv_plantList;
    String[] plantNick, plantSow, plantSpace, growthCycle, compPlants, plantDesc;
    private List<String> plantName = new ArrayList<String>();
    private List<String> plantName2 = new ArrayList<String>();
    private List<String> waterNeed = new ArrayList<String>();
    private List<String> waterNeed2 = new ArrayList<String>();
    private List<String> plantImg = new ArrayList<String>();
    private List<String> plantImg2 = new ArrayList<String>();
    private List<String> harvestIns = new ArrayList<String>();
    private List<String> harvestIns2 = new ArrayList<String>();
    ListAdapter lAdapter;
    RestClient restClient = new RestClient();
    ImageView iv_detailImage;
    private WaterUtils waterUtils;
    private LinearLayout ll_water;
    private TextView tv_plantName,tv_plantNickName, tv_space, tv_cycle, tv_sow, tv_comp, tv_desc;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        bt_back = view.findViewById(R.id.button4);
        bt_generate = view.findViewById(R.id.button6);
        top_view = view.findViewById(R.id.search_view);
        bottom_view = view.findViewById(R.id.plant_detail_view);
        et_plant = view.findViewById(R.id.editText2);
        lv_plantList = view.findViewById(R.id.lv_adminProvide);
        iv_detailImage = view.findViewById(R.id.large_image);
        ll_water = view.findViewById(R.id.waterNeed_bar);
        tv_plantName = view.findViewById(R.id.textView5);
        tv_plantNickName = view.findViewById(R.id.textView6);
        tv_space = view.findViewById(R.id.tv_space);
        tv_cycle = view.findViewById(R.id.tv_cycle);
        tv_sow = view.findViewById(R.id.tv_sow);
        tv_comp = view.findViewById(R.id.tv_comp);
        tv_desc = view.findViewById(R.id.tv_desc);

        new SearchFromDatabase().execute();

        WatchEditText(et_plant);

        lv_plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(getActivity(), plantName.get(i), Toast.LENGTH_SHORT).show();
                //page switch
                top_view.setVisibility(View.GONE);
                bottom_view.setVisibility(View.VISIBLE);
                //load image
                Picasso.get().load(plantImg.get(i)).into(iv_detailImage);
                //set plant name
                tv_plantName.setText(plantName.get(i));
                //set plant nickname
                String nick = "Alias: " + plantNick[i];
                tv_plantNickName.setText(nick);
                //set plant water need
                waterUtils = new WaterUtils();
                int icon_array = R.drawable.ic_water;
                int temp = 0;
                switch (waterNeed.get(i)) {
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
                tv_space.setText(plantSpace[i]);
                tv_cycle.setText(harvestIns.get(i));
                tv_sow.setText(plantSow[i]);
                tv_comp.setText(compPlants[i]);
                tv_desc.setText(plantDesc[i]);
            }
        });


        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_view.setVisibility(View.VISIBLE);
                bottom_view.setVisibility(View.GONE);
                ll_water.removeAllViews();
            }
        });

        bt_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //这部分之上还应该执行生成plan的逻辑，页面跳转是最后执行的，此处仅满足测试需求
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void WatchEditText(final EditText et_plant) {
        et_plant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                plantName.clear();
                harvestIns.clear();
                waterNeed.clear();
                plantImg.clear();
                String data = et_plant.getText().toString();
                for (int i = 0; i < plantName2.size(); ++i) {
                    if (plantName2.get(i).contains(data) || waterNeed2.get(i).contains(data) || harvestIns2.get(i).contains(data)) {
                        plantName.add(plantName2.get(i));
                        harvestIns.add(harvestIns2.get(i));
                        waterNeed.add(waterNeed2.get(i));
                        plantImg.add(plantImg2.get(i));
                    }
                }
                lAdapter.notifyDataSetChanged();
            }
        });
    }

    private class SearchFromDatabase extends  AsyncTask<Void, Void, String> {
        @Override
        protected  String doInBackground(Void... params) {
            String result = restClient.findAllPlants();
            try {
                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray.length() == 0){
                    return null;
                }
                plantNick = new String[jsonArray.length()];
                plantSow = new String[jsonArray.length()];
                plantSpace = new String[jsonArray.length()];
                growthCycle = new String[jsonArray.length()];
                compPlants = new String[jsonArray.length()];
                plantDesc = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++){
                    plantName.add(jsonArray.getJSONObject(i).getString("plantName"));
                    plantNick[i] = jsonArray.getJSONObject(i).getString("plantNick");
                    plantSow[i] = jsonArray.getJSONObject(i).getString("plantSow");
                    plantSpace[i] = jsonArray.getJSONObject(i).getString("plantSpace");
                    growthCycle[i] = jsonArray.getJSONObject(i).getString("growthCycle");
                    harvestIns.add(jsonArray.getJSONObject(i).getString("harvestIns"));
                    compPlants[i] = jsonArray.getJSONObject(i).getString("compPlants");
                    waterNeed.add(jsonArray.getJSONObject(i).getString("waterNeed"));
                    plantDesc[i] = jsonArray.getJSONObject(i).getString("plantDesc");
                    plantImg.add(jsonArray.getJSONObject(i).getString("plantImg"));
                }
                lAdapter = new ListAdapter(getActivity(), plantName, harvestIns, waterNeed, plantImg);

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            lv_plantList.setAdapter(lAdapter);
            plantName2.addAll(plantName);
            harvestIns2.addAll(harvestIns);
            waterNeed2.addAll(waterNeed);
            plantImg2.addAll(plantImg);
        }
    }





}
