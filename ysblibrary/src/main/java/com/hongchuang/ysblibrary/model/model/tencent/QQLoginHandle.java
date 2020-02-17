package com.hongchuang.ysblibrary.model.model.tencent;

/***
 * 功能描述:用于解决在某些低端机上调用登录后，由于内存紧张导致APP被系统回收，登录成功后无法成功回传数据。
 * 作者:qiujialiu
 * 时间:2017/6/26
 ***/

public interface QQLoginHandle {
    void setOnResultProcess(QQLoginResult result);
}
