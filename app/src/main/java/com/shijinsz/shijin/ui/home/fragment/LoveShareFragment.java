package com.shijinsz.shijin.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.home.LoveShareDetails;
import com.shijinsz.shijin.ui.home.adapter.LoveShareAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/** 爱分享模块 */
public class LoveShareFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private String type;
    private static final String TAG = "NewListFragment";
    private View footView;
    private List<StoreGoodsBean> list = new ArrayList<>();

    private LoveShareAdapter adapter;
    private boolean isRefresh = true;
    private int pageIndex = 1;

    public static LoveShareFragment getInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(TAG, type);
        LoveShareFragment videoArticleView = new LoveShareFragment();
        videoArticleView.setArguments(bundle);
        return videoArticleView;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.new_list_fragment;
    }

    @Override
    protected void loadData() {

        type=getArguments().getString(TAG);
        mInflater = LayoutInflater.from(getContext());
        footView = mInflater.inflate(R.layout.empty_layout, null);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setRefreshHeader(new HeaderView(getContext()));
        adapter = new LoveShareAdapter(R.layout.home_love_share_item, list);
        adapter.isUseEmpty(true);
        adapter.setEmptyView(footView);
        adapter.setHeaderAndEmpty(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnListen(new LoveShareAdapter.onListen() {
            @Override
            public void onClickInto(StoreGoodsBean data) {
                //点击跳转详情
                Intent intent = new Intent(getContext(), LoveShareDetails.class);
                //System.out.println("跳转详情ID " + data.get_id());
                intent.putExtra("goodsId",data.get_id());
                startActivity(intent);

            }
        });

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                getData();
            }
        });

        mStateView.showLoading();
        getData();

    }

    //获取数据
    private void getData() {
        Map<String,Object> map = new HashMap<>();
        map.put("pageIndex",pageIndex);
        map.put("pageSize","10");
        map.put("type","I-SHARE");
        YSBSdk.getService(OAuthService.class).getShopGoods(map, new YRequestCallback<BaseBean<StoreGoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<StoreGoodsBean> var1) {

                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    list.clear();
                }

                list.addAll(var1.getGoods());
                if (var1.getGoods().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(),var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        pageIndex++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        if (list.size() > 0) {
            pageIndex = 1;
            getData();
        } else {
            refresh.setNoMoreData(true);
            refresh.finishLoadMore();
        }
    }
}
