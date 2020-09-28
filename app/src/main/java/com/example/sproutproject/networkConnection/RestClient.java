package com.example.sproutproject.networkConnection;


import android.util.Log;

import com.example.sproutproject.database_entity.Credential;
import com.example.sproutproject.database_entity.UserTable;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Date;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {

    //jingweili account
    //private static final String BASE_URL = "https://kmg09z4my0.execute-api.ap-southeast-2.amazonaws.com/iteration-1-search/allplants";
    //jlii0158 student account
    private static final String getAllPlants_URL = "https://fq4h60gx39.execute-api.ap-southeast-2.amazonaws.com/iteration-1-findPlants/plants";
    private static final String getAllUser_URL = "https://j019m0u38a.execute-api.ap-southeast-2.amazonaws.com/findUser/findalluser";
    private static final String postUser_URL = "https://7lykoo8hjj.execute-api.ap-southeast-2.amazonaws.com/postUserWithDetail/postuserwithdetail";
    private static final String getPlantByName_URL = "https://obrykx3jfb.execute-api.ap-southeast-2.amazonaws.com/findPlantByName/findplantbyname?plantName=";
    private static final String updateGrowValue_URL = "https://h68g7av5mg.execute-api.ap-southeast-2.amazonaws.com/updateGrowValue/updategrowvalue?user_grow=";
    private static final String getAllMedals_URL = "https://fok9mlpzah.execute-api.us-east-2.amazonaws.com/test/getallmedalresource";


    private static OkHttpClient client=null;
    private static String results;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public RestClient(){
        client=new OkHttpClient();
    }

    public String findAllPlants(){
        Request.Builder builder = new Request.Builder();
        builder.url(getAllPlants_URL);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public String findPlantByName(String plantName){
        Request.Builder builder = new Request.Builder();
        builder.url(getPlantByName_URL + plantName);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public String findAllUser() {
        Request.Builder builder = new Request.Builder();
        builder.url(getAllUser_URL);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }


    public static int postUser(String nickname, String email, String signup_password) {

        UserTable userTable = new UserTable("0", nickname, "6");

        String res = "?user_grow=0&user_nick=" + nickname + "&user_name=" + email + "&password_hash=" + signup_password + "&head_id=6";
        Gson gson = new Gson();
        String credentialJson = gson.toJson(userTable);
        String strResponse="";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json " , credentialJson);
        RequestBody body = RequestBody.create(credentialJson, JSON);
        Request request = new Request.Builder()
                .url(postUser_URL + res)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int updateGrowValue(String growValue, String userAccount) {

        UserTable userTable = new UserTable("0", "kkk", "6");
        Gson gson = new Gson();
        String credentialJson = gson.toJson(userTable);
        RequestBody body = RequestBody.create(credentialJson, JSON);
        Request request = new Request.Builder()
                .url(updateGrowValue_URL + Integer.parseInt(growValue) + "&user_name=" + userAccount)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String findAllMedals(){
        Request.Builder builder = new Request.Builder();
        builder.url(getAllMedals_URL);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

}
