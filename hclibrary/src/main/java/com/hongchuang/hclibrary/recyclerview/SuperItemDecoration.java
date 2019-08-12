//package com.hongchuang.hclibrary.recyclerview;
//
//import android.graphics.Rect;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import org.xutils.common.util.DensityUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
///***
// * 功能描述:RecyclerView的分隔线，支持横向和纵向，正向反向，
// * （以后:支持自定颜色，自定图片分隔）
// * 作者:qiujialiu
// * 时间:2017/2/28
// * 版本:1.0
// ***/
//
//public class SuperItemDecoration extends RecyclerView.ItemDecoration {
//    public static final int ORIENTATION_HORIZONTAL = 1;
//    public static final int ORIENTATION_VERTICAL = 2;
//    private int dividerSize = 10;
//    private int orientation = ORIENTATION_VERTICAL;
//    private Map<Integer, Boolean> ignoreMap;
//    private boolean reverseLayout = false;
//
//    private void setReverseLayout(boolean reverseLayout) {
//        this.reverseLayout = reverseLayout;
//    }
//
//    private int getDividerSize() {
//        return dividerSize;
//    }
//
//    private void setDividerSize(int dividerSize) {
//        this.dividerSize = dividerSize;
//    }
//
//    private void addIgnoreIndex(int index) {
//        if (ignoreMap == null) {
//            ignoreMap = new HashMap<>();
//        }
//        ignoreMap.put(index, true);
//    }
//
//    private boolean removeIgnoreIndex(int index) {
//        if (ignoreMap == null) {
//            return false;
//        }
//        return ignoreMap.remove(index);
//    }
//
//
//    private int getOrientation() {
//        return orientation;
//    }
//
//    private void setOrientation(int orientation) {
//        this.orientation = orientation;
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
//                               RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        int position = parent.getChildAdapterPosition(view);
//        if (ignoreMap != null && ignoreMap.get(position) != null && ignoreMap.get(position)) {
//            return;
//        }
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
//                        outRect.right = dividerSize;
//                        outRect.top = dividerSize;
//                    } else {
//                        outRect.bottom = dividerSize;
//                        outRect.right = dividerSize;
//                    }
//                } else {
//                    if (reverseLayout) {
//                        outRect.bottom = dividerSize;
//                        outRect.left = dividerSize;
//                    } else {
//                        outRect.bottom = dividerSize;
//                        outRect.right = dividerSize;
//                    }
//                }
//            }
//        }
//    }
//
//    public static class Builder {
//        private final SuperItemDecoration itemDecoration;
//
//        public Builder() {
//            itemDecoration = new SuperItemDecoration();
//        }
//
//        public SuperItemDecoration.Builder setOrientation(int orientation) {
//            itemDecoration.setOrientation(orientation);
//            return this;
//        }
//
//        public SuperItemDecoration.Builder setDividerSize(float sizeDp) {
//            itemDecoration.setDividerSize(DensityUtil.dip2px(sizeDp));
//            return this;
//        }
//
//        public SuperItemDecoration.Builder addNoDividerIndex(int index) {
//            itemDecoration.addIgnoreIndex(index);
//            return this;
//        }
//
//        public SuperItemDecoration.Builder reverseLayout(boolean reverse) {
//            itemDecoration.setReverseLayout(reverse);
//            return this;
//        }
//
//        public SuperItemDecoration bulid() {
//            return itemDecoration;
//        }
//    }
//}
