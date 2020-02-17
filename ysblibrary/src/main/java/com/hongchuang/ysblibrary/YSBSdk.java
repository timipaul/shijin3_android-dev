package com.hongchuang.ysblibrary;

import android.content.Context;
import com.hongchuang.ysblibrary.common.GrobalListener;


/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/11/21
 ***/

public class YSBSdk {
    public static long COMPANY_ID = 0;

    public static <T> T getService(Class<T> tClass) {
        return YSBLibrary.getLibrary().getService(tClass);
    }

    public static void init(Context context) {
        YSBLibrary.init(context);
    }

    public static void setCompanyId(long companyId) {
        COMPANY_ID = companyId;
    }


    /**
     * 初始化腾讯微信api
     *
     * @param appid
     */
    public static void initWeChat(String appid) {

        try {
//            YSBLibrary.getLibrary().initWeChat(appid);
        } catch (Exception e) {
            throw new RuntimeException("invoke HDSDK.init(app) befault init wechat library");
        }
    }

    /**
     * 初始化腾讯QQapi
     *
     * @param appid
     */
    public static void initTencent(String appid) {
        try {
//            YSBLibrary.getLibrary().initTencent(appid);
        } catch (Exception e) {
            throw new RuntimeException("invoke HDSDK.init(app) befault init tencent library");
        }
    }

    /**
     * 添加app要处理的回调
     *
     * @param listener
     */
    public static void addGrobalListener(GrobalListener listener) {
        try {
            YSBLibrary.getLibrary().addGrobalListener(listener);
        } catch (Exception e) {
            throw new RuntimeException("invoke HDSDK.init(app) befault set this listener");
        }
    }

    /**
     * 移除app要处理的回调
     *
     * @param listener
     */
    public static void removeGrobalListener(GrobalListener listener) {
        try {
            YSBLibrary.getLibrary().removeGrobalListener(listener);
        } catch (Exception e) {
            throw new RuntimeException("invoke HDSDK.init(app) befault set this listener");
        }
    }

//    public static void addCartCountChangeListener(CartCountChangeListener listener) {
//        try {
//            YSBLibrary.getLibrary().addCartCountChangeListener(listener);
//        } catch (Exception e) {
//            throw new RuntimeException("invoke HDSDK.init(app) befault set this listener");
//        }
//    }
//
//    public static void removeCartCountChangeListener(CartCountChangeListener listener) {
//        try {
//            YSBLibrary.getLibrary().removeCartCountChangeListener(listener);
//        } catch (Exception e) {
//            throw new RuntimeException("invoke HDSDK.init(app) befault set this listener");
//        }
//    }
}
