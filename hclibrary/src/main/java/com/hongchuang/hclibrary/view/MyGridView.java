package com.hongchuang.hclibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Administrator on 2018/7/27.
 */

public class MyGridView extends GridView {

        boolean expanded = true;

        public boolean isExpanded() {
            return expanded;
        }

        public MyGridView(Context context) {
            super(context);
        }

        public MyGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyGridView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (isExpanded()) {
                int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandSpec);
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = getMeasuredHeight();
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

}
