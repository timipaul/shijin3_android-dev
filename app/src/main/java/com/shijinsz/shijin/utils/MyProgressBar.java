package com.shijinsz.shijin.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.shijinsz.shijin.R;

/**
 * Copyright (C)
 * FileName: MyProgressBar
 * Author: m1342
 * Date: 2019/6/26 15:15
 * Description: ${DESCRIPTION}
 */
public class MyProgressBar extends View {

    private Paint mPaint = null ;
    private int max=100;
    private int progress=0;
    private int type=1;
    private int color;
    private int NORMAL_TYPE=1;
    private int ALERT_TYPE=2;
    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyProgressBar); //与属性名称一致
        max = array.getInteger(R.styleable.MyProgressBar_max, 100);//第一个是传递参数，第二个是默认值
        progress = array.getInteger(R.styleable.MyProgressBar_progress, 0);
        type=array.getInt(R.styleable.MyProgressBar_type, 1);
        color = array.getColor(R.styleable.MyProgressBar_pro_color,Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point left_top=new Point(0,0);
        Point right_bottom=new Point(getWidth(),getHeight());
        double rate=(double)progress/(double)max;
        drawProgressBar(canvas, left_top, right_bottom, rate);
    }
    public void setProgress(int progress){
        this.progress=progress;
        invalidate();//使得onDraw重绘
    }

    public void setMax(int max){
        this.max = max;
        invalidate();//使得onDraw重绘
    }
    private void drawProgressBar(Canvas canvas,Point left_top,Point right_bottom,double rate){
        int width=1;
        int rad=10;
        //mPaint.setColor(Color.BLACK);//画边框
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mPaint.setStrokeWidth(width);
        RectF rectF = new RectF(left_top.x,left_top.y,right_bottom.x,right_bottom.y);
        canvas.drawRoundRect(rectF, rad, rad, mPaint);

        mPaint.setColor(color);//画progress bar

        if (type==ALERT_TYPE) {
            /*if (rate>0.9)
                mPaint.setColor(Color.RED);*/
        }
        mPaint.setStyle(Paint.Style.FILL);
        int x_end=(int)(right_bottom.x*rate);
        RectF rectF2 = new RectF(left_top.x+width,left_top.y+width,x_end-width,right_bottom.y-width);
        canvas.drawRoundRect(rectF2, rad, rad, mPaint);
    }
}
