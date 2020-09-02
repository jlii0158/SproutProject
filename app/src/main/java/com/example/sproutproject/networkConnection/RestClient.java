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
    private static final String getAllCredential_URL = "https://qw1zshde50.execute-api.ap-southeast-2.amazonaws.com/findCredential/findallcredential";
    private static final String getAllUser_URL = "https://j019m0u38a.execute-api.ap-southeast-2.amazonaws.com/findUser/findalluser";
    private static final String postUser_URL = "https://auop78cuee.execute-api.ap-southeast-2.amazonaws.com/postUser/postuser";
    private static final String postCredential_URL = "https://p6bcs1jb52.execute-api.ap-southeast-2.amazonaws.com/postCredential/postcredential";


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

    public static String findAllCredential() {
        Request.Builder builder = new Request.Builder();
        builder.url(getAllCredential_URL);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public static int postCredential(String cre_id, String nickname, String email, String signup_password) {
        int userId = getUserId();
        String tempUser = String.valueOf(userId);
        UserTable userTable = new UserTable(tempUser, "0", nickname, "6");

        postUser(userTable);

        String date = getCurrentDate();

        Credential credential = new Credential(cre_id, email, signup_password, date, tempUser);

        Gson gson = new Gson();
        String credentialJson = gson.toJson(credential);
        String strResponse="";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json " , credentialJson);
        RequestBody body = RequestBody.create(credentialJson, JSON);
        Request request = new Request.Builder()
                .url(postCredential_URL)
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

    public static int postUser(UserTable userTable) {

        Gson gson = new Gson();
        String userJson = gson.toJson(userTable);
        String strResponse="";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json " , userJson);
        RequestBody body = RequestBody.create(userJson, JSON);
        Request request = new Request.Builder()
                .url(postUser_URL)
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

    private static int getUserId() {
        String result = getAllUser();
        String[] user_id;
        int newUserId = 0;
        try {
            JSONArray jsonArray = new JSONArray(result);
            if(jsonArray.length() == 0){
                return 0;
            }
            user_id = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++){
                user_id[i] = jsonArray.getJSONObject(i).getString("user_id");
            }
            //get the new user id, = last user id + 1
            newUserId = Integer.parseInt(user_id[jsonArray.length() - 1]) + 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newUserId;
    }

    private static String getAllUser() {
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

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static String getCurrentDate() {
        long cur_time = System.currentTimeMillis();
        String datetime = df.format(new Date(cur_time));
        return datetime;
    }

}
