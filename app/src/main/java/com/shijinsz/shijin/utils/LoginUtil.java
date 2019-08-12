package com.shijinsz.shijin.utils;

import android.content.Context;
import android.content.Intent;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.shijinsz.shijin.ui.user.LoginActivity;

/**
 * Created by yrdan on 2018/9/21.
 */

public class LoginUtil {
    public static boolean isLogin(Context context){
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_login).equals("on")){
            return true;
        }else {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
    }
}
