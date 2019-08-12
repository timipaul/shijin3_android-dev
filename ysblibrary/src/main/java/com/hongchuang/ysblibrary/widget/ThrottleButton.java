package com.hongchuang.ysblibrary.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hongchuang.hclibrary.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/***
 * 功能描述:防止重复操作的Button
 * 作者:qiujialiu
 * 时间:2017/6/6
 ***/

public class ThrottleButton extends android.support.v7.widget.AppCompatButton implements ThrottleButtonControler.OnOperationListener {
    private boolean isStartLoad;

    public ThrottleButton(Context context) {
        this(context, null);
        init();
    }

    public ThrottleButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.borderlessButtonStyle);
        init();
    }

    public ThrottleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    private void init() {

    }

    public void setOnThrottleClickListener(@Nullable final OnClickListener l) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(getContext(), v);
                setEnabled(false);
                LogUtils.w("ThrottleButton", "onClick");
                ThrottleButtonControler.getInstance().addListener(ThrottleButton.this);
                if (l != null) {
                    l.onClick(v);
                }
                Observable.timer(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (!isStartLoad) {
                            setEnabled(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!isStartLoad) {
                            setEnabled(true);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.w("ThrottleButton is detached from window");
        ThrottleButtonControler.getInstance().removeListener(this);
    }

    @Override
    public void onOperationStart() {
        isStartLoad = true;
    }

    @Override
    public void onOperationEnd() {
        isStartLoad = false;
        setEnabled(true);
    }
}
