package com.hongchuang.hclibrary.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.R;
import com.hongchuang.hclibrary.utils.LogUtils;
import com.hongchuang.hclibrary.utils.TextUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/***
 * 功能描述:加载中dialog
 * 作者:qiujialiu
 * 时间:2016-09-21
 * 版本:1.0
 ***/
public class LoadingDialog extends AlertDialog {
    private static Context mContext;
    private static LoadingDialog mDialog;
    //   private ImageView mImageViewRotate;
    private TextView mTextViewStatus;
    private LinearLayout mLinearLayout;
    private TextView mTextViewFail;
    private RotateAnimation animation;
    private String mContent;
    private boolean autoDiss;

    protected LoadingDialog(Context context) {
        super(context);
        init();
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    protected LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public static LoadingDialog Build(final Context context) throws IllegalThreadStateException {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalThreadStateException("only the original thread that created a mView hierarchy can touch its views");
        } else {
            LogUtils.e("LoadingDialog", "在主线程里。。");
            dismissDialog();
            mContext = context;
            mDialog = new LoadingDialog(mContext, R.style.LoadingDialog);
            LogUtils.e("LoadingDialog", "Dialog is null" + (mDialog == null));
            return mDialog;
        }


    }

    public static void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            LogUtils.w("LoadingDialog", "dismiss");
            mDialog.dismiss();
        }
        mContext = null;
        mDialog = null;
    }

    public static void dismissHint(String fail) {
        if (mDialog != null) {
            mDialog.showFail(fail);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.dismiss();
                }
            }, 2 * 1000);
        }
    }

    private void init() {
        mContent = null;
    }

    public LoadingDialog setContent(String content) {
        mContent = content;
        return mDialog;
    }

    public void showAutoDismiss() {
        autoDiss = true;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_loading_dialog);
        // mImageViewRotate = (ImageView) findViewById(R.id.iv_rotate);
        mTextViewStatus = (TextView) findViewById(R.id.tv_status);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_rotate);
        animation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
    }

    public void showFail(final String fail) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLinearLayout.setVisibility(View.GONE);
                if (TextUtil.isNotEmpty(fail)) {
                    mTextViewFail.setText(fail);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void show() {

        try {
            if (mDialog.isShowing() && ((Activity) mContext).getWindow() != null) {
                dismissDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mTextViewStatus != null) {
            if (TextUtil.isNotEmpty(mContent) && mContent.length() <= 6) {
                mTextViewStatus.setText(mContent);
            } else {
                mTextViewStatus.setText("加载中");
            }
        }
        setCanceledOnTouchOutside(false);
        if (autoDiss) {
            Observable.timer(20000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public void dismiss() {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Observable.create(new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        subscriber.onNext(null);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                LoadingDialog.super.dismiss();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                LoadingDialog.super.dismiss();
                            }
                        });

            } else {
                super.dismiss();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (animation != null) {

                        mLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
