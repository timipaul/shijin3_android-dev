package com.hongchuang.hclibrary.view.banner.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class BannerViewPager extends ViewPager {
    private boolean scrollable = true;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.scrollable) {
            return !(getCurrentItem() == 0 && getChildCount() == 0) && super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.scrollable) {
            return !(getCurrentItem() == 0 && getChildCount() == 0) && super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
