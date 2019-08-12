package com.hongchuang.ysblibrary.widget;


import com.hongchuang.hclibrary.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.Network;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/6/6
 ***/

public class ThrottleButtonControler {
    private static ThrottleButtonControler instance;
    private List<MListener> listeners;
    private MListener currentListener;

    private ThrottleButtonControler() {
        listeners = new ArrayList<>();
        Network.addOnRequestStatusListener(new Network.ReqStatusListener() {
            @Override
            public void onStart(String url) {
                //LogUtils.w("ThrottleButtonControler" + "onStart : " + url);
                if (currentListener != null) {
                    listeners.add(currentListener);
                }
                if (listeners != null && listeners.size() > 0) {
                    for (MListener listener : listeners) {
                        if (TextUtil.equals(listener.url, url) && listener.listener != null) {
                            listener.listener.onOperationStart();
                        }
                    }
                }
                currentListener = null;
            }

            @Override
            public void onComplete(String url) {
                //LogUtils.w("ThrottleButtonControler" + "onComplete : " + url);
                if (listeners != null && listeners.size() > 0) {
                    for (MListener listener : listeners) {
                        if (TextUtil.equals(listener.url, url) && listener.listener != null) {
                            listener.listener.onOperationEnd();
                        }
                    }
                }
            }

            @Override
            public void onError(String url) {
                //LogUtils.w("ThrottleButtonControler" + "onError : " + url);
                if (listeners != null && listeners.size() > 0) {
                    for (MListener listener : listeners) {
                        if (TextUtil.equals(listener.url, url) && listener.listener != null) {
                            listener.listener.onOperationEnd();
                        }
                    }
                }
            }
        });
    }

    public static synchronized ThrottleButtonControler getInstance() {
        if (instance == null) {
            instance = new ThrottleButtonControler();
        }
        return instance;
    }

    public void addListener(OnOperationListener listener) {
        currentListener = new MListener();
        currentListener.listener = listener;
    }

    public void removeListener(OnOperationListener listener) {
        if (listeners != null && listeners.size() > 0) {
            for (int i = 0; i < listeners.size(); i++) {
                if (listener != null && listeners.get(i).listener == listener) {
                    listeners.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    public interface OnOperationListener {
        void onOperationStart();

        void onOperationEnd();
    }

    class MListener {
        String url;
        OnOperationListener listener;
    }

}
