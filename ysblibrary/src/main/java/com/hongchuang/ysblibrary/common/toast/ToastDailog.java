package com.hongchuang.ysblibrary.common.toast;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.hongchuang.ysblibrary.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/***
 * 功能描述:Toast的替代，有些rom(小米，魅族)等在4.4以后，Toast也要开启了通知权限才能显示
 * 作者:qiujialiu
 * 时间:2017/4/24
 * 版本:1.0
 ***/

public class ToastDailog extends AlertDialog {
    private TextView mTextView;
    private String text;

    public ToastDailog(Context context) {
        super(context);
    }

    public ToastDailog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_toast);
    }

    public ToastDailog setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public void show() {
        super.show();
        mTextView = (TextView) findViewById(R.id.tv_toast_msg);
        mTextView.setText(text);
        Observable.timer(2000, TimeUnit.MILLISECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        dismiss();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismiss();
                    }
                });
    }
}
