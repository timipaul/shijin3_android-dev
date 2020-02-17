package com.hongchuang.ysblibrary.model.model.tencent;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/6/3
 ***/

public class WeChatLoginResultBean {
    public TYPE type;
    public String code;
    public String errorCode;
    public String errorString;
    public String loginType;

    public enum TYPE {
        SUCCESS,
        CANCEL,
        REJECT,
        ERROR
    }
}
