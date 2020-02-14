package com.shijinsz.shijin.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.storeUserBean;
import com.shijinsz.shijin.ui.user.LoginActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/9/21.
 */

public class LoginUtil {

    IWXAPI api;

    public static boolean isLogin(Context context){
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_login).equals("on")){
            return true;
        }else {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
    }

    //绑定微信
    public void isWxData(Context context){
        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_OPENID).equals("")){
            wxLoing(context);
        }
    }

    //判定微信
    private void wxLoing(Context context) {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        wechat.SSOSetting(false);
//                wechat.removeAccount(true);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                platform.getDb().exportData();
                Map<String, Object> map = new HashMap<>();
                map.put("username", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME));
                map.put("nickname", platform.getDb().getUserName());
                map.put("avatarUrl", platform.getDb().getUserIcon());
                map.put("openid", platform.getDb().getUserId());

                ShareDataManager.getInstance().save(context,SharedPreferencesKey.KEY_OPENID,platform.getDb().getUserId());
                //保存用户信息
                submitWxData(map);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                //ToastUtil.showToast("登录异常 onError");

            }

            @Override
            public void onCancel(Platform platform, int i) {
                //ToastUtil.showToast("登录异常 onCancel");

            }
        });
        wechat.authorize();
    }

    //保存个人信息
    private void submitWxData(Map<String, Object> map) {
        YSBSdk.getService(OAuthService.class).putBindWx(map, new YRequestCallback<storeUserBean>() {
            @Override
            public void onSuccess(storeUserBean var1) {

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
