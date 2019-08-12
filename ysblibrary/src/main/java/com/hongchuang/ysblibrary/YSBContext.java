package com.hongchuang.ysblibrary;

import android.content.Context;


/***
 * 功能描述:library的对象，方法拦截等管理类
 * 作者:qiujialiu
 * 时间:2017/5/27
 ***/

public class YSBContext {
    private static final String TAG = "HDContext";
    private static YSBContext library;
    private Context context;
    private OnCallBack callBack;

    public interface OnCallBack {
        void onNoLogin();
        void onconnectError();
    }

    private YSBContext() {
    }

    public static void init(Context context) {
        library = new YSBContext();
        library.context = context;
    }

    public void setCallBack(OnCallBack callBack) {
        this.callBack = callBack;
    }

    public static YSBContext getLibrary() {
        if (library == null) {
            throw new RuntimeException("please invoke HDSDK.init(app) before use this library");
        }
        return library;
    }

    public Context getContext() {
        return context;
    }

    public void setNoLogin() {
        if (callBack != null) {
            callBack.onNoLogin();
        }
    }

    public void setConnectError(){
        if (callBack != null) {
            callBack.onconnectError();
        }
    }
}
