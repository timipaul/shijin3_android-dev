package com.shijinsz.shijin.ui.mine;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.PointDetailBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.wallet.adapter.PointDetailApater;
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
 * 兑换记录
 */
public class MySwopActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private PointDetailApater adpater;
    private List<PointDetailBean> list = new ArrayList<>();
    private String cursor;
    private boolean isRefresh = true;
    private String userId;

    @Override
    public int bindLayout() {
        return R.layout.my_wallet_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle(getString(R.string.convert_recode));
        showTitleBackButton();

        userId = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);

        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);

        adpater = new PointDetailApater(R.layout.point_detail_item, list);
        recyclerView.setAdapter(adpater);
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
        getData();
    }

    private void getData() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("cursor", cursor);
        map.put("user_id",userId);
        map.put("size", "10");
        //rush_goods
        //exchange_goods
        YSBSdk.getService(OAuthService.class).rush_goods(map, new YRequestCallback<BaseBean<PointDetailBean>>() {
            @Override
            public void onSuccess(BaseBean<PointDetailBean> var1) {
               // System.out.println("111数据: " + var1.getRecords().toString());
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    if (var1.getRecords().size()==0){
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                    list.clear();
                }
                if (var1.getRecords().size()>0){
                    for (int i = 0; i < var1.getRecords().size(); i++) {
                        String time="";
                        if (i==var1.getRecords().size()-1){
                            cursor=var1.getRecords().get(i).getCreated_at();
                        }
                        time= TimeUtil.format(Long.valueOf(var1.getRecords().get(i).getCreated_at())*1000,"yyyy-MM-dd HH:mm");
                        var1.getRecords().get(i).setCreated_at(time);
                    }
                }

                list.addAll(var1.getRecords());
                if (var1.getRecords().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                adpater.notifyDataSetChanged();
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
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        cursor = System.currentTimeMillis() / 1000 + "";
        isRefresh = true;
        getData();
    }

}
