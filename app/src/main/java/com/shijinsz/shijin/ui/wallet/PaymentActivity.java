package com.shijinsz.shijin.ui.wallet;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.PaymentBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.wallet.adapter.PaymentAdapter;
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
 * Created by yrdan on 2018/8/31.
 */

public class PaymentActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private PaymentAdapter adapter;
    private List<PaymentBean> list = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.my_withdraw_list_activity;
    }

    private boolean isRefresh = true;
    private String cursor = "";

    @Override
    public void initView(View view) {
        setTitle(getString(R.string.withdraw_record));
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnRefreshLoadMoreListener(this);
        adapter = new PaymentAdapter(R.layout.withdraw_item, list);
        recyclerView.setAdapter(adapter);
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
        map.put("mode", "withdraw");
        map.put("cursor", cursor);
        map.put("size", "10");
        YSBSdk.getService(OAuthService.class).payment(map, new YRequestCallback<BaseBean<PaymentBean>>() {
            @Override
            public void onSuccess(BaseBean<PaymentBean> var1) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                mStateView.showContent();
                if (var1.getRecords().size() > 0) {
                    for (int i = 0; i < var1.getRecords().size(); i++) {
                        String time = TimeUtil.format(Long.valueOf(var1.getRecords().get(i).getCreated_at()) * 1000, "yyyy-MM-dd HH:mm");
                        if (i == var1.getRecords().size() - 1) {
                            cursor = var1.getRecords().get(i).getCreated_at();
                        }
                        var1.getRecords().get(i).setCreated_at(time);
                    }
                }else {
                    if (isRefresh){
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                }
                if (isRefresh) {
                    money.setText(var1.getTotal_change() + getString(R.string.yuan));
                    list.clear();
                }
                list.addAll(var1.getRecords());
                if (list.size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                refresh.finishLoadMore(false);
                refresh.finishRefresh();
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    refresh.finishLoadMore(false);
                    refresh.finishRefresh();
                    if (isRefresh)
                        mStateView.showRetry();
                    else
                        mStateView.showContent();
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
        isRefresh = true;
        cursor = System.currentTimeMillis()/1000 + "";
        getData();
    }

}
