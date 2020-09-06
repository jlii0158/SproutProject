package com.example.sproutproject.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.sproutproject.DetailActivity;
import com.example.sproutproject.adapter.ListAdapter;
import com.example.sproutproject.R;
import com.example.sproutproject.database_entity.Plant;
import com.example.sproutproject.networkConnection.Ingredient;
import com.example.sproutproject.networkConnection.RestClient;
import com.example.sproutproject.networkConnection.TransApi;
import com.example.sproutproject.utils.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.core.content.PermissionChecker.checkSelfPermission;


public class SearchFragment extends Fragment {

    private Button  bt_camera, bt_album, bt_search_name, bt_search_water, bt_search_cycle;
    private EditText et_plant, ed_search_by_water, ed_search_by_cycle;
    private ListView lv_plantList;

    private List<String> plantNick, plantNick2, plantSow, plantSow2, plantSpace, plantSpace2, compPlants, compPlants2, plantDesc, plantDesc2, harvestWeek2;
    private List<String> plantName, plantName2, waterNeed, waterNeed2, plantImg, plantImg2, harvestIns, harvestIns2, growthCycle, growthCycle2, harvestWeek;
    ListAdapter lAdapter;
    RestClient restClient = new RestClient();



    private final String TAG = getClass().getSimpleName();
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_PERMISSION_CODE = 267;
    private static final int TAKE_PHOTO = 189;
    private static final int CHOOSE_PHOTO = 385;
    private static final String APP_ID = "20200828000553608";
    private static final String SECURITY_KEY = "99UXraNwDwpwC1ODtTsZ";
    private Toast toast = null;


    public SearchFragment() {
        // Required empty public constructor
    }


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        /*申请读取存储的权限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Objects.requireNonNull(getContext()),PERMISSION_WRITE_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{PERMISSION_WRITE_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }

        et_plant = view.findViewById(R.id.editText2);
        ed_search_by_water = view.findViewById(R.id.ed_search_by_water);
        ed_search_by_cycle = view.findViewById(R.id.ed_search_by_cycle);
        lv_plantList = view.findViewById(R.id.lv_adminProvide);
        bt_camera = view.findViewById(R.id.bt_camera);
        bt_album = view.findViewById(R.id.bt_album);
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
                Plant plant = new Plant();
                plant.setPlant_name(plantName.get(i));
                plant.setPlant_nickname(plantNick.get(i));
                plant.setPlant_sow_ins(plantSow.get(i));
                plant.setPlant_space_ins(plantSpace.get(i));
                plant.setPlant_harvest_ins(harvestIns.get(i));
                plant.setComp_plant(compPlants.get(i));
                plant.setWater_need(waterNeed.get(i));
                plant.setPlant_desc(plantDesc.get(i));
                plant.setPlant_img(plantImg.get(i));
                plant.setGrowth_cycle(growthCycle.get(i));
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("plant",plant);
                startActivity(intent);
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
                    if (data.toLowerCase().equals("baby cabbage")) {
                        data = "chinese cabbage";
                    }
                    if (plantName2.get(i).toLowerCase().contains(data.toLowerCase()) || data.toLowerCase().contains(plantName2.get(i).toLowerCase())) {
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
                        showToast(back);
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
