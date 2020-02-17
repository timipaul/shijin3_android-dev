package com.shijinsz.shijin.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.shijinsz.shijin.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by yrdan on 2018/9/27.
 */

public class HeaderView extends LinearLayout implements RefreshHeader {
    public HeaderView(Context context) {
        super(context);
        initView(context);
    }
    private GifImageView gifImageView;
    private ImageView successimg;

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        gifImageView = new GifImageView(context);
        successimg = new ImageView(context);
        successimg.setImageResource(R.mipmap.fresh_success);
        successimg.setVisibility(GONE);
        gifImageView.setImageResource(R.mipmap.freshing);
        gifImageView.setScaleType(ImageView.ScaleType.CENTER);
        GifDrawable gifDrawable=(GifDrawable) gifImageView.getDrawable();
        gifDrawable.setLoopCount(0);
        addView(successimg,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        addView(gifImageView, LayoutParams.MATCH_PARENT, DensityUtil.dp2px(80));
        setMinimumHeight(DensityUtil.dp2px(80));
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        gifImageView.setVisibility(VISIBLE);
        successimg.setVisibility(GONE);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        gifImageView.setVisibility(GONE);
        successimg.setVisibility(VISIBLE);
        return 500;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        gifImageView.setVisibility(VISIBLE);
        successimg.setVisibility(GONE);
    }
}
