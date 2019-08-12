package com.shijinsz.shijin.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.activity.adapter.TeamStatusAdapter;
import com.shijinsz.shijin.utils.HeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yrdan on 2018/10/24.
 */

public class TeamStatusActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private TeamStatusAdapter adapter;
    private List<String> list=new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.team_status_activity;
    }

    @Override
    public void initView(View view) {
        setTitle("小组赛况");
        showTitleBackButton();
        View header = mInflater.inflate(R.layout.team_status_header,null);
        HeaderView headerView=new HeaderView(mContext);
        refresh.setRefreshHeader(headerView);
        refresh.setOnRefreshLoadMoreListener(this);
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        adapter=new TeamStatusAdapter(R.layout.team_status_item,list);
        adapter.addHeaderView(header);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refresh.finishLoadMore();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refresh.finishRefresh();
        list.clear();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        adapter.notifyDataSetChanged();
    }
}
