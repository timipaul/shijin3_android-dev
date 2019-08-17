package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.AdAllianceBean;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.PointDetailBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.ui.mine.adapter.CouponListViewAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.ShareDialog;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: ShoppingCouponActivity
 * Author: m1342
 * Date: 2019/7/10 14:55
 * Description: 购物优惠劵
 */
public class ShoppingCouponActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.tv_right)
    TextView mRight;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.listView)
    ListView mlistView;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @BindView(R.id.but_share)
    Button mbt_right;

    private int pageIndex = 1;
    private boolean isRefresh = true;

    private CouponListViewAdapter mAdapter;
    private List<AdAllianceBean> list = new ArrayList();

    @Override
    public int bindLayout() {
        return R.layout.common_show_listview;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("领劵中心");
        showTitleBackButton();
        mbt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShare();
            }
        });

        mbt_right.setVisibility(View.VISIBLE);
        

        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        mAdapter = new CouponListViewAdapter(mContext,list,R.layout.coupon_list_item);
        mlistView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CouponListViewAdapter.OnItemClickListener() {
            @Override
            public void buttonOnclick(String uri) {
                //跳转链接
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        mStateView.showLoading();
        mStateView.showContent();
        refresh.finishLoadMore();
        refresh.finishRefresh();
        getData();
    }

    //获取分享信息
    public void getShare(){
        mStateView.showLoading();
        String mode ="share_to_friend";
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                mStateView.showContent();
                new ShareDialog(mActivity).showWithdrapDialog(mActivity,3,var1.getShare_title(),var1.getShare_info(),var1.getShare_pic(), Comment.url+"invitation_registration?nickname="+ ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname)+"&username="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+"&imageurl="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl));
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    private void getData(){
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("pageIndex", pageIndex);
        map.put("pageSize", "10");

        YSBSdk.getService(OAuthService.class).get_alliance_ad(map, new YRequestCallback<BaseBean<AdAllianceBean>>() {
            @Override
            public void onSuccess(BaseBean<AdAllianceBean> var1) {
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
                mAdapter.notifyDataSetChanged();
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
        pageIndex++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        pageIndex = 1;
        isRefresh = true;
        getData();
    }
}
