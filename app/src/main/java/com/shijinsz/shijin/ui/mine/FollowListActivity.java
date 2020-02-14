package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.FollowUserBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.adapter.FollowListAdapter;
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

public class FollowListActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<FollowUserBean> list = new ArrayList<>();
    private FollowListAdapter adapter;
    private boolean isRefresh=true;
    private View follow_empty;
    private String cursor="";
    @Override
    public int bindLayout() {return R.layout.my_follow_activity2; }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.myfollow));
        follow_empty=mInflater.inflate(R.layout.center_empty_layout,null);
        TextView tv_empty=follow_empty.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.follow_empty));
        tv_empty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_focus),null,null);
        adapter=new FollowListAdapter(R.layout.my_follow_item,list);
        recyclerView.setAdapter(adapter);
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        mStateView.showLoading();
        adapter.setOnFollow(new FollowListAdapter.OnFollow() {
            @Override
            public void call(int pos) {
                if (list.get(pos).getIs_follow().equals("on")){
                    //取消关注
                    unfollow(pos);

                }else {
                    //关注
                    follow(pos);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext,UserDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }
        });
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh=true;
                cursor=System.currentTimeMillis()/1000+"";
                getData();
            }
        });
        cursor=System.currentTimeMillis()/1000+"";
        getData();
    }

    private void follow(final int pos){
        Map map =new HashMap();

        YSBSdk.getService(OAuthService.class).fans(list.get(pos).getId(),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                list.get(pos).setIs_follow("on");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }
    private void unfollow(final int pos) {
        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView)mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.unfollow_dialog));
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map =new HashMap();
                YSBSdk.getService(OAuthService.class).unfans(list.get(pos).getId(),map, new YRequestCallback<PicCodeBean>() {
                    @Override
                    public void onSuccess(PicCodeBean var1) {
                        list.get(pos).setIs_follow("off");
                        mailDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String var1, String message) {
                        ErrorUtils.error(mContext,var1,message);
                    }

                    @Override
                    public void onException(Throwable var1) {
                        var1.printStackTrace();
                    }
                });

            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });

    }

    private void getData() {
        Map map = new HashMap();
        map.put("mode","person");
        map.put("cursor",cursor);
        map.put("size","10");
        YSBSdk.getService(OAuthService.class).user_follow(map, new YRequestCallback<BaseBean<FollowUserBean>>() {
            @Override
            public void onSuccess(BaseBean<FollowUserBean> var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh){
                    adapter.setEmptyView(follow_empty);
                    list.clear();
                }
                list.addAll(var1.getUsers());
                if (var1.getUsers().size()<10){
                    refresh.setNoMoreData(true);
                }else {
                    refresh.setNoMoreData(false);
                }
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
                try{
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                }catch (Exception e){

                }

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh=false;
        cursor=list.get(list.size()-1).getCreated_at();
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh=true;
        cursor=System.currentTimeMillis()/1000+"";
        getData();
    }
}
