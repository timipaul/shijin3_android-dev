package com.hongchuang.hclibrary.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/***
 * 功能描述:gridlayoutManager,捕获异常，解决recyclerview会崩溃
 * 作者:qiujialiu
 * 时间:2016/12/20
 * 版本:1.0
 ***/

public class YunGridLayoutManager extends GridLayoutManager {
    public YunGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public YunGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public YunGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
