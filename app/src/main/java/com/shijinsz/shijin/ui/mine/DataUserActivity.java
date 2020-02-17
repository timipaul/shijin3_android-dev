package com.shijinsz.shijin.ui.mine;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.FollowNumberBean;
import com.hongchuang.ysblibrary.model.model.bean.TodayFollowBean;
import com.hongchuang.ysblibrary.utils.PopSetectDate;
import com.hongchuang.ysblibrary.utils.SetText;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/13.
 */

public class DataUserActivity extends BaseActivity {
    @BindView(R.id.linechart)
    LineChart linechart;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.tv_today_new)
    TextView tvTodayNew;
    @BindView(R.id.tv_unfollow)
    TextView tvUnfollow;
    @BindView(R.id.tv_near_7day)
    TextView tvNear7day;
    @BindView(R.id.tv_near_30day)
    TextView tvNear30day;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    private boolean is7day=true,is30day=false;
    private PopSetectDate popSetectDate,popSetectDateEnd;
    private long endtime;
    private long time;

    @Override
    public int bindLayout() {
        return R.layout.data_user_activity;
    }

    private YAxis leftYAxis;
    private YAxis rightYAxis;
    private XAxis xAxis;

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle(getString(R.string.data_user));
        showTitleBackButton();
        tvFollowNum.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_fan_number));
        endtime=System.currentTimeMillis()/1000;
        time = System.currentTimeMillis()/1000-3600*24*7;
        tvEndTime.setText(TimeUtil.format(endtime*1000,"yyyy年MM月dd日"));
        tvStartTime.setText(TimeUtil.format((endtime-3600*24*7)*1000,"yyyy年MM月dd日"));
        initChart();
        getTodayData();
    }

    private void getTodayData() {
        Map map =new HashMap();
        map.put("mode","person");
        YSBSdk.getService(OAuthService.class).today_follow_number(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map, new YRequestCallback<TodayFollowBean>() {
            @Override
            public void onSuccess(TodayFollowBean var1) {
                tvTodayNew.setText(var1.getToday_follow_number());
                tvUnfollow.setText(var1.getToday_unfollow_number());
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    private void initChart() {
        linechart.setDrawBorders(false);
        linechart.getDescription().setEnabled(false);
        linechart.setScaleEnabled(false);
        linechart.setNoDataText("暂无数据");
        linechart.getLegend().setEnabled(false);
        linechart.setDrawGridBackground(false);
        leftYAxis = linechart.getAxisLeft();
        rightYAxis = linechart.getAxisRight();
        xAxis = linechart.getXAxis();
        leftYAxis.setGridColor(getResources().getColor(R.color.mine_function_divider));
//        linechart.getAxisLeft().setDrawGridLines(false);
        leftYAxis.setTextColor(getResources().getColor(R.color.text_6));
        xAxis.setTextColor(getResources().getColor(R.color.text_6));
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawGridLines(false);
//        rightYAxis.setAxisMinimum(0f);
        //设置X轴
        xAxis = linechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        getData(System.currentTimeMillis()/1000-3600*24*7+"",System.currentTimeMillis()/1000+"");
    }

    private void getData(String start_time,String end_time) {
        Map map = new HashMap();
        map.put("mode","person");
        map.put("start_time",start_time);
        map.put("end_time",end_time);
        YSBSdk.getService(OAuthService.class).daily_follow_number(map, new YRequestCallback<BaseBean<FollowNumberBean>>() {
            @Override
            public void onSuccess(BaseBean<FollowNumberBean> var1) {
                notifyDataSetChanged(linechart,var1.getRecords());
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }
    @OnClick({R.id.tv_near_7day, R.id.tv_near_30day, R.id.tv_start_time, R.id.tv_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_near_7day:
                if (is7day){
                    return;
                }else {
                    is30day=false;
                    is7day=true;
                    getData(System.currentTimeMillis()/1000-3600*24*7+"",System.currentTimeMillis()/1000+"");
                    tvNear7day.setTextColor(getResources().getColor(R.color.white));
                    tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_choice_day));
                    tvNear30day.setTextColor(getResources().getColor(R.color.text_33));
                    tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                }
                break;
            case R.id.tv_near_30day:
                if (is30day){
                    return;
                }else {
                    is30day=true;
                    is7day=false;
                    getData(System.currentTimeMillis()/1000-3600*24*30+"",System.currentTimeMillis()/1000+"");
                    tvNear30day.setTextColor(getResources().getColor(R.color.white));
                    tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_choice_day));
                    tvNear7day.setTextColor(getResources().getColor(R.color.text_33));
                    tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                }
                break;
            case R.id.tv_start_time:
                if (popSetectDate==null)
                    popSetectDate = new PopSetectDate(mActivity);
                popSetectDate.showPopupWindow(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), new SetText() {
                    @Override
                    public void listenResult(String text) {
                        time = TimeUtil.parseTime(text,"yyyy.MM.dd");
                        if (time>System.currentTimeMillis()/1000){
                            time=System.currentTimeMillis()/1000;
                            tvStartTime.setText(TimeUtil.format(endtime*1000,"yyyy年MM月dd日"));
                            return;
                        }
                        tvStartTime.setText(TimeUtil.format(time*1000,"yyyy年MM月dd日"));
                        if (time > endtime){

                        }else {
                            is7day=false;
                            is30day=false;
                            tvNear30day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            tvNear7day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            getData(time+"",endtime+"");
                        }
                    }
                });
                break;
            case R.id.tv_end_time:
                if (popSetectDateEnd==null)
                    popSetectDateEnd = new PopSetectDate(mActivity);
                popSetectDateEnd.showPopupWindow(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), new SetText() {
                    @Override
                    public void listenResult(String text) {
                        endtime = TimeUtil.parseTime(text,"yyyy.MM.dd");
                        if (endtime>System.currentTimeMillis()/1000){
                            endtime=System.currentTimeMillis()/1000;
                            tvEndTime.setText(TimeUtil.format(endtime*1000,"yyyy年MM月dd日"));
                            return;
                        }
                        tvEndTime.setText(TimeUtil.format(endtime*1000,"yyyy年MM月dd日"));
                        if (time > endtime){

                        }else {
                            is7day=false;
                            is30day=false;
                            tvNear30day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            tvNear7day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            getData(time+"",endtime+3600 * 24+"");
                        }
                    }
                });
                break;
        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     */
    public void notifyDataSetChanged(LineChart chart,final List<FollowNumberBean> dataList) {
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return TimeUtil.format(Long.valueOf(dataList.get((int)value%dataList.size()).getCreated_at())*1000,"MM-dd");
            }
        });
        if (dataList.size()/2>7){
            xAxis.setLabelCount(8,true);
        }else {
            xAxis.setLabelCount(dataList.size() / 2 + 1,true);
        }
        leftYAxis.setLabelCount(3);
        chart.invalidate();
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            FollowNumberBean data = dataList.get(i);
            Entry entry = new Entry(i, Integer.parseInt(data.getFollow_number()));
            entries.add(entry);
        }
        setChartData(chart, entries);
    }
    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public void setChartData(LineChart chart, List<Entry> values) {
        LineDataSet lineDataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
           lineDataSet = new LineDataSet(values, null);
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setHighlightEnabled(false);
            lineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
            lineDataSet.setDrawValues(false);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillColor(getResources().getColor(R.color.color_FCE8E8));
            LineData data = new LineData(lineDataSet);
            linechart.setData(data);
            chart.invalidate();
        }
    }
}
