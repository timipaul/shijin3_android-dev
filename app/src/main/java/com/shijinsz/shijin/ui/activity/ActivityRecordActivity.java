package com.shijinsz.shijin.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.activity.adapter.ActivityRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/10/23.
 */

public class ActivityRecordActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.share)
    TextView share;
    @BindView(R.id.total_input)
    TextView totalInput;
    @BindView(R.id.total_get)
    TextView totalGet;
    @BindView(R.id.times)
    TextView times;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<String> list=new ArrayList<>();
    private ActivityRecordAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_record_activitylayout;
    }

    @Override
    public void initView(View view) {
        refresh.setEnableRefresh(false);
        refresh.setOnLoadMoreListener(this);
        for (int i = 0; i < 10; i++) {
            list.add(i+"");
        }
        adapter=new ActivityRecordAdapter(R.layout.activity_record_item,list);
        recyclerView.setAdapter(adapter);
    }


    @OnClick({R.id.iv_back, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.share:
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        for (int i = 0; i < 10; i++) {
            list.add(i+"");
        }
        adapter.notifyDataSetChanged();
        refresh.setNoMoreData(true);
        refresh.finishLoadMore();
    }
}
