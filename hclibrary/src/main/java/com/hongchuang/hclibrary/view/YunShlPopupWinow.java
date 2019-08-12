package com.hongchuang.hclibrary.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.hongchuang.hclibrary.R;


/***
 * 功能描述:PopupWindow，增加显示和移除的监听回调，
 *           解决7.0showAsDropDown的显示问题
 * 作者:qiujialiu
 * 时间:2017/6/8
 ***/

public class YunShlPopupWinow extends PopupWindow {
    private IShowListener mListener;
    private Activity context;

    public YunShlPopupWinow(Context context) {
        super(context);
    }

    public YunShlPopupWinow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YunShlPopupWinow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public YunShlPopupWinow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public YunShlPopupWinow() {
    }

    public YunShlPopupWinow(View contentView) {
        super(contentView);
    }

    public YunShlPopupWinow(int width, int height) {
        super(width, height);
    }

    public YunShlPopupWinow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public YunShlPopupWinow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public void setShowListener(IShowListener listener) {
        this.mListener = listener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mListener != null) {
            mListener.onDismiss();
        }
        backgroundAlpha(context, 1);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            // 适配 android 7.0
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            super.showAtLocation(anchor, gravity, xoff, y + getHeight() + yoff);
        }
        if (mListener != null) {
            mListener.onShow();
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor, xoff, yoff);
        } else {
            // 适配 android 7.0
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, xoff, y + getHeight() + yoff);
        }
        if (mListener != null) {
            mListener.onShow();
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor, 0, 0);
        } else {
            // 适配 android 7.0
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y + anchor.getHeight());
        }
        if (mListener != null) {
            mListener.onShow();
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        super.showAtLocation(parent, gravity, x, y);
        if (mListener != null) {
            mListener.onShow();
        }
    }

    public YunShlPopupWinow setCommonConfig(Activity context) {
        setFocusable(true);// 取得焦点
        setAnimationStyle(R.style.dialog_topleft_bottomright_animation);
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        setOutsideTouchable(true);
        //设置可以点击
        setTouchable(true);
        this.context = context;
        return this;
    }

    public YunShlPopupWinow setCommonConfig(Activity context, AnimaDirect direct) {
        return setConfig(context, direct, true);
    }

    public YunShlPopupWinow setCommonConfig(Activity context, AnimaDirect direct, boolean transBg) {
        return setConfig(context, direct, transBg);
    }

    public YunShlPopupWinow setAnimation(Activity context, boolean show, int style) {
        setFocusable(true);// 取得焦点
        setAnimationStyle(style);
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        if (show) {
            backgroundAlpha(context, 0.4f);//0.0-1.0
        }
        //设置可以点击
        setTouchable(true);
        this.context = context;
        return this;
    }

    @NonNull
    private YunShlPopupWinow setConfig(Activity context, AnimaDirect direct, boolean show) {
        setFocusable(true);// 取得焦点
        if (direct == AnimaDirect.DIRECT_BOTTOM_TOP) {
            setAnimationStyle(R.style.dialog_bottom_top_animation);
        } else if (direct == AnimaDirect.DIRECT_RIGHTTOP_LEFTBOTTOM) {
            setAnimationStyle(R.style.dialog_topright_bottomleft_animation);
        } else if (direct == AnimaDirect.DIRECT_TOP_BOTTON) {
            setAnimationStyle(R.style.dialog_top_bottom_animation);
        } else if (direct == AnimaDirect.DIRECT_RIGHT_LEFT) {
            setAnimationStyle(R.style.dialog_right_left_animation);
        }
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        if (show) {
            backgroundAlpha(context, 0.4f);//0.0-1.0
        }
        //设置可以点击
        setTouchable(true);
        this.context = context;
        return this;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(Activity context, float bgAlpha) {
        if (context == null) return;
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        //context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    public enum AnimaDirect {
        DIRECT_TOP_BOTTON, DIRECT_LEFT_RIGHT,
        DIRECT_RIGHT_LEFT, DIRECT_BOTTOM_TOP,
        DIRECT_LEFTTOP_RIGHTBOTTOM,
        DIRECT_LEFTBOTTOM_RIGHTTOP,
        DIRECT_RIGHTTOP_LEFTBOTTOM,
        DIRECT_RIGHTBOTTOM_LEFTTOP
    }

    public interface IShowListener {
        void onShow();

        void onDismiss();
    }

}
