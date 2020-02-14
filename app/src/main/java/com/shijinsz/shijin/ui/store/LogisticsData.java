package com.shijinsz.shijin.ui.store;

import android.view.View;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.model.model.bean.LogisticBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.LogisticsAdapter;
import com.shijinsz.shijin.utils.KdniaoTrackQueryAPI;

import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 显示物流信息
 */
public class LogisticsData extends BaseActivity {


    @BindView(R.id.order_list)
    PowerfulRecyclerView mRecycler;
    @BindView(R.id.no_data)
    TextView mNopage;
    @BindView(R.id.order_time)
    TextView mTime;
    @BindView(R.id.order_num)
    TextView mNum;
    //订单号
    String order_serial;

    ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
    SimpleAdapter adapter;
    LogisticsAdapter mAdapter;
    LogisticBean mLogistic;

    KdniaoTrackQueryAPI api;

    Handler handler;

    private String type;
    private String order;

    @Override
    public int bindLayout() {
        return R.layout.logistics_layout;
    }

    @Override
    public void initView(View view) {
        setTitle("物流信息");
        showTitleBackButton();

        order_serial = getIntent().getStringExtra("order");
        handler = new Handler();

        try {
            type = getIntent().getStringExtra("type");
            order = getIntent().getStringExtra("order");
            String time = getIntent().getStringExtra("time");
            mTime.setText(time);
            mNum.setText(order);
            initData();
        } catch (Exception e) {
            mNopage.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        }


    }

    private void initData() {
        api = new KdniaoTrackQueryAPI();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result;
                    if (type.equals("JD")) {
                        result = api.getOrderTracesByJson(type, order);
                    } else {
                        //result = api.getOrderTracesByJson("ZTO", "75308523482784");
                        result = api.getOrderTracesByJson("ZTO", order);
                    }


                    mLogistic = new Gson().fromJson(result, LogisticBean.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new LogisticsAdapter(R.layout.item_logistics, mLogistic.getTraces());
                            mRecycler.setAdapter(mAdapter);
                            mRecycler.setVisibility(View.VISIBLE);
                            mNopage.setVisibility(View.GONE);

                        }
                    });
                } catch (Exception e) {
                    System.out.println("报错.......");
                    e.printStackTrace();
                }


            }
        }).start();
    }


}
