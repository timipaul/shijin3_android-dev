package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ad.NewAdActivity;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
import com.shijinsz.shijin.ui.mine.adapter.MyputAdapter;
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
 * Created by yrdan on 2018/8/14.
 */

public class MyPutActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.ln_empty)
    LinearLayout lnEmpty;
    private List<Ads> list = new ArrayList<>();
    private MyputAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.my_put_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.my_release));
//        refresh.setRefreshFooter(new ClassicsFooter(mContext));
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        adapter = new MyputAdapter(R.layout.my_put_item, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (list.get(position).getAdshow()){
                    case "topay":
                        ToastUtil.showToast("您的发布未支付");
                        break;
                    case "pending":
                        ToastUtil.showToast("您的发布审核中");
                        break;
                    case "on":
                        Intent intent =new Intent(mContext, VideoDetailActivity.class);
                        intent.putExtra("id",list.get(position).getId());
                        startActivity(intent);
                        break;
                    case "back":
                        ToastUtil.showToast("您的发布未通过");
                        break;
                    case "renew":
                        Intent intent3 =new Intent(mContext, VideoDetailActivity.class);
                        intent3.putExtra("id",list.get(position).getId());
                        startActivity(intent3);
                        break;
                    case "off":
                        Intent intent2 =new Intent(mContext, VideoDetailActivity.class);
                        intent2.putExtra("id",list.get(position).getId());
                        startActivity(intent2);
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                page=1;
                getData();
            }
        });
        getData();
    }

    private int page = 1;
    private String sort = "created_at";

    private void getData() {
        if (page == 1) {
            mStateView.showLoading();
        }
        Map map = new HashMap();
        map.put("mode", "release");
        map.put("sort", "-"+sort);
        map.put("page_size", "10");
        map.put("current_page", page);
        YSBSdk.getService(OAuthService.class).get_put(map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();
                tvTotal.setText(String.format(getString(R.string.total_put), var1.getTotal_size()));
                if (page == 1) {
                    list.clear();
                    if (var1.getAds()==null||var1.getAds().size()==0){
                        lnEmpty.setVisibility(View.VISIBLE);
                    }
                }
                if (var1.getAds().size()>0){
                    for (int i = 0; i < var1.getAds().size(); i++) {
                        var1.getAds().get(i).setCreated_at(TimeUtil.format(Long.valueOf(var1.getAds().get(i).getCreated_at())*1000,"yyyy-MM-dd HH:mm"));
                    }
                }
                list.addAll(var1.getAds());
                if (var1.getAds().size() < 10) {
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
                    mStateView.showRetry();
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                }catch (Exception e){

                }

            }
        });
    }



    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    private PopupWindow window;

    public void showEarlyDialog() {
        View popupView = mInflater.inflate(R.layout.my_put_soft_pop, null);
        if (window == null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        TextView cancel = popupView.findViewById(R.id.cancel);
        TextView tv1 = popupView.findViewById(R.id.tv1);
        TextView tv3 = popupView.findViewById(R.id.tv3);
        TextView tv4 = popupView.findViewById(R.id.tv4);
        TextView tv5 = popupView.findViewById(R.id.tv5);
        TextView tv6 = popupView.findViewById(R.id.tv6);
        TextView tv7 = popupView.findViewById(R.id.tv7);
        TextView tv8 = popupView.findViewById(R.id.tv8);
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
                page = 1;
                sort = "reward";
                tvSort.setText(getString(R.string.last_reward));
                getData();
                window.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                sort = "ad_exposure";
                tvSort.setText(getString(R.string.ad_show_num));
                getData();
                window.dismiss();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                sort = "ad_click";
                tvSort.setText(getString(R.string.ad_click_num));
                getData();
                window.dismiss();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                sort = "ad_like";
                tvSort.setText(getString(R.string.ad_share_num));
                getData();
                window.dismiss();
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                sort = "ad_share";
                tvSort.setText(getString(R.string.ad_star_num));
                getData();
                window.dismiss();
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                sort = "ad_comment";
                tvSort.setText(getString(R.string.ad_like_num));
                getData();
                window.dismiss();
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                sort = "ad_collection";
                tvSort.setText(getString(R.string.ad_comment));
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



    @OnClick({R.id.tv_put, R.id.tv_sort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_put:
                startActivity(NewAdActivity.class);
                break;
            case R.id.tv_sort:
                showEarlyDialog();
                break;
        }
    }
}
