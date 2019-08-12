package com.hongchuang.ysblibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hongchuang.ysblibrary.R;


public class NoticeDialog extends Dialog {
    Context context;
    int screenWidth;
    int screenHeight;
    private Window window = null;

    public NoticeDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void showDialog(View layout, int x, int y) {
        setContentView(layout);
        windowDeploy(x, y);
        Context context = getContext();
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(divierId);
        if (divider!=null)
        divider.setBackgroundColor(Color.TRANSPARENT);
//        setCanceledOnTouchOutside(false);
        show();
    }
    private int type=0;
    public void showDialog(View layout, int x, int y,int type) {
        setContentView(layout);
        this.type=type;
        Context context1 = getContext();
        int divierId = context1.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(divierId);
        if (divider!=null)
        divider.setBackgroundColor(Color.TRANSPARENT);
        windowDeploy(x, y);
//        setCanceledOnTouchOutside(false);
        show();
    }

    public void windowDeploy(int x, int y) {
        window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移
        if (type==1){
            wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        }else {
            wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
        }
//        wl.alpha = 0.6f; //设置透明度
        wl.gravity = Gravity.CENTER; //设置重力
        window.setAttributes(wl);
    }
}
