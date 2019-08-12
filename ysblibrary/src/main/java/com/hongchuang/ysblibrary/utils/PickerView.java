package com.hongchuang.ysblibrary.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.hongchuang.ysblibrary.R;
import com.hongchuang.ysblibrary.dao.AreaBean;
import com.hongchuang.ysblibrary.dao.CityBean;
import com.hongchuang.ysblibrary.dao.ProvinceBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author chenjing
 */
public class PickerView extends View {

    public static final String TAG = "PickerView";
    /**
     * text之间间距和minTextSize之比
     */
    public static final float MARGIN_ALPHA = 2.2f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 5;


    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint;

    private float mMaxTextSize = 80;
    private float mMinTextSize = 40;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;

    private int mColorText = 0xAD99A2;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private onSelectListener1 mSelectListener1;
    private onSelectListener2 mSelectListener2;
    private onSelectListener3 mSelectListener3;
    private List<String> mDataList;
    private List<ProvinceBean> mProvinceList;
    private List<CityBean> mCityList;
    private List<AreaBean> mAreaList;
    private Timer timer;
    private MyTimerTask mTask;
    private int TYPE;

    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            invalidate();
        }

    };

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    public void setOnSelectListener1(onSelectListener1 listener) {
        mSelectListener1 = listener;
    }

    public void setOnSelectListener2(onSelectListener2 listener) {
        mSelectListener2 = listener;
    }

    public void setOnSelectListener3(onSelectListener3 listener) {
        mSelectListener3 = listener;
    }

    public interface onSelectListener {
        void onSelect(String text, int point);
    }

    public interface onSelectListener1 {
        void onSelect(ProvinceBean pbean);
    }

    public interface onSelectListener2 {
        void onSelect(CityBean cbean);
    }

    public interface onSelectListener3 {
        void onSelect(AreaBean Abean);
    }


    private void performSelect() {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected), mCurrentSelected);
        if (mSelectListener1 != null)
            mSelectListener1.onSelect(mProvinceList.get(mCurrentSelected));
        if (mSelectListener2 != null) {
            if(mCityList.size()>0){
                mSelectListener2.onSelect(mCityList.get(mCurrentSelected));
            }else {
                mSelectListener2.onSelect(null);
            }
        }
        if (mSelectListener3 != null){
            if(mAreaList.size()>0){
                mSelectListener3.onSelect(mAreaList.get(mCurrentSelected));
            }else{
                mSelectListener3.onSelect(null);
            }
        }

    }

    /**
     * @param datas
     * @param index 初始位置
     */
    public void setData(List<String> datas, int index) {
        mDataList = datas;
        mCurrentSelected = index;
        invalidate();
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected), mCurrentSelected);
    }

    public void setPData(List<ProvinceBean> datas, int index) {
        if(null==datas){
            mDataList.add("");
            return;
        }
        List<String> data = new ArrayList<String>();
        mProvinceList = datas;
        for (ProvinceBean list : datas
                ) {
            if(list.getFNAME().length()>4){
                String fname=list.getFNAME().substring(0,4)+"...";
                data.add(fname);
            }else {
                data.add(list.getFNAME());
            }
        }
        mDataList = data;
        mCurrentSelected = index;
        if (mSelectListener1 != null)
            mSelectListener1.onSelect(mProvinceList.get(mCurrentSelected));
        invalidate();

    }

    public void setCData(List<CityBean> datas, int index) {
        if(null==datas){
            mCityList.clear();
            mDataList.clear();
            mDataList.add("");
            mCurrentSelected = index;
            invalidate();
            if (mSelectListener2 != null)
                mSelectListener2.onSelect(null);
            return;
        }
        List<String> data = new ArrayList<String>();
        mCityList = datas;
        for (CityBean list : datas
                ) {
            if(list.getFNAME().length()>4){
                String fname=list.getFNAME().substring(0,4)+"...";
                data.add(fname);
            }else {
                data.add(list.getFNAME());
            }
        }
        mDataList = data;
        mCurrentSelected = index;
        if (mSelectListener2 != null)
            mSelectListener2.onSelect(mCityList.get(mCurrentSelected));
        invalidate();

    }

    public void setAData(List<AreaBean> datas, int index) {
        if(null==datas){
            mAreaList.clear();
            mDataList.clear();
            mDataList.add("");
            mCurrentSelected = index;
            invalidate();
            if (mSelectListener3 != null)
                mSelectListener3.onSelect(null);
            return;
        }
        List<String> data = new ArrayList<String>();
        mAreaList = datas;
        for (AreaBean list : datas
                ) {
            if(list.getFNAME().length()>4){
                String fname=list.getFNAME().substring(0,4)+"...";
                data.add(fname);
            }else {
                data.add(list.getFNAME());
            }
        }
        mDataList = data;
        mCurrentSelected = index;
        if (mSelectListener3 != null)
            mSelectListener3.onSelect(mAreaList.get(mCurrentSelected));
        invalidate();

    }

    public void setSelected(int selected) {
        mCurrentSelected = selected;
    }

    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
        if (mProvinceList.size()>0) {
            ProvinceBean pbean = mProvinceList.get(0);
            mProvinceList.remove(0);
            mProvinceList.add(pbean);
        }
        if(mCityList.size()>0) {
            CityBean cbean = mCityList.get(0);
            mCityList.remove(0);
            mCityList.add(cbean);
        }
        if (mAreaList.size()>0){
            AreaBean abean = mAreaList.get(0);
            mAreaList.remove(0);
            mAreaList.add(abean);
        }
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
        if (mProvinceList.size()>0) {
            ProvinceBean pbean = mProvinceList.get(mProvinceList.size()-1);
            mProvinceList.remove(mProvinceList.size()-1);
            mProvinceList.add(0, pbean);
        }
        if(mCityList.size()>0) {
            CityBean cbean = mCityList.get(mCityList.size()-1);
            mCityList.remove(mCityList.size()-1);
            mCityList.add(0,cbean);
        }
        if (mAreaList.size()>0){
            AreaBean abean = mAreaList.get(mAreaList.size()-1);
            mAreaList.remove(mAreaList.size()-1);
            mAreaList.add(0,abean);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 4.0f;
        mMinTextSize = mMaxTextSize / 2f;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<String>();
        mProvinceList = new ArrayList<ProvinceBean>();
        mAreaList = new ArrayList<AreaBean>();
        mCityList = new ArrayList<CityBean>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;

        mPaint.setColor(getResources().getColor(R.color.text_33));
        mPaint.setTextSize(getResources().getDimension(R.dimen.sp_16));
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));


        if (mDataList.size() > 0) {
            canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        }else {
            canvas.drawText("",x,baseline,mPaint);
        }
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }

    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
                * mMoveLen);
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setColor(mColorText);
        mPaint.setTextSize(getResources().getDimension(R.dimen.sp_16));
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        if (mDataList.size() > 0) {
            canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                    (float) (mViewWidth / 2.0), baseline, mPaint);
        }
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {

        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }

    }


}
