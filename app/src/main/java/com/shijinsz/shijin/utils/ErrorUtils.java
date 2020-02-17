package com.shijinsz.shijin.utils;

import android.content.Context;
import android.content.Intent;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.shijinsz.shijin.ui.user.LoginActivity;

import retrofit.ToKenUtil;

/**
 * Created by yrdan on 2018/9/29.
 */

public class ErrorUtils {
    public static void error(Context context,String code,String message){
        if (code.equals("400")){
            ToastUtil.showToast(message);
        }else if (code.equals("401")){
            ToastUtil.showToast("未登录或登录信息过期");
            ToKenUtil.deleteToken();
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_nickname);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_OPENID);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_qqid);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_invitation_code);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_is_advertiser);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_new_one_status);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_gender);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_age);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_birth);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_income);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_job);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_nickname_update_number);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_imageurl);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_points);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_address);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_change);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_interest);
            ShareDataManager.getInstance().delete(context, SharedPreferencesKey.KEY_is_login);
            context.startActivity(new Intent(context, LoginActivity.class));
        }else if (code.equals("403")){
            ToastUtil.showToast(message);
        }else if (code.equals("404")){
            ToastUtil.showToast(message);
        }else if (code.equals("405")){
            ToastUtil.showToast("账号异常或已经被禁用");
        }else {
            ToastUtil.showToast("系统出了一点小差，请稍后再试");
        }
    }
}
