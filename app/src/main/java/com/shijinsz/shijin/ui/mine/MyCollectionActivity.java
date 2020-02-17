package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.AdReleaseBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
import com.shijinsz.shijin.ui.mine.adapter.LookAdapter;
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
 * Created by yrdan on 2018/8/21.
 */

public class MyCollectionActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<AdReleaseBean.Records> list = new ArrayList<>();
    private LookAdapter adapter;
    private boolean isRefresh = true;
    private View empty;
    @Override
    public int bindLayout() {
        return R.layout.draft_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.my_collection));
        empty=mInflater.inflate(R.layout.center_empty_layout,null);
        adapter = new LookAdapter(R.layout.draft_item, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext,VideoDetailActivity.class);
                intent.putExtra("id",list.get(position).getAd().getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        cursor = System.currentTimeMillis() / 1000 + "";
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh=true;
                cursor = System.currentTimeMillis()/1000 + "";
                getData();
            }
        });
        mStateView.showLoading();
        getData();
    }
    private String cursor = "";

    private void getData() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("cursor", cursor);
        map.put("size", "10");
        YSBSdk.getService(OAuthService.class).ad_collection(map, new YRequestCallback<AdReleaseBean>() {
            @Override
            public void onSuccess(AdReleaseBean var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                adapter.setEmptyView(empty);
                if (isRefresh) {
                    list.clear();
                }
                if (var1.getRecords().size()>0){
                    cursor=var1.getRecords().get(var1.getRecords().size()-1).getCreated_at();
                    for (int i = 0; i < var1.getRecords().size(); i++) {
                        var1.getRecords().get(i).setCreated_at(TimeUtil.format(Long.valueOf(var1.getRecords().get(i).getCreated_at())*1000,"yyyy-MM-dd HH:mm"));
                    }
                }
                list.addAll(var1.getRecords());
                if (var1.getRecords().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                adapter.setEmptyView(empty);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                    mStateView.showRetry();
                }catch (Exception e){

                }

            }
        });

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        cursor = System.currentTimeMillis()/1000+ "";
        getData();
    }

}
