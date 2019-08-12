package com.hongchuang.ysblibrary.model.model;

//import com.tencent.tauth.UiError;

import org.json.JSONObject;

/***
 * 功能描述:QQ与微信回调
 * 作者:qiujialiu
 * 时间:2017/5/31
 ***/

public interface TencentCallback {

    void onCancel();

//    void onError(UiError error);

    /**
     * @param jsonObject "ret":0,
     *                   "pay_token":"xxxxxxxxxxxxxxxx",
     *                   "pf":"openmobile_android",
     *                   "expires_in":"7776000",
     *                   "openid":"xxxxxxxxxxxxxxxxxxx",
     *                   "pfkey":"xxxxxxxxxxxxxxxxxxx",
     *                   "msg":"sucess",
     *                   "access_token":"xxxxxxxxxxxxxxxxxxxxx"
     */
    void onComplete(JSONObject jsonObject);
}
