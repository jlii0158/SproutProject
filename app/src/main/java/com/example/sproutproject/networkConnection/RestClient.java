package com.example.sproutproject.networkConnection;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestClient {

    //jingweili account
    //private static final String BASE_URL = "https://kmg09z4my0.execute-api.ap-southeast-2.amazonaws.com/iteration-1-search/allplants";
    //jlii0158 student account
    private static final String BASE_URL = "https://fq4h60gx39.execute-api.ap-southeast-2.amazonaws.com/iteration-1-findPlants/plants";


    private OkHttpClient client=null;
    private String results;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public RestClient(){
        client=new OkHttpClient();
    }

    public String findAllPlants(){
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL);
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
