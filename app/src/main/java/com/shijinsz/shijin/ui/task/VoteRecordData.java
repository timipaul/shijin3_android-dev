package com.shijinsz.shijin.ui.task;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
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
import com.shijinsz.shijin.ui.task.adapter.VoteDataApater;
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
 * FileName: VoteRecordData
 * Author: m1342
 * Date: 2019/6/25 14:29
 * Description: 投票历史
 */
public class VoteRecordData extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {


    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private VoteDataApater adapter;
    private List<VoteBean> list = new ArrayList<>();
    private String cursor;
    private boolean isRefresh = true;
    private String userId;
    private int pageIndex = 1;

    @Override
    public int bindLayout() {
        return R.layout.my_wallet_activity;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("投票记录");
        showTitleBackButton();

        userId = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);

        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        adapter = new VoteDataApater(R.layout.vote_record_list_item, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setBackgroundColor(Color.WHITE);
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
        mStateView.showLoading();
        mStateView.showContent();
        refresh.finishLoadMore();
        refresh.finishRefresh();
        getData();
    }

    private void getData() {
        Map map = new HashMap();
        map.put("pageIndex",pageIndex);
        map.put("pageSize", "10");
        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        YSBSdk.getService(OAuthService.class).get_vote_record(username,map, new YRequestCallback<BaseBean<VoteBean>>() {
            @Override
            public void onSuccess(BaseBean<VoteBean> var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    if (var1.getResult().size()== 0){
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
                System.out.println("投票记录: " + list.toString());

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
        pageIndex++;
        isRefresh = false;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        cursor = System.currentTimeMillis() / 1000 + "";
        pageIndex = 1;
        isRefresh = true;
        getData();
    }
}
