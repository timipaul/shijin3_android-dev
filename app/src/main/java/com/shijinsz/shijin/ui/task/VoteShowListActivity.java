package com.shijinsz.shijin.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.google.gson.Gson;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.task.adapter.VoteListViewAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: VoteShowListActivity
 * Author: m1342
 * Date: 2019/6/24 10:45
 * Description: 投票数据显示页面
 */
public class VoteShowListActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {


    @BindView(R.id.tv_right)
    TextView mRight;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.listView)
    ListView recyclerView;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private String cursor;
    private int page_index = 1;
    private boolean isRefresh = true;
    private VoteListViewAdapter adapter;
    private List<VoteBean> list = new ArrayList();

    @Override
    public int bindLayout() { return R.layout.vote_show_list; }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("参与投票");
        showTitleBackButton();
        mRight.setVisibility(View.VISIBLE);
        mRight.setText("投票记录");
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,VoteRecordData.class));
            }
        });

        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        ImageView iv = new ImageView(mContext);
        iv.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.vote_show_log));

        recyclerView.addHeaderView(iv);
        adapter = new VoteListViewAdapter(mContext,list,R.layout.vote_list_item);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new VoteListViewAdapter.OnItemClickListener() {
            @Override
            public void buttonOnclick() {
            }

            @Override
            public void layoutOnclick(int index,VoteBean voteBean) {
                Intent intent = new Intent(mContext,VoteDetailsActivity.class);
                Bundle data = new Bundle();
                data.putString("voteBean",new Gson().toJson(voteBean));
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                cursor = System.currentTimeMillis()/1000 + "";
                getData();
            }
        });
        cursor = System.currentTimeMillis()/1000 + "";
        mStateView.showLoading();
        mStateView.showContent();
        refresh.finishLoadMore();
        refresh.finishRefresh();

        getData();
    }

    private void getData(){

        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);

        Map map = new HashMap();
        map.put("mode", "person");
        map.put("pageIndex", page_index);
        map.put("pageSize", "10");
        YSBSdk.getService(OAuthService.class).get_vote_list(username,map, new YRequestCallback<BaseBean<VoteBean>>() {
            @Override
            public void onSuccess(BaseBean<VoteBean> var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();

                if (isRefresh) {
                    if (var1.getResult().size()==0){
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                    list.clear();
                }

                if (var1.getResult().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }

                list.addAll(var1.getResult());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showContent();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                    mStateView.showRetry();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        page_index++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        cursor = System.currentTimeMillis() / 1000 + "";
        isRefresh = true;
        page_index = 1;
        getData();
    }
}
