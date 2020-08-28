package com.example.sproutproject.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
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
import com.example.sproutproject.networkConnection.Ingredient;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.networkConnection.TransApi;
import com.example.sproutproject.utils.ThreadUtils;
import com.example.sproutproject.utils.WaterUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static androidx.core.content.PermissionChecker.checkSelfPermission;


public class SearchFragment extends Fragment {

    private Button bt_back,bt_collect, bt_generate, bt_camera, bt_album;
    private View top_view, bottom_view;
    String plant_name;
    private EditText et_plant;
    private ListView lv_plantList;
    String[] plantNick, plantSow, plantSpace, growthCycle, compPlants, plantDesc;
    private List<String> plantName, plantName2, waterNeed, waterNeed2, plantImg, plantImg2, harvestIns, harvestIns2;
    ListAdapter lAdapter;
    RestClient restClient = new RestClient();
    ImageView iv_detailImage;
    private WaterUtils waterUtils;
    private LinearLayout ll_water;
    private TextView tv_plantName,tv_plantNickName, tv_space, tv_cycle, tv_sow, tv_comp, tv_desc, tv_water;

    private final String TAG = getClass().getSimpleName();
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_PERMISSION_CODE = 267;
    private static final int TAKE_PHOTO = 189;
    private static final int CHOOSE_PHOTO = 385;
    private static final String APP_ID = "20200828000553608";
    private static final String SECURITY_KEY = "99UXraNwDwpwC1ODtTsZ";

    public SearchFragment() {
        // Required empty public constructor
    }


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        /*申请读取存储的权限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Objects.requireNonNull(getContext()),PERMISSION_WRITE_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{PERMISSION_WRITE_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }

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
        tv_water = view.findViewById(R.id.tv_water);
        bt_camera = view.findViewById(R.id.bt_camera);
        bt_album = view.findViewById(R.id.bt_album);

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
                tv_water.setText("Water need is " + waterNeed.get(i));
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

        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
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
                    if (plantName2.get(i).contains(data) || waterNeed2.get(i).contains(data) || harvestIns2.get(i).contains(data)
                        || plantName2.get(i).toLowerCase().contains(data) || harvestIns2.get(i).toLowerCase().contains(data)) {
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
            //if (plantName == null) {
                String result = restClient.findAllPlants();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() == 0) {
                        return null;
                    }

                    plantName = new ArrayList<String>();
                    plantName2 = new ArrayList<String>();
                    waterNeed = new ArrayList<String>();
                    waterNeed2 = new ArrayList<String>();
                    plantImg = new ArrayList<String>();
                    plantImg2 = new ArrayList<String>();
                    harvestIns = new ArrayList<String>();
                    harvestIns2 = new ArrayList<String>();

                    plantNick = new String[jsonArray.length()];
                    plantSow = new String[jsonArray.length()];
                    plantSpace = new String[jsonArray.length()];
                    growthCycle = new String[jsonArray.length()];
                    compPlants = new String[jsonArray.length()];
                    plantDesc = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
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
                    plantName2.addAll(plantName);
                    harvestIns2.addAll(harvestIns);
                    waterNeed2.addAll(waterNeed);
                    plantImg2.addAll(plantImg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            //}
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            lv_plantList.setAdapter(lAdapter);

        }
    }


    /**
     * Open album
     */
    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);//打开相册
    }

    /**
     * Take Picture
     */
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
        }
    }

    /*申请权限的回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: permission granted");
        } else {
            Log.i(TAG, "onRequestPermissionsResult: permission denied");
            Toast.makeText(getActivity(), "You Denied Permission", Toast.LENGTH_SHORT).show();
        }
    }

    /*相机或者相册返回来的数据*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap  bitmap = data.getParcelableExtra("data");
                    //先将bitmap转为byte[]
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    final byte[] bytes = baos.toByteArray();

                    //将拍照得到的照片转成bytes传给百度API的方法
                    ThreadUtils.runInThread(new Runnable() {
                        @Override
                        public void run() {
                            String a = Ingredient.ingredient(bytes);
                            try {
                                JSONObject jsonObject = new JSONObject(a);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");

                                //Translate the match result to english
                                String query = jsonArray.getJSONObject(0).getString("name");
                                TransApi api = new TransApi(APP_ID, SECURITY_KEY);
                                String resultArray = api.getTransResult(query, "auto", "en");
                                JSONObject jsonObject1 = new JSONObject(resultArray);
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("trans_result");
                                final String result = jsonArray1.getJSONObject(0).getString("dst");
                                //set editText
                                ThreadUtils.runInUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        et_plant.setText(result);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case CHOOSE_PHOTO:
                if (data == null) {//如果没有选取照片，则直接返回
                    return;
                }
                Log.i(TAG, "onActivityResult: ImageUriFromAlbum: " + data.getData());
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream iStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final byte[] inputData = getBytes(iStream);
                        Log.e("TAG", imageUri.toString());

                        ThreadUtils.runInThread(new Runnable() {
                            @Override
                            public void run() {
                                String a = Ingredient.ingredient(inputData);
                                try {
                                    JSONObject jsonObject = new JSONObject(a);
                                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                                    //Translate the match result to english
                                    String query = jsonArray.getJSONObject(0).getString("name");
                                    TransApi api = new TransApi(APP_ID, SECURITY_KEY);
                                    String resultArray = api.getTransResult(query, "auto", "en");
                                    JSONObject jsonObject1 = new JSONObject(resultArray);
                                    JSONArray jsonArray1 = jsonObject1.getJSONArray("trans_result");
                                    final String result = jsonArray1.getJSONObject(0).getString("dst");
                                    //set editText
                                    ThreadUtils.runInUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            et_plant.setText(result);
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



}
