package com.xiqu.sdklibrary.util;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;

import com.xiqu.sdklibrary.view.AdListActivity;

public class XWUtils {

    private static final String TAG = "XWUtils";
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_APP_SECRET = "app_secret";
    private static final String KEY_APP_SIGN = "app_sign";
    private static final String KEY_APP_AD_ID = "app_adId";
    private static final String KEY_ACTIONBAR_TITLE = "actionbar_title";
    private static final String KEY_ACTIONBAR_BG_COLOR = "actionbar_bg_clolor";
    private static final String KEY_ACTIONBAR_TITLE_COLOR = "actionbar_title_clolor";

    private static final String KEY_APP_H5_URL = "app_h5_url";
    private static final String default_list_url = "http://h5.wangzhuantianxia.com/try/try_list_plus.aspx?";
    private static final String default_detail_url = "http://h5.wangzhuantianxia.com/try/try_cpl_plus.aspx?";

    private String titleBGColorString = "#FF5200";          //actionBar背景 颜色
    private String titleTextColorString = "#FFFFFF";        //actionbar 标题颜色
    private int tileBGColor = Color.parseColor(titleBGColorString);
    private int tileTextColor = Color.parseColor(titleTextColorString);

    private String appId;               //渠道appid
    private String appSecret;           //用户密钥
    private String appSign;             //用户userId
    private String baseUrl;
    private String adId;                //广告Id

    private String title;

    private int mode = 0;               // 展示形式 0: AD列表 1:AD详情

    private static volatile XWUtils sInst = null;

    private Context mContext = null;

    private XWUtils(Context context) {
        this.mContext = context;
    }

    public static XWUtils getInstance(Context context) {
        XWUtils inst = sInst;
        if (inst == null) {
            synchronized (XWUtils.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new XWUtils(context.getApplicationContext());
                    sInst = inst;
                }
            }
        }
        return inst;
    }

    public void init(String appid, String secret, String sign) {
        if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(secret) || TextUtils.isEmpty(sign)) {
            Toast.makeText(mContext, "请检查参数", Toast.LENGTH_SHORT).show();
        } else {
            appId = appid;
            appSecret = secret;
            appSign = sign;
            AppUtil.saveValue(mContext, KEY_APP_ID, appid);
            AppUtil.saveValue(mContext, KEY_APP_SECRET, secret);
            AppUtil.saveValue(mContext, KEY_APP_SIGN, sign);
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mod) {
        mode = mod;
    }

    public String getAdId() {
        if (adId == null) {
            adId = AppUtil.getValue(mContext, KEY_APP_AD_ID, null);
        }
        return adId;
    }


    public void setAdId(String adId) {

        if (TextUtils.isEmpty(adId)) {
            return;
        }
        this.adId = adId;
        AppUtil.saveValue(mContext, KEY_APP_AD_ID, adId);
    }

    public String getTitle() {
        if (title == null) {
            title = AppUtil.getValue(mContext, KEY_ACTIONBAR_TITLE, "嘻趣");
        }
        return title;
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        this.title = title;
        AppUtil.saveValue(mContext, KEY_ACTIONBAR_TITLE, title);
    }

    public void setBaseUrl(String h5url) {
        if (TextUtils.isEmpty(h5url)) {
            return;
        }
        this.baseUrl = h5url;
        AppUtil.getValue(mContext, KEY_APP_H5_URL, h5url);
    }

    public String getBaseUrl() {
        if (mode == 0) {
            baseUrl = default_list_url;
        } else {
            baseUrl = default_detail_url;
        }
        return baseUrl;
    }

    public void setAppId(String appid) {
        if (TextUtils.isEmpty(appid)) {
            return;
        }
        this.appId = appid;
        AppUtil.saveValue(mContext, KEY_APP_ID, appid);
    }

    public String getAppId() {
        if (appId == null) {
            appId = AppUtil.getValue(mContext, KEY_APP_ID, null);
        }
        return appId;
    }

    public void setAppSecret(String secret) {
        if (TextUtils.isEmpty(secret)) {
            return;
        }
        this.appSecret = secret;
        AppUtil.saveValue(mContext, KEY_APP_SECRET, secret);
    }

    public String getAppSecret() {
        if (appSecret == null) {
            appSecret = AppUtil.getValue(mContext, KEY_APP_SECRET, null);
        }
        return appSecret;
    }

    public void setAppSign(String sign) {
        if (TextUtils.isEmpty(sign)) {
            return;
        }
        this.appSign = sign;
        AppUtil.saveValue(mContext, KEY_APP_SIGN, sign);
    }

    public String getAppSign() {
        if (appSign == null) {
            appSign = AppUtil.getValue(mContext, KEY_APP_SIGN, null);
        }
        return appSign;
    }

    public void jumpToAd() {
        if (getAppId() == null)
            throw new IllegalArgumentException("appid can not be null");
        if (getAppSecret() == null)
            throw new IllegalArgumentException("appsecret can not be null");
        if (getAppSign() == null)
            throw new IllegalArgumentException("appsign can not be null");
        Intent intent = new Intent(mContext, AdListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public String getTitleBGColorString() {
        if (titleBGColorString == null) {
            titleBGColorString = AppUtil.getValue(mContext, KEY_ACTIONBAR_BG_COLOR, "#FA6B24");
        }
        return titleBGColorString;
    }

    public void setTitleBGColorString(String colorString) {
        this.titleBGColorString = colorString;
        if (titleBGColorString != null) {
            AppUtil.saveValue(mContext, KEY_ACTIONBAR_BG_COLOR, titleBGColorString);
        }
    }

    public String getTitleTextColorString() {
        if (titleTextColorString == null) {
            titleTextColorString = AppUtil.getValue(mContext, KEY_ACTIONBAR_TITLE_COLOR, "#FFFFFF");
        }
        return titleTextColorString;
    }

    public void setTitleTextColorString(String titleTextColorString) {
        this.titleTextColorString = titleTextColorString;
        if (titleTextColorString != null) {
            AppUtil.saveValue(mContext, KEY_ACTIONBAR_TITLE_COLOR, titleTextColorString);
        }
    }

    public int getTileBGColor() {
        return Color.parseColor(getTitleBGColorString());
    }

    public int getTileTextColor() {
        return Color.parseColor(getTitleTextColorString());
    }
}
