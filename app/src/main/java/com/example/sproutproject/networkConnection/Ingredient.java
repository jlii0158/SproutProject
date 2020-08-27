package com.example.sproutproject.networkConnection;


import android.annotation.SuppressLint;

import java.net.URLEncoder;
public class Ingredient {

    public static String ingredient(byte[] imgData) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/ingredient";
        try {
            // 本地文件路径
            //@SuppressLint("SdCardPath") String filePath = "/sdcard/download.jpg";
            //byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.6dd96e3883509a7ce4feb726c08f054e.2592000.1601106865.282335-22298707";

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
