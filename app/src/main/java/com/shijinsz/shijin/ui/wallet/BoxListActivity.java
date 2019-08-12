package com.shijinsz.shijin.ui.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.BoxBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.wallet.adapter.BoxAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/30.
 */

public class BoxListActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private List<BoxBean> list = new ArrayList<>();
    private BoxAdapter adapter;
    private boolean isRefresh = true;
    private String cursor = "";

    @Override
    public int bindLayout() {
        return R.layout.my_follow_activity2;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(R.string.shijin_box);
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnRefreshLoadMoreListener(this);
        adapter = new BoxAdapter(R.layout.shijin_box_item, list);
        recyclerView.setAdapter(adapter);
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        mStateView.showLoading();
        getData();
    }

    private void getData() {
        Map map = new HashMap();
        map.put("mode", "lottery");
        map.put("size", "10");
        map.put("cursor", cursor);
        YSBSdk.getService(OAuthService.class).lotteries(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID), map, new YRequestCallback<BaseBean<BoxBean>>() {
            @Override
            public void onSuccess(BaseBean<BoxBean> var1) {
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();
                if (isRefresh) {
                    if (var1.getLotteries().size()==0){
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                    list.clear();
                }
                list.addAll(var1.getLotteries());
                if (var1.getLotteries().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onException(Throwable var1) {
                try{
                    mStateView.showContent();
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                }catch (Exception e){

                }

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        cursor = list.get(list.size() - 1).getCreated_at();
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        getData();
    }

}
