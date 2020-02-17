package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/21.
 */

public class MyLookActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    private List<AdReleaseBean.Records> list = new ArrayList<>();
    private LookAdapter adapter;
    private boolean isRefresh = true;
    private String sort = "-created_at";
    private View empty;
    @Override
    public int bindLayout() {
        return R.layout.my_put_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.mylook));
        empty=mInflater.inflate(R.layout.center_empty_layout,null);
        TextView tv_empty=empty.findViewById(R.id.tv_empty);
        tv_empty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_seen),null,null);
        tv_empty.setText(getString(R.string.seen_empty));
        adapter = new LookAdapter(R.layout.draft_item, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent =new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("id",list.get(position).getAd().getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh=true;
                cursor = System.currentTimeMillis()/1000 + "";
                getData();

            }
        });
        cursor = System.currentTimeMillis()/1000 + "";
        mStateView.showLoading();
        getData();

    }

    private String cursor = "-created_at";

    private void getData() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("sort", sort);
        map.put("cursor", cursor);
        map.put("size", "10");
        YSBSdk.getService(OAuthService.class).ad_look(map, new YRequestCallback<AdReleaseBean>() {
            @Override
            public void onSuccess(AdReleaseBean var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    tvTotal.setText(String.format(getString(R.string.total_look), var1.getTotal_size()));
                    adapter.setEmptyView(empty);
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
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
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


    private PopupWindow window;

    public void showEarlyDialog() {
        View popupView = mInflater.inflate(R.layout.look_soft_pop, null);
        if (window == null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        TextView cancel = popupView.findViewById(R.id.cancel);
        TextView tv1 = popupView.findViewById(R.id.tv1);
        TextView tv2 = popupView.findViewById(R.id.tv2);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRefresh = true;
                sort = "-created_at";
                cursor = System.currentTimeMillis() / 1000 + "";
                tvSort.setText(getString(R.string.near_to_far));
                getData();
                window.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort = "+created_at";
                isRefresh = true;
                cursor = "0";
                tvSort.setText(getString(R.string.far_to_near));
                getData();
                window.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);

            }
        });
    }


    @OnClick(R.id.tv_sort)
    public void onViewClicked() {
        showEarlyDialog();
    }

}
