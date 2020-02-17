package com.hongchuang.ysblibrary.common;

import retrofit.callback.FailOrExceptionType;

/***
 * 功能描述:应用全局监听器
 * 作者:qiujialiu
 * 时间:2017/11/21
 ***/

public interface GrobalListener {
    void requestFial(@FailOrExceptionType.FailType int type, int code);

    void networkLost();//网络断开
    void connectError(@FailOrExceptionType.FailType int type, String code);//错误信息

    void networkAvailable();//网络连接
}
