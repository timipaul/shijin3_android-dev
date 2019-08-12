package com.shijinsz.shijin.ui.task;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.comm.util.AdError;
import com.shijinsz.shijin.R;


/**
 * Copyright (C)
 * FileName: UnifiedInterstitialADActivity
 * Author: m1342
 * Date: 2019/7/18 11:16
 * Description: ${DESCRIPTION}
 */
public class UnifiedInterstitialADActivity extends Activity implements View.OnClickListener{

    private static final String TAG = UnifiedInterstitialADActivity.class.getSimpleName();
    private InterstitialAD iad;
    private String posId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unified_interstitial_ad);
        ((EditText) findViewById(R.id.posId)).setText("3040652898151811");
        this.findViewById(R.id.loadIAD).setOnClickListener(this);
        this.findViewById(R.id.showIAD).setOnClickListener(this);
        this.findViewById(R.id.showIADAsPPW).setOnClickListener(this);
        this.findViewById(R.id.closeIAD).setOnClickListener(this);
        //((CheckBox) this.findViewById(R.id.cbPos)).setOnCheckedChangeListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadIAD:
                //MultiProcessFlag.setMultiProcess(true);
                //getIAD().loadAD();
                break;
            case R.id.showIAD:
                showAD();
                break;
            case R.id.showIADAsPPW:
                showAsPopup();
                break;
            default:
                break;
        }
    }

    private InterstitialAD getIAD() {
        String posId = "8575134060152130849";
        //String posId = "1000277230686372";
        if (iad != null && this.posId.equals(posId)) {
            return iad;
        }
        this.posId = posId;
        if (this.iad != null) {
            iad.closePopupWindow();
            iad.destroy();
            iad = null;
        }
        if (iad == null) {
            iad = new InterstitialAD(this, "1101152570", posId);
        }
        return iad;
    }

    private void showAD() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("LoadInterstitialAd Fail, error code: %d, error msg: %s",
                                error.getErrorCode(), error.getErrorMsg()));
            }

            @Override
            public void onADReceive() {
                Log.i("AD_DEMO", "onADReceive");
                iad.show();
            }
        });
        iad.loadAD();
    }

    private void showAsPopup() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("LoadInterstitialAd Fail, error code: %d, error msg: %s",
                                error.getErrorCode(), error.getErrorMsg()));
            }

            @Override
            public void onADReceive() {
                iad.showAsPopupWindow();
            }
        });
        iad.loadAD();

    }

    private void closeAsPopup() {
        if (iad != null) {
            iad.closePopupWindow();
        }
    }

}
