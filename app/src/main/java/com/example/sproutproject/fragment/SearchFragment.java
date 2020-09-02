package com.example.sproutproject.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    private Button bt_back,bt_collect, bt_generate, bt_camera, bt_album, bt_search_name, bt_search_water, bt_search_cycle;
    private View top_view, bottom_view;
    String plant_name, imgUrl;
    private EditText et_plant, ed_search_by_water, ed_search_by_cycle;
    private ListView lv_plantList;

    private List<String> plantNick, plantNick2, plantSow, plantSow2, plantSpace, plantSpace2, compPlants, compPlants2, plantDesc, plantDesc2, harvestWeek2;
    private List<String> plantName, plantName2, waterNeed, waterNeed2, plantImg, plantImg2, harvestIns, harvestIns2, growthCycle, growthCycle2, harvestWeek;
    ListAdapter lAdapter;
    RestClient restClient = new RestClient();
    ImageView iv_detailImage, iv_large, iv_nice_image;
    private WaterUtils waterUtils;
    private LinearLayout ll_water;
    private TextView tv_plantName,tv_plantNickName, tv_space, tv_cycle, tv_sow, tv_comp, tv_water;
    CardView cv_detail;
    View imgEntryView;

    private final String TAG = getClass().getSimpleName();
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_PERMISSION_CODE = 267;
    private static final int TAKE_PHOTO = 189;
    private static final int CHOOSE_PHOTO = 385;
    private static final String APP_ID = "20200828000553608";
    private static final String SECURITY_KEY = "99UXraNwDwpwC1ODtTsZ";
    private Toast toast = null;

    public static String plant_name_pass, plant_water_pass, plant_harvest_pass, plant_img_pass;

    public SearchFragment() {
        // Required empty public constructor
    }


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

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
        ed_search_by_water = view.findViewById(R.id.ed_search_by_water);
        ed_search_by_cycle = view.findViewById(R.id.ed_search_by_cycle);
        lv_plantList = view.findViewById(R.id.lv_adminProvide);
        iv_detailImage = view.findViewById(R.id.large_img);
        iv_large = view.findViewById(R.id.large_image_show);
        ll_water = view.findViewById(R.id.waterNeed_bar);
        tv_plantName = view.findViewById(R.id.textView5);
        tv_plantNickName = view.findViewById(R.id.textView6);
        tv_space = view.findViewById(R.id.tv_space);
        tv_cycle = view.findViewById(R.id.tv_cycle);
        tv_sow = view.findViewById(R.id.tv_sow);
        tv_comp = view.findViewById(R.id.tv_comp);
        tv_water = view.findViewById(R.id.tv_water);
        bt_camera = view.findViewById(R.id.bt_camera);
        bt_album = view.findViewById(R.id.bt_album);
        cv_detail = view.findViewById(R.id.large_image);
        imgEntryView = inflater.inflate(R.layout.dialog_photo, null);
        iv_nice_image = view.findViewById(R.id.iv_nice_image);
        bt_search_name = view.findViewById(R.id.bt_search_name);
        bt_search_water = view.findViewById(R.id.bt_search_water);
        bt_search_cycle  = view.findViewById(R.id.bt_search_cycle);


        new SearchFromDatabase().execute();

        WatchEditText(et_plant);
        WatchEditText1(ed_search_by_water);
        WatchEditText2(ed_search_by_cycle);

        bt_search_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_search_name.setVisibility(View.INVISIBLE);
                et_plant.setVisibility(View.INVISIBLE);
                et_plant.setText("");
                bt_search_water.setVisibility(View.VISIBLE);
                ed_search_by_water.setVisibility(View.VISIBLE);
            }
        });

        bt_search_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_search_water.setVisibility(View.INVISIBLE);
                ed_search_by_water.setVisibility(View.INVISIBLE);
                ed_search_by_water.setText("");
                bt_search_cycle.setVisibility(View.VISIBLE);
                ed_search_by_cycle.setVisibility(View.VISIBLE);
            }
        });

        bt_search_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_search_cycle.setVisibility(View.INVISIBLE);
                ed_search_by_cycle.setVisibility(View.INVISIBLE);
                ed_search_by_cycle.setText("");
                bt_search_name.setVisibility(View.VISIBLE);
                et_plant.setVisibility(View.VISIBLE);
            }
        });

        lv_plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                plant_name_pass = plantName.get(i);
                plant_water_pass = waterNeed.get(i);
                plant_harvest_pass = growthCycle.get(i);
                plant_img_pass = plantDesc.get(i);

                Toast.makeText(getActivity(), plantName.get(i), Toast.LENGTH_SHORT).show();
                //page switch
                top_view.setVisibility(View.GONE);
                bottom_view.setVisibility(View.VISIBLE);
                //load image
                imgUrl = plantImg.get(i);
                Picasso.get().load(imgUrl).into(iv_detailImage);
                //set plant name
                tv_plantName.setText(plantName.get(i));
                //set plant nickname
                String aaa = plantNick.get(i);
                if (!plantNick.get(i).equals("null")) {
                    String nick = "Alias: " + plantNick.get(i);
                    tv_plantNickName.setText(nick);
                }

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
                tv_space.setText(plantSpace.get(i));
                tv_cycle.setText(harvestIns.get(i));
                tv_sow.setText(plantSow.get(i));
                tv_comp.setText(compPlants.get(i));
                Picasso.get().load(plantDesc.get(i)).into(iv_nice_image);
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

        cv_detail.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                //dialog.show();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View imgEntryView = inflater.inflate(R.layout.dialog_photo, null); // 加载自定义的布局文件
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
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
                plantNick.clear();
                plantSow.clear();
                plantSpace.clear();
                compPlants.clear();
                plantDesc.clear();
                growthCycle.clear();
                String data = et_plant.getText().toString();
                for (int i = 0; i < plantName2.size(); ++i) {
                    if (plantName2.get(i).contains(data.toLowerCase()) || plantName2.get(i).toLowerCase().contains(data)
                            || data.contains(plantName2.get(i).toLowerCase())) {
                        plantName.add(plantName2.get(i));
                        harvestIns.add(harvestIns2.get(i));
                        waterNeed.add(waterNeed2.get(i));
                        plantImg.add(plantImg2.get(i));
                        plantNick.add(plantNick2.get(i));
                        plantSow.add(plantSow2.get(i));
                        plantSpace.add(plantSpace2.get(i));
                        compPlants.add(compPlants2.get(i));
                        plantDesc.add(plantDesc2.get(i));
                        growthCycle.add(growthCycle2.get(i));
                    }
                }
                lAdapter.notifyDataSetChanged();
                if (plantName.size() == 0) {
                    String back = "Sorry, no findings";
                    showToast(back);
                }
            }
        });
    }

    private void WatchEditText1(final EditText et_plant) {
        ed_search_by_water.addTextChangedListener(new TextWatcher() {
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
                plantNick.clear();
                plantSow.clear();
                plantSpace.clear();
                compPlants.clear();
                plantDesc.clear();
                growthCycle.clear();
                String data = ed_search_by_water.getText().toString();
                for (int i = 0; i < plantName2.size(); ++i) {
                    if (waterNeed2.get(i).contains(data.toLowerCase())) {
                        plantName.add(plantName2.get(i));
                        harvestIns.add(harvestIns2.get(i));
                        waterNeed.add(waterNeed2.get(i));
                        plantImg.add(plantImg2.get(i));
                        plantNick.add(plantNick2.get(i));
                        plantSow.add(plantSow2.get(i));
                        plantSpace.add(plantSpace2.get(i));
                        compPlants.add(compPlants2.get(i));
                        plantDesc.add(plantDesc2.get(i));
                        growthCycle.add(growthCycle2.get(i));
                    }
                }
                lAdapter.notifyDataSetChanged();
                if (plantName.size() == 0) {
                    String back = "Sorry, no findings";
                    showToast(back);
                }
            }
        });
    }

    private void WatchEditText2(final EditText et_plant) {
        ed_search_by_cycle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = ed_search_by_cycle.getText().toString();
                plantName.clear();
                harvestIns.clear();
                waterNeed.clear();
                plantImg.clear();
                plantNick.clear();
                plantSow.clear();
                plantSpace.clear();
                compPlants.clear();
                plantDesc.clear();
                growthCycle.clear();
                harvestWeek.clear();

                if (!temp.equals("")) {
                    int aTempValue = 0;
                    int data = 0;
                    for (int i = 0; i < temp.length(); i++) {
                        if (!Character.isDigit(temp.charAt(i))) {
                            aTempValue = 1;
                        }
                    }
                    if (aTempValue == 0) {
                        data = Integer.parseInt(temp);
                    } else {
                        String back = "Please enter a number";
                        Toast.makeText(getActivity(), back, Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < plantName2.size(); ++i) {
                        if (stringToInt(data, harvestWeek2.get(i)) == 1) {
                            plantName.add(plantName2.get(i));
                            harvestIns.add(harvestIns2.get(i));
                            waterNeed.add(waterNeed2.get(i));
                            plantImg.add(plantImg2.get(i));
                            plantNick.add(plantNick2.get(i));
                            plantSow.add(plantSow2.get(i));
                            plantSpace.add(plantSpace2.get(i));
                            compPlants.add(compPlants2.get(i));
                            plantDesc.add(plantDesc2.get(i));
                            growthCycle.add(growthCycle2.get(i));
                            harvestWeek.add(harvestWeek2.get(i));
                        }
                    }
                } else {
                    plantNick.addAll(plantNick2);
                    plantSow.addAll(plantSow2);
                    plantSpace.addAll(plantSpace2);
                    compPlants.addAll(compPlants2);
                    plantDesc.addAll(plantDesc2);
                    plantName.addAll(plantName2);
                    harvestIns.addAll(harvestIns2);
                    waterNeed.addAll(waterNeed2);
                    plantImg.addAll(plantImg2);
                    growthCycle.addAll(growthCycle2);
                    harvestWeek.addAll(harvestWeek2);
                }
                lAdapter.notifyDataSetChanged();
                if (plantName.size() == 0) {
                    String back = "Sorry, no findings";
                    showToast(back);
                }
            }
        });
    }

    public int stringToInt(int test, String value) {
        int a = 0;
        value = value.replace("'", "");
        String[] valueArr = value.split(",");
        int[] intArr = new int[valueArr.length];
        for (int i = 0; i < valueArr.length; i++) {
            intArr[i] = Integer.parseInt(valueArr[i]);
            System.out.println(intArr[i]);
        }
        for (int i = 0; i < valueArr.length; i++) {
            if (test == intArr[i]) {
                a = 1;
            }
        }
        return a;
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

                    harvestWeek = new ArrayList<String>();
                    harvestWeek2 = new ArrayList<String>();
                    growthCycle = new ArrayList<String>();
                    growthCycle2 = new ArrayList<String>();

                    plantName = new ArrayList<String>();
                    plantName2 = new ArrayList<String>();
                    waterNeed = new ArrayList<String>();
                    waterNeed2 = new ArrayList<String>();
                    plantImg = new ArrayList<String>();
                    plantImg2 = new ArrayList<String>();
                    harvestIns = new ArrayList<String>();
                    harvestIns2 = new ArrayList<String>();

                    plantNick = new ArrayList<String>();
                    plantNick2 = new ArrayList<String>();
                    plantSow = new ArrayList<String>();
                    plantSow2 = new ArrayList<String>();
                    plantSpace = new ArrayList<String>();
                    plantSpace2 = new ArrayList<String>();
                    compPlants = new ArrayList<String>();
                    compPlants2 = new ArrayList<String>();
                    plantDesc = new ArrayList<String>();
                    plantDesc2 = new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        plantName.add(jsonArray.getJSONObject(i).getString("plantName"));
                        plantNick.add(jsonArray.getJSONObject(i).getString("plantNick"));
                        plantSow.add(jsonArray.getJSONObject(i).getString("plantSow"));
                        plantSpace.add(jsonArray.getJSONObject(i).getString("plantSpace"));
                        growthCycle.add(jsonArray.getJSONObject(i).getString("growthCycle"));
                        harvestIns.add(jsonArray.getJSONObject(i).getString("harvestIns"));
                        compPlants.add(jsonArray.getJSONObject(i).getString("compPlants"));
                        waterNeed.add(jsonArray.getJSONObject(i).getString("waterNeed"));
                        plantDesc.add(jsonArray.getJSONObject(i).getString("plantDesc"));
                        plantImg.add(jsonArray.getJSONObject(i).getString("plantImg"));
                        harvestWeek.add(jsonArray.getJSONObject(i).getString("harvestWeek"));
                    }
                    lAdapter = new ListAdapter(getActivity(), plantName, harvestIns, waterNeed, plantImg);
                    plantNick2.addAll(plantNick);
                    plantSow2.addAll(plantSow);
                    plantSpace2.addAll(plantSpace);
                    compPlants2.addAll(compPlants);
                    plantDesc2.addAll(plantDesc);
                    plantName2.addAll(plantName);
                    harvestIns2.addAll(harvestIns);
                    waterNeed2.addAll(waterNeed);
                    plantImg2.addAll(plantImg);
                    growthCycle2.addAll(growthCycle);
                    harvestWeek2.addAll(harvestWeek);
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
    /*
    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);//打开相册
    }
    */

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= 23) {
            if(Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},222);
                return;
            }else{
                startActivityForResult(intent,CHOOSE_PHOTO);
            }
        } else {
            startActivityForResult(intent,CHOOSE_PHOTO);
        }
    }

    /**
     * Take Picture
     */
    /*
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
        }
    }*/
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= 23) {
            if(Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{
                startActivityForResult(intent,TAKE_PHOTO);
            }
        } else {
            startActivityForResult(intent,TAKE_PHOTO);
        }
    }


    /*申请权限的回调*/
    /*
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
    */

    /*The data return back from camera or album*/
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
                            ThreadUtils.runInUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    String search= "Searching...";
                                    Toast.makeText(getActivity(), search, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //user AI API to recognize the picture
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
                                if (result.equals("Non fruit and vegetable ingredients")) {
                                    ThreadUtils.runInUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String back= "Sorry, the picture you took is not fruit or vegetable";
                                            Toast.makeText(getActivity(), back, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    //set editText
                                    ThreadUtils.runInUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            et_plant.setText(result);
                                        }
                                    });
                                }
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
                                ThreadUtils.runInUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String search= "Searching...";
                                        Toast.makeText(getActivity(), search, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //user AI API to recognize the picture
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
                                    if (result.equals("Non fruit and vegetable ingredients")) {
                                        ThreadUtils.runInUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String back= "Sorry, the picture you choose is not a fruit or a vegetable";
                                                Toast.makeText(getActivity(), back, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else {
                                        //set editText
                                        ThreadUtils.runInUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                et_plant.setText(result);
                                            }
                                        });
                                    }
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

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
