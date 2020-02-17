package com.shijinsz.shijin.ui.activity;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.hongchuang.ysblibrary.common.Constants;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

import java.util.Locale;

/**
 * Copyright (C)
 * FileName: UnifiedInterstitialADActivity
 * Author: m1342
 * Date: 2019/7/30 10:03
 * Description: 显示弹框
 */
public class UnifiedInterstitialADActivity extends Activity implements View.OnClickListener,
        UnifiedInterstitialADListener, CompoundButton.OnCheckedChangeListener{

    private static final String TAG = UnifiedInterstitialADActivity.class.getSimpleName();
    private UnifiedInterstitialAD iad;


    public Activity mContext;
    public UnifiedInterstitialADActivity(Activity context){
        this.mContext = context;
    }

    public void Destroy() {
        if (iad != null) {
            iad.destroy();
        }
    }

    public void loadAd(){

        getIAD().loadAD();
    }

    public void showAd(){
       // Toast.makeText(getApplicationContext(),"显示",Toast.LENGTH_LONG).show();
        showAD();
    }

    private UnifiedInterstitialAD getIAD() {

        if (iad != null ) {
            return iad;
        }

        if (iad == null) {
            iad = new UnifiedInterstitialAD(mContext, Constants.APPID, Constants.UNIFIED_INTERSTITIAL_ID_LARGE_SMALL, this);

        }
        return iad;
    }


    private void showAD() {
        if (iad != null) {
            iad.show();
        } else {
            Log.i(TAG,"请加载广告后再进行展示 ！");
            //Toast.makeText(getApplicationContext(), "请加载广告后再进行展示 ！ ", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onADReceive() {
        //Toast.makeText(getApplicationContext(), "广告加载成功 ！ ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNoAD(AdError error) {
        String msg = String.format(Locale.getDefault(), "onNoAD, error code: %d, error msg: %s",
                error.getErrorCode(), error.getErrorMsg());
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onADOpened() {
        Log.i(TAG, "onADOpened");
    }

    @Override
    public void onADExposure() {
        Log.i(TAG, "onADExposure");
    }

    @Override
    public void onADClicked() {
        Log.i(TAG, "onADClicked");
    }

    @Override
    public void onADLeftApplication() {
        Log.i(TAG, "onADLeftApplication");
    }

    @Override
    public void onADClosed() {
        Log.i(TAG, "onADClosed");
    }
}
