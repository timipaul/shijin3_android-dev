package com.hongchuang.ysblibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.utils.LogUtils;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.common.GrobalListener;
import com.hongchuang.ysblibrary.model.model.OAuthServiceImp;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.tencent.tauth.Tencent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.NetWorkReceiver;
import retrofit.callback.FailOrExceptionType;


/***
 * 功能描述:library的对象，方法拦截等管理类
 * 作者:qiujialiu
 * 时间:2017/5/27
 ***/

public class YSBLibrary {
    private static final String TAG = "HDLibrary";
    private static YSBLibrary library;
    private Map<String, Object> serivceMap;
    private Context context;
//    private IWXAPI mWeChat;
//    private Tencent mTencent;
    private List<GrobalListener> listeners;
    //    private List<CartCountChangeListener> changeListeners;
    private String WXAppid;

    private YSBLibrary() {
        serivceMap = new HashMap<>();
        listeners = new ArrayList<>();
    }

    static void init(Context context) {
        library = new YSBLibrary();
        library.context = context;
        YSBContext.init(context);
        YSBContext.getLibrary().setCallBack(new YSBContext.OnCallBack() {
            @SuppressLint("WrongConstant")
            @Override
            public void onNoLogin() {
                library.sendRequestFial(401,1);
//                UserinfoUtil.deleteUserInfo();
            }

            @SuppressLint("WrongConstant")
            @Override
            public void onconnectError() {
                library.connectError(500,"服务器连接失败");

            }
        });
        ShareDataManager.getInstance().init(context, "app_data");
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(new NetWorkReceiver(), filter);
    }

    public static YSBLibrary getLibrary() {
        if (library == null) {
            throw new RuntimeException("please invoke HDSDK.init(app) before use this library");
        }
        return library;
    }

    public Context getContext() {
        return context;
    }

    <T> T getService(Class<T> tClass) {
        LogUtils.w("-----", tClass.getName());
        if (serivceMap.get(tClass.getName()) != null) {
            return (T) serivceMap.get(tClass.getName());
        }


        final T c = getServiceImpClass(tClass);

        final T result1 = (T) Proxy.newProxyInstance(c.getClass().getClassLoader(), c.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        LogUtils.w(TAG, "befault method " + method.getName());
                        Object obj = method.invoke(c, args);
                        LogUtils.w(TAG, "after method " + method.getName());
                        return obj;
                    }
                });
        serivceMap.put(tClass.getName(), result1);
        return result1;
    }

//    public IWXAPI getWeChat() {
//        return mWeChat;
//    }
//
//    public Tencent getTencent() {
//        return mTencent;
//    }

    public String getWeChatAppid() {
        return WXAppid;
    }

    private <T> T getServiceImpClass(Class<T> tClass) {
        if (TextUtil.equals(tClass.getName(), "com.hongchuang.ysblibrary.model.model.OAuthService")) {
            return (T) new OAuthServiceImp();
        }
//        if (TextUtil.equals(tClass.getName(), "com.hongchuang.ysblibrary.model.user.UploadService")) {
//            return (T) new UploadServiceImp();
//        }
        return null;
    }

//    void initWeChat(String appid) {
//        this.mWeChat = WXAPIFactory.createWXAPI(context, null);
//        this.mWeChat.registerApp(appid);
//        WXAppid = appid;
//        mWeChat.registerApp(appid);
//    }
//
//    void initTencent(String appid) {
//        this.mTencent = Tencent.createInstance(appid, context);
//    }
//

    void addGrobalListener(GrobalListener listener) {
        if (listeners!=null) {
            listeners.clear();
        }
            listeners.add(listener);
    }

//    public void onCartChange(int cartCount) {
//        if (changeListeners != null && changeListeners.size() > 0) {
//            for (CartCountChangeListener listener : changeListeners) {
//                listener.onChange(cartCount);
//            }
//        }
//    }

    void removeGrobalListener(GrobalListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void sendRequestFial(@FailOrExceptionType.FailType int failType, int code) {
        if (listeners != null && listeners.size() > 0) {
            for (GrobalListener listener : listeners) {
                listener.requestFial(failType, code);
            }
        }
    }

    public void lostNetwork() {
        if (listeners != null && listeners.size() > 0) {
            for (GrobalListener listener : listeners) {
                listener.networkLost();
            }
        }
    }


    public void connectError(@FailOrExceptionType.FailType int type, String code) {
        if (listeners != null && listeners.size() > 0) {
            for (GrobalListener listener : listeners) {
                listener.connectError(type,code);
            }
        }
    }

    public void availableNetwork() {
        if (listeners != null && listeners.size() > 0) {
            for (GrobalListener listener : listeners) {
                listener.networkAvailable();
            }
        }
    }

//    public void addCartCountChangeListener(CartCountChangeListener listener) {
//        if (listener == null)return;
//        if (changeListeners == null) {
//            changeListeners = new ArrayList<>();
//        }
//        changeListeners.add(listener);
//        listener.onChange(CartDbManager.getInstance().getCartProductCount());
//    }
//
//    public void removeCartCountChangeListener(CartCountChangeListener listener) {
//        if (changeListeners != null && listener != null) {
//            changeListeners.remove(listener);
//            if (changeListeners.size() == 0){
//                changeListeners = null;
//            }
//        }
//    }
}
