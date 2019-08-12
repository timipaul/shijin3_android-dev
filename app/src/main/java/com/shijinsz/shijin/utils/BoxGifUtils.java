package com.shijinsz.shijin.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by yrdan on 2018/9/7.
 */

public class BoxGifUtils {

    private NoticeDialog dialog;
    private RelativeLayout view;
    private TextView tv;
    private GifImageView img;
    private GifDrawable gif;
    private TranslateAnimation translateAnimation;
    public void showGif(Context mContext,String name){
        if (view==null){
            view= (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.box_gif_pop,null);
            dialog =new NoticeDialog(mContext);
            tv=view.findViewById(R.id.tv);
            img=view.findViewById(R.id.gif);
            gif=(GifDrawable)img.getDrawable();
        }
        tv.setText(String.format(mContext.getString(R.string.get),name));
        tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = tv.getMeasuredHeight();
        img.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height2 = img.getTop();
        translateAnimation=new TranslateAnimation(0,0,height,height-200);
        translateAnimation.setDuration(1500);
        gif.setLoopCount(1);
        gif.addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationCompleted(int loopNumber) {
                tv.setVisibility(View.VISIBLE);
                tv.setAnimation(translateAnimation);
                tv.startAnimation(translateAnimation);
            }
        });
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.showDialog(view, 0, 0,1);
    }
}
