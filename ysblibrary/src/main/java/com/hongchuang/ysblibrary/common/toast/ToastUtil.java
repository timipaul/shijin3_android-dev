package com.hongchuang.ysblibrary.common.toast;


import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.widget.Toast;

import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.YSBLibrary;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2016/12/19
 * 版本:
 * ===================
 ***/
public class ToastUtil {
    private static long lastShowTime;
    private static String lastMessage;

    public static void showToast(final String msg) {
        if (TextUtil.isEmpty(msg)) return;
        showToast(msg, Toast.LENGTH_LONG);
    }

    public static void showToast(final String msg, final int duration) {
        if (TextUtil.isEmpty(msg)) return;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (msg != null) {
                    subscriber.onNext(msg);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                        if (TextUtil.equals(lastMessage, msg)) {
                            if (System.currentTimeMillis() - lastShowTime > 3000) {
//                                if (haveNoPermission()) {
//                                    showDailogToast(msg);
//                                } else {
                                    Toast.makeText(YSBLibrary.getLibrary().getContext(), msg, duration).show();
//                                }
                                lastShowTime = System.currentTimeMillis();
                                lastMessage = msg;
                            }
                        } else {
//                            if (haveNoPermission()) {
                                showDailogToast(msg);
//                            } else {
//                                Toast.makeText(YSBLibrary.getLibrary().getContext(), msg, duration).show();
//                            }
                            lastShowTime = System.currentTimeMillis();
                            lastMessage = msg;
                        }
                    }
                });
    }

    public static void showToastOnUI(String msg) {
        if (TextUtil.isEmpty(msg)) return;
        if (TextUtil.equals(lastMessage, msg)) {
            if (System.currentTimeMillis() - lastShowTime > 3000) {
//                if (haveNoPermission()) {
                    showDailogToast(msg);
//                } else {
//                    Toast.makeText(YSBLibrary.getLibrary().getContext(), msg, Toast.LENGTH_LONG).show();
//                }
                lastShowTime = System.currentTimeMillis();
                lastMessage = msg;
            }
        } else {
//            if (haveNoPermission()) {
//                showDailogToast(msg);
//            } else {
                Toast.makeText(YSBLibrary.getLibrary().getContext(), msg, Toast.LENGTH_LONG).show();
//            }
            lastShowTime = System.currentTimeMillis();
            lastMessage = msg;
        }
    }

    private static boolean haveNoPermission() {
        if (OsInfoUtil.isMIUI() || OsInfoUtil.checkIsMeizuRom() || OsInfoUtil.checkIsHuaweiRom()) {
            if (Build.VERSION.SDK_INT >= 19) {
                return !isNotificationEnabled(YSBLibrary.getLibrary().getContext());
            }
        }
        return false;
    }

    public static void showIOExceptionMessage(final Exception e) {
        if (e instanceof ConnectException || e instanceof ConnectTimeoutException
                || e instanceof SocketTimeoutException || e instanceof SocketException ||
                e instanceof UnknownHostException) {
            showToast("" + e.getMessage());
        }
    }

    private static void showDailogToast(String msg) {
//        if (HuiDengApplication.getInstance() != null && HuiDengApplication.getInstance().getTopActivity() != null) {
//            ToastManager.getInstance().showToast(msg);
//        }
        Toast.makeText(YSBLibrary.getLibrary().getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showErrorToast(String interfaces, String code, String msg) {
        Toast.makeText(YSBLibrary.getLibrary().getContext(),  msg, Toast.LENGTH_LONG).show();
//        Toast.makeText(YSBLibrary.getLibrary().getContext(), "interface=" + interfaces + ",code=" + code + ",msg=" + msg, Toast.LENGTH_LONG).show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        final String CHECK_OP_NO_THROW = "checkOpNoThrow";
        final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

}
