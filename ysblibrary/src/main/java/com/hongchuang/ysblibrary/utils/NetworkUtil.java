package com.hongchuang.ysblibrary.utils;

import android.preference.PreferenceActivity;
import android.util.Log;

import com.baidu.mapapi.http.HttpClient;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiguang.net.HttpResponse;

/**
 * Created by Administrator on 2018/6/7.
 */

public class NetworkUtil {


    /*
    * 传入一个Url地址  返回一个JSON字符串
    * 网络请求的情况分析:
    *   如果是404 500 ... 代表网络(Http协议)请求失败
    *   200 服务器返回成功
    *       业务成功  /业务失败
    * */
    public static String doGet(String urlPath) {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            Map<String, List<String>> map = conn.getHeaderFields();
//            Log.i("HttpURLConnection", "doGet: "+new Gson().toJson(map));
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                Log.i("HttpURLConnection", "doGet: "+reader.readLine());
                return reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{ \"success\": false,\n   \"errorMsg\": \"后台服务器开小差了!\",\n     \"result\":{}}";
    }

    /*
        * 传入一个Url地址  返回一个JSON字符串
        * 网络请求的情况分析:
        *   如果是404 500 ... 代表网络(Http协议)请求失败
        *   200 服务器返回成功
        *       业务成功  /业务失败
        * */
    public static Map<String, List<String>> doGetHeader(String urlPath) {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                Map<String, List<String>> map = conn.getHeaderFields();
                Log.i("HttpURLConnection", "doGet: "+new Gson().toJson(map));
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String doPost(String urlPath, HashMap<String, String> paramsMap) {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //--------------------------------
            conn.setDoOutput(true);
            conn.getOutputStream().write(getParams(paramsMap).getBytes());
            //--------------------------------
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                return reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{ \"success\": false,\n   \"errorMsg\": \"后台服务器开小差了!\",\n     \"result\":{}}";
    }

    private static String getParams(HashMap<String, String> paramsMap) {
        String result = "";
        for (HashMap.Entry<String, String> entity : paramsMap.entrySet()) {
            result += "&" + entity.getKey() + "=" + entity.getValue();
        }
        return result.substring(1);
    }


}