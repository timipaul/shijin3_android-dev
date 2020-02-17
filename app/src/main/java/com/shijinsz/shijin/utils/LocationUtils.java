package com.shijinsz.shijin.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/8/1.
 */

public class LocationUtils {

    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider;
    private Context mContext;

    public LocationUtils(Context context){
        mContext = context;
    }

    public void getCity(TextView textView){
        //获取定位服务
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        //使用网络位置定位
        provider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            String string =  location.getLatitude() + "," + location.getLongitude();

            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_LOCATION,location.getLongitude()+","+location.getLatitude());
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_LON,location.getLongitude()+"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_LAT,location.getLatitude()+"");

            //创建okHttpClient对象
            OkHttpClient okClient = new OkHttpClient();
            //创建一个Request
            final Request request = new Request.Builder()
                    .url("http://api.map.baidu.com/geocoder?output=json&location="+ string +"&ak=esNPFDwwsXWtsQfw4NMNmur1")
                    .build();
            //new call
            Call call = okClient.newCall(request);
            //请求加入调度
            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String htmlStr =  response.body().string();
                    try {
                        JSONObject result = new JSONObject(htmlStr);
                        //result
                       String city =  result.getJSONObject("result").getJSONObject("addressComponent").get("city").toString();
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_LOCATE, city);
                        try {
                            textView.setText(city);
                        }catch (Exception e) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}
