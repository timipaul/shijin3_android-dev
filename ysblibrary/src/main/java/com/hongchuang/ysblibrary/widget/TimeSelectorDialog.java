package com.hongchuang.ysblibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.utils.LogUtils;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.YunShlPopupWinow;
import com.hongchuang.ysblibrary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/***
 * 功能描述:时间选择器
 * 作者:J
 * 时间:2016/9/1
 * 版本:
 ***/
public class TimeSelectorDialog {
    private Context mContext;
    private View mView;
    private YunShlPopupWinow mPopupWindow;
    private CycleWheelView mCycleWheelViewYear;
    private CycleWheelView mCycleWheelViewMouth;
    private CycleWheelView mCycleWheelViewDay;
    private TextView mTextViewCancel;
    private TextView mTextVeiwSure;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private LinearLayout mLayoutStartTime;
    private LinearLayout mLayoutEndTime;
    private View viewLineStart;
    private View viewLineEnd;
    private int currentSelect = 1;
    private BindClickListener mBindClickListener;
    private boolean tenMinute;//分钟以10分为每个单位
    private List<Integer> yearList;
    private List<Integer> mouthList;
    private List<Integer> dayList;
    private Calendar currentCalender = Calendar.getInstance();

    private long currentStartTime;
    private long currentEndTime;

    private int currentYearI;
    private int currentMonthI;
    private int currentDayI;


    public TimeSelectorDialog(Context mContext) {
        this.mContext = mContext;
        init();
    }

    public TimeSelectorDialog(Context context, boolean tenMinute) {
        this.tenMinute = tenMinute;
        this.mContext = context;
        init();
    }


    private void init() {
        currentStartTime = System.currentTimeMillis();
        currentEndTime = currentStartTime + (1000 * 60 * 60 * 24);
        currentCalender.setTimeInMillis(currentStartTime);
        currentYearI = currentCalender.get(Calendar.YEAR);
        currentMonthI = currentCalender.get(Calendar.MONTH) + 1;
        currentDayI = currentCalender.get(Calendar.DAY_OF_MONTH);
    }

    public TimeSelectorDialog setBindClickListener(BindClickListener mBindClickListener) {
        this.mBindClickListener = mBindClickListener;
        return this;
    }

    public void setTime(String timeStr) {
        if (TextUtil.isNotEmpty(timeStr)) {
            if (timeStr.matches("[\\d]{4}\\.[\\d]{2}\\.[\\d]{2}") ||
                    timeStr.matches("[\\d]{4}\\.[\\d]{2}\\.[\\d]{2}-[\\d]{4}\\.[\\d]{2}\\.[\\d]{2}")) {
                if (timeStr.contains("-")) {
                    String[] strings = timeStr.split("-");
                    if (strings != null && strings.length > 1) {
                        currentStartTime = TimeUtil.parseMillsTime(strings[0], "yyyy.MM.dd");
                        currentCalender.setTimeInMillis(currentStartTime);
                        currentYearI = currentCalender.get(Calendar.YEAR);
                        currentMonthI = currentCalender.get(Calendar.MONTH) + 1;
                        currentDayI = currentCalender.get(Calendar.DAY_OF_MONTH);
                        currentEndTime = TimeUtil.parseMillsTime(strings[1], "yyyy.MM.dd");
                    }
                } else {
                    currentStartTime = TimeUtil.parseMillsTime(timeStr, "yyyy.MM.dd");
                    currentCalender.setTimeInMillis(currentStartTime);
                    currentYearI = currentCalender.get(Calendar.YEAR);
                    currentMonthI = currentCalender.get(Calendar.MONTH) + 1;
                    currentDayI = currentCalender.get(Calendar.DAY_OF_MONTH);
                    currentEndTime = currentStartTime + (1000 * 60 * 60 * 24);
                }
            }
        }
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_time_selector, null);
        mCycleWheelViewYear = (CycleWheelView) mView.findViewById(R.id.cwv_date);
        mCycleWheelViewMouth = (CycleWheelView) mView.findViewById(R.id.cwv_hour);
        mCycleWheelViewDay = (CycleWheelView) mView.findViewById(R.id.cwv_minute);
        mCycleWheelViewYear.setCycleEnable(false);
        mCycleWheelViewMouth.setCycleEnable(false);
        mCycleWheelViewDay.setCycleEnable(false);
        mTextViewCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        mTextVeiwSure = (TextView) mView.findViewById(R.id.tv_confirm);
        mTextViewStartTime = (TextView) mView.findViewById(R.id.tv_start_time);
        mTextViewEndTime = (TextView) mView.findViewById(R.id.tv_end_time);
        mLayoutStartTime = (LinearLayout) mView.findViewById(R.id.ll_start_time);
        mLayoutEndTime = (LinearLayout) mView.findViewById(R.id.ll_end_time);
        viewLineEnd = mView.findViewById(R.id.view_line_end);
        viewLineStart = mView.findViewById(R.id.view_line_start);
        initData();
    }

    private void initData() {
        setYearData();
        setMonthData();
        setDayData(currentYearI, currentMonthI, currentDayI);
        mTextViewStartTime.setText(TimeUtil.format(currentStartTime, "yyyy-MM-dd"));
        mTextViewEndTime.setText(TimeUtil.format(currentEndTime, "yyyy-MM-dd"));
        mLayoutStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelect = 1;
                viewLineStart.setVisibility(View.VISIBLE);
                viewLineEnd.setVisibility(View.INVISIBLE);
            }
        });
        mLayoutEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelect = 2;
                viewLineEnd.setVisibility(View.VISIBLE);
                viewLineStart.setVisibility(View.INVISIBLE);
            }
        });

        mTextViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTextVeiwSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtil.isNotEmpty(mTextViewStartTime.getText().toString())) {
                    long startTimeL = TimeUtil.parseMillsTime(mTextViewStartTime.getText().toString(), "yyyy-MM-dd");
                    //   long endTimeL = TimeUtil.parseMillsTime(mTextViewEndTime.getText().toString(),"yyyy-MM-dd");
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar1.setTimeInMillis(startTimeL);

//                    calendar2.setTimeInMillis(endTimeL);
//                    if (endTimeL < startTimeL) {
//
//                        ToastManager.getInstance().showToast("开始时间不能大于结束时间");
//                    }else {

                    mBindClickListener.selectTime(startTimeL);


                    dismiss();
//                        if (isMoreThanSixMonth(calendar1, calendar2)) {
//                            ToastManager.getInstance().showToast("请选择三个月内的时间");
//                        }else {
//                            mBindClickListener.selectTime(startTimeL, endTimeL);
//                            dismiss();
//                        }
                }
//                }else {
//                    ToastManager.getInstance().showToast("请选择开始和结束时间");
//                }
            }
        });
    }

    private boolean isMoreThanSixMonth(Calendar calendar1, Calendar calendar2) {
        if (calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR) > 1) {
            return true;
        } else if (calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR) == 1) {
            if (calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH) > -9) {
                return true;
            } else if (calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH) == -9) {
                if (calendar2.get(Calendar.DAY_OF_MONTH) > calendar1.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        } else if (calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR)) {
            if (calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH) > 3) {
                return true;
            } else if (calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH) == 3) {
                if (calendar2.get(Calendar.DAY_OF_MONTH) > calendar1.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getYearPosition(boolean start) {
        Calendar calendar = Calendar.getInstance();
        if (start) {
            calendar.setTimeInMillis(currentStartTime);
        } else {
            calendar.setTimeInMillis(currentEndTime);
        }
        if (yearList != null) {
            int position = yearList.indexOf(calendar.get(Calendar.YEAR));
            if (position >= 0) {
                return position;
            }
            return yearList.size() / 2;
        }
        return 0;
    }

    /**
     * 日期
     */
    private void setYearData() {
        yearList = new ArrayList<>();
        initYearData(0);
        mCycleWheelViewYear.setLabels(yearList, "年");
        try {
            mCycleWheelViewYear.setWheelSize(7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCycleWheelViewYear.setLabelSelectSize(14f);
        mCycleWheelViewYear.setLabelSize(12f);
        mCycleWheelViewYear.setAlphaGradual(0.5f);
        mCycleWheelViewYear.setCycleEnable(false);
        mCycleWheelViewYear.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewYear.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewYear.setLabelSelectColor(Color.parseColor("#333333"));
        mCycleWheelViewYear.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewYear.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                LogUtils.e("mCycleWheelViewDate", position + "====" + mCycleWheelViewYear.getSelection());
                int count = mCycleWheelViewYear.getLabelCount();
                if (position == 10) {
                    initYearData(1);
                    mCycleWheelViewYear.setLabels(yearList, "年");
                    mCycleWheelViewYear.setSelection(position + 24);
                }
                if (position == count - 10) {
                    initYearData(2);
                    mCycleWheelViewYear.setLabels(yearList, "年");
                    mCycleWheelViewYear.setSelection(position - 24);
                }
                currentYearI = yearList.get(position);
                setDayData(currentYearI, currentMonthI, currentDayI);
                setTimeText();
            }
        });
        mCycleWheelViewYear.setSelection(getYearPosition(currentSelect == 1));

    }

    private void setTimeText() {
        if (currentSelect == 1) {
            mTextViewStartTime.setText(mCycleWheelViewYear.getSelectLabel()
                    + "-" + mCycleWheelViewMouth.getSelectLabel() + "-" + mCycleWheelViewDay.getSelectLabel());
        } else {
            mTextViewEndTime.setText(mCycleWheelViewYear.getSelectLabel()
                    + "-" + mCycleWheelViewMouth.getSelectLabel() + "-" + mCycleWheelViewDay.getSelectLabel());
        }
    }

    /**
     * @param type 1 up 2down
     */
    private void initYearData(int type) {
        if (yearList == null || yearList.size() == 0) {
            yearList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                yearList.add(currentCalender.get(Calendar.YEAR) + (i - 25));
            }
        } else {
            if (type == 1) {
                int first = yearList.get(0);
                yearList.clear();
                for (int i = 0; i < 49; i++) {
                    yearList.add(first + (i - 24));
                }
            } else {
                int last = yearList.get(yearList.size() - 1);
                yearList.clear();
                for (int i = 0; i < 49; i++) {
                    yearList.add(last + (i - 24));
                }
            }
        }
    }

    /**
     *
     */
    private void setMonthData() {
        mouthList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            mouthList.add(i + 1);
        }
        mCycleWheelViewMouth.setLabels(mouthList, "月");
        try {
            mCycleWheelViewMouth.setWheelSize(7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCycleWheelViewMouth.setLabelSelectSize(14f);
        mCycleWheelViewMouth.setLabelSize(12f);
        mCycleWheelViewMouth.setAlphaGradual(0.7f);
        mCycleWheelViewMouth.setCycleEnable(false);
        mCycleWheelViewMouth.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewMouth.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewMouth.setLabelSelectColor(Color.parseColor("#333333"));
        mCycleWheelViewMouth.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewMouth.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                currentMonthI = mouthList.get(position);
                mCycleWheelViewMouth.setLabels(mouthList, "月");
                mCycleWheelViewMouth.setLabelColor(Color.parseColor("#b3b3b3"));
                mCycleWheelViewMouth.setDivider(Color.parseColor("#e3e3e3"), 1);
                mCycleWheelViewMouth.setLabelSelectColor(Color.parseColor("#333333"));
                setDayData(currentYearI, currentMonthI, currentDayI);
                setTimeText();
            }
        });
        mCycleWheelViewMouth.selection(currentMonthI - 1);
    }

    /**
     *
     */
    private void setDayData(int year, int month, int currentDay) {
        dayList = new ArrayList<>();
        int day = 28;
        if (month == 2) {
            if (TimeUtil.isLeapYear(year)) {
                day = 29;
            } else {
                day = 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            day = 30;
        } else {
            day = 31;
        }
        if (currentDay > day) {
            currentDay = day;
        }
        for (int i = 0; i < day; i++) {
            dayList.add(i + 1);
        }
        mCycleWheelViewDay.setLabels(dayList, "日");

        try {
            mCycleWheelViewDay.setWheelSize(7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCycleWheelViewDay.setLabelSelectSize(14f);
        mCycleWheelViewDay.setLabelSize(12f);
        mCycleWheelViewDay.setAlphaGradual(0.7f);
        mCycleWheelViewDay.setCycleEnable(false);
        mCycleWheelViewDay.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewDay.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewDay.setLabelSelectColor(Color.parseColor("#333333"));
        mCycleWheelViewDay.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewDay.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                currentDayI = dayList.get(position);
                setTimeText();
            }
        });
        mCycleWheelViewDay.setSelection(currentDay - 1);
    }

    public void show() {
        initView();
        mPopupWindow = new YunShlPopupWinow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setCommonConfig((Activity) mContext, YunShlPopupWinow.AnimaDirect.DIRECT_BOTTOM_TOP);
        mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
    }

    public void show(Activity activity, View parentView, boolean tranbg) {
        initView();
        mPopupWindow = new YunShlPopupWinow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setCommonConfig(activity, YunShlPopupWinow.AnimaDirect.DIRECT_BOTTOM_TOP, tranbg);
        mPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }


    public interface BindClickListener {
        void selectTime(long startTime);
    }

}
