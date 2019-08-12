//package com.hongchuang.hclibrary.recyclerview;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.view.View;
//
//import com.hongchuang.hclibrary.utils.DevicesUtil;
//
////import org.xutils.common.util.DensityUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
///***
// * 功能描述:
// * 作者:qiujialiu
// * 时间:2017/8/14
// ***/
//
//public class SuperItemDecorationPlus extends RecyclerView.ItemDecoration {
//    public static final int ORIENTATION_HORIZONTAL = 1;
//    public static final int ORIENTATION_VERTICAL = 2;
//    private int dividerSize = 10;
//    private int orientation = ORIENTATION_VERTICAL;
//    private Map<Integer, Boolean> ignoreMap;
//    private boolean reverseLayout = false;
//    private int dividerColor;
//
//    private Context mContext;
//    private Paint mPaint;
//
//    public SuperItemDecorationPlus(Context context) {
//        this.mContext = context;
//
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.FILL);
//    }
//
//    public void setDividerSize(int dividerSize) {
//        this.dividerSize = dividerSize;
//    }
//
//    private void setReverseLayout(boolean reverseLayout) {
//        this.reverseLayout = reverseLayout;
//    }
//
//    public void setOrientation(int orientation) {
//        this.orientation = orientation;
//    }
//
//    private void addIgnoreIndex(int index) {
//        if (ignoreMap == null) {
//            ignoreMap = new HashMap<>();
//        }
//        ignoreMap.put(index, true);
//    }
//
//    public void setDividerColor(int dividerColor) {
//        this.dividerColor = dividerColor;
//        mPaint.setColor(dividerColor);
//    }
//
//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        if (dividerColor == 0) {
//            dividerColor = Color.parseColor("#FFE3E3E3");
//            mPaint.setColor(dividerColor);
//        }
//        if (parent.getLayoutManager() != null) {
//            if (parent.getLayoutManager() instanceof LinearLayoutManager && !(parent.getLayoutManager() instanceof GridLayoutManager)) {
//                if (orientation == ORIENTATION_HORIZONTAL) {
//                    drawHorizontalLinear(c, parent);
//                } else {
//                    drawVerticalLiner(c, parent);
//                }
//            } else {
//                drawHorizontal(c, parent);
//                drawVertical(c, parent);
//            }
//        }
//    }
//
//    private void drawHorizontalLinear(Canvas c, RecyclerView parent) {
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            if (ignoreMap != null && ignoreMap.get(i) != null && ignoreMap.get(i)) {
//                return;
//            }
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//
//            if (reverseLayout) {
//                final int left = child.getLeft() - params.leftMargin;
//                final int right = child.getRight() + params.rightMargin
//                        + dividerSize;
//                final int top = child.getTop() - params.topMargin - dividerSize;
//                final int bottom = top + dividerSize;
//                c.drawRect(left, top, right, bottom, mPaint);
//            } else {
//                final int left = child.getLeft() - params.leftMargin;
//                final int right = child.getRight() + params.rightMargin
//                        + dividerSize;
//                final int top = child.getBottom() + params.bottomMargin;
//                final int bottom = top + dividerSize;
//                c.drawRect(left, top, right, bottom, mPaint);
//            }
//
//        }
//    }
//
//    private int getSpanCount(RecyclerView parent) {
//        // 列数
//        int spanCount = -1;
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//
//            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            spanCount = ((StaggeredGridLayoutManager) layoutManager)
//                    .getSpanCount();
//        }
//        return spanCount;
//    }
//
//    public void drawHorizontal(Canvas c, RecyclerView parent) {
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            if (ignoreMap != null && ignoreMap.get(i) != null && ignoreMap.get(i)) {
//                return;
//            }
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//
//            final int left = child.getLeft() - params.leftMargin;
//            final int right = child.getRight() + params.rightMargin
//                    + dividerSize;
//            final int top = child.getBottom() + params.bottomMargin;
//            final int bottom = top + dividerSize;
//            c.drawRect(left, top, right, bottom, mPaint);
//
//        }
//    }
//
//    public void drawVertical(Canvas c, RecyclerView parent) {
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            if (ignoreMap != null && ignoreMap.get(i) != null && ignoreMap.get(i)) {
//                return;
//            }
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//            if (!reverseLayout) {
//                final int top = child.getTop() - params.topMargin;
//                final int bottom = child.getBottom() + params.bottomMargin;
//                final int left = child.getRight() + params.rightMargin;
//                final int right = left + dividerSize;
//
//                c.drawRect(left, top, right, bottom, mPaint);
//            } else {
//                final int top = child.getTop() - params.topMargin;
//                final int bottom = child.getBottom() + params.bottomMargin;
//                final int left = child.getLeft() - params.leftMargin - dividerSize;
//                final int right = left + dividerSize;
//
//                c.drawRect(left, top, right, bottom, mPaint);
//            }
//        }
//    }
//
//    private void drawVerticalLiner(Canvas c, RecyclerView parent) {
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            if (ignoreMap != null && ignoreMap.get(i) != null && ignoreMap.get(i)) {
//                return;
//            }
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//            if (!reverseLayout) {
//                final int top = child.getTop() - params.topMargin;
//                final int bottom = child.getBottom() + params.bottomMargin;
//                final int left = child.getRight() + params.rightMargin;
//                final int right = left + dividerSize;
//
//                c.drawRect(left, top, right, bottom, mPaint);
//            } else {
//                final int top = child.getTop() - params.topMargin;
//                final int bottom = child.getBottom() + params.bottomMargin;
//                final int left = child.getLeft() - params.leftMargin - dividerSize;
//                final int right = left + dividerSize;
//
//                c.drawRect(left, top, right, bottom, mPaint);
//            }
//        }
//    }
//
//    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
//                                int childCount) {
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
//            {
//                return true;
//            }
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            int orientation = ((StaggeredGridLayoutManager) layoutManager)
//                    .getOrientation();
//            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
//                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
//                {
//                    return true;
//                }
//            } else {
//                childCount = childCount - childCount % spanCount;
//                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
//                              int childCount) {
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            childCount = childCount - childCount % spanCount;
//            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
//                return true;
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            int orientation = ((StaggeredGridLayoutManager) layoutManager)
//                    .getOrientation();
//            // StaggeredGridLayoutManager 且纵向滚动
//            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
//                childCount = childCount - childCount % spanCount;
//                // 如果是最后一行，则不需要绘制底部
//                if (pos >= childCount)
//                    return true;
//            } else
//            // StaggeredGridLayoutManager 且横向滚动
//            {
//                // 如果是最后一行，则不需要绘制底部
//                if ((pos + 1) % spanCount == 0) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, int itemPosition,
//                               RecyclerView parent) {
//        if (ignoreMap != null && ignoreMap.get(itemPosition) != null && ignoreMap.get(itemPosition)) {
//            return;
//        }
//        int spanCount = getSpanCount(parent);
//        int childCount = parent.getAdapter().getItemCount();
//        if (parent.getLayoutManager() != null) {
//            if (parent.getLayoutManager() instanceof LinearLayoutManager && !(parent.getLayoutManager() instanceof GridLayoutManager)) {
//                if (orientation == ORIENTATION_VERTICAL) {
//                    if (reverseLayout) {
//                        outRect.top = dividerSize;
//                    } else {
//                        outRect.bottom = dividerSize;
//                    }
//                } else {
//                    if (reverseLayout) {
//                        outRect.left = dividerSize;
//                    } else {
//                        outRect.right = dividerSize;
//                    }
//                }
//            } else {
//                if (orientation == ORIENTATION_VERTICAL) {
//                    if (reverseLayout) {
//                        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
//                        {
//                            outRect.right = dividerSize;
//                        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
//                        {
//                            outRect.top = dividerSize;
//                        } else {
//                            outRect.right = dividerSize;
//                            outRect.top = dividerSize;
//                        }
//                    } else {
//                        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
//                        {
//                            outRect.right = dividerSize;
//                        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
//                        {
//                            outRect.bottom = dividerSize;
//                        } else {
//                            outRect.right = dividerSize;
//                            outRect.bottom = dividerSize;
//                        }
//                    }
//                } else {
//                    if (reverseLayout) {
//                        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
//                        {
//                            outRect.left = dividerSize;
//                        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
//                        {
//                            outRect.bottom = dividerSize;
//                        } else {
//                            outRect.left = dividerSize;
//                            outRect.bottom = dividerSize;
//                        }
//                    } else {
//                        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
//                        {
//                            outRect.right = dividerSize;
//                        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
//                        {
//                            outRect.bottom = dividerSize;
//                        } else {
//                            outRect.right = dividerSize;
//                            outRect.bottom = dividerSize;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    public static class Builder {
//        private final SuperItemDecorationPlus itemDecoration;
//
//        public Builder(Context context) {
//            itemDecoration = new SuperItemDecorationPlus(context);
//        }
//
//        public SuperItemDecorationPlus.Builder setOrientation(int orientation) {
//            itemDecoration.setOrientation(orientation);
//            return this;
//        }
//
////        public SuperItemDecorationPlus.Builder setDividerSize(float sizeDp) {
////            itemDecoration.setDividerSize(DevicesUtil.dip2px(sizeDp));
////            return this;
////        }
//
//        public SuperItemDecorationPlus.Builder addNoDividerIndex(int index) {
//            itemDecoration.addIgnoreIndex(index);
//            return this;
//        }
//
//        public SuperItemDecorationPlus.Builder reverseLayout(boolean reverse) {
//            itemDecoration.setReverseLayout(reverse);
//            return this;
//        }
//
//        public SuperItemDecorationPlus.Builder setColor(int color) {
//            itemDecoration.setDividerColor(color);
//            return this;
//        }
//
//        public SuperItemDecorationPlus bulid() {
//            return itemDecoration;
//        }
//    }
//
//}
