package com.hongchuang.ysblibrary.model.model.tencent;

import com.hongchuang.hclibrary.storage.LogManager;
import com.hongchuang.ysblibrary.YSBLibrary;
//import com.tencent.connect.UserInfo;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.callback.YRequestCallback;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/5/31
 ***/

public class BaseUiListener{
//    private YRequestCallback callback;
//
//    public BaseUiListener(YRequestCallback<QQLoginResultBean> callback) {
//        this.callback = callback;
//    }
//
//    public BaseUiListener() {
//    }
//
//    @Override
//    public void onComplete(Object response) {
//        if (null == response) {
//            LogManager.getInstance().w("OAuthService", "QQ登录失败,返回为空");
//            if (callback != null) {
//                callback.onFailed("", "QQ登录失败");
//            }
//            return;
//        }
//        final JSONObject jsonResponse = (JSONObject) response;
//        if (null != jsonResponse && jsonResponse.length() == 0) {
//            LogManager.getInstance().w("OAuthService", "QQ登录失败,返回为空");
//            if (callback != null) {
//                callback.onFailed("", "QQ登录失败");
//            }
//            return;
//        }
//        LogManager.getInstance().w("OAuthService", "QQ登录成功");
//        if (callback != null) {
//            try {
//                YSBLibrary.getLibrary().getTencent().getQQToken().setAccessToken(jsonResponse.getString("access_token"),
//                        jsonResponse.getString("expires_in"));
//                YSBLibrary.getLibrary().getTencent().getQQToken().setOpenId(jsonResponse.getString("openid"));
//                UserInfo userInfo = new UserInfo(YSBLibrary.getLibrary().getContext(), YSBLibrary.getLibrary().getTencent().getQQToken());
//                userInfo.getUserInfo(new IUiListener() {
//                    @Override
//                    public void onComplete(Object o) {
//                        LogManager.getInstance().w("OAuthService", "获取QQ信息成功");
//                        QQLoginResultBean resultBean = new QQLoginResultBean();
//                        JSONObject object = (JSONObject) o;
//                        try {
//                            resultBean.setAvatar(object.getString("figureurl_qq_1"));
//                            resultBean.setNickName(object.getString("nickname"));
//                            resultBean.setOpenId(jsonResponse.getString("openid"));
//                            if (callback != null) {
//                                callback.onSuccess(resultBean);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            if (callback != null) {
//                                callback.onFailed("", "登录失败,获取用户信息失败");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(UiError uiError) {
//                        LogManager.getInstance().w("OAuthService", "获取QQ信息出错 : " + uiError.errorDetail);
//                        if (callback != null) {
//                            callback.onFailed("", "登录失败");
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//            } catch (Exception e) {
//                LogManager.getInstance().w("OAuthService", "获取QQ信息出错 : " + e.getMessage());
//                e.printStackTrace();
//                if (callback != null) {
//                    callback.onFailed("", "登录失败");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onError(UiError e) {
//        LogManager.getInstance().w("OAuthService", "QQ登录出错" + e.errorDetail);
//        if (callback != null) {
//            callback.onFailed("", "QQ登录出错");
//        }
//    }
//
//    @Override
//    public void onCancel() {
//        LogManager.getInstance().w("OAuthService", "已取消授权");
//        if (callback != null) {
//            callback.onFailed("", "已取消授权");
//        }
//    }
}
