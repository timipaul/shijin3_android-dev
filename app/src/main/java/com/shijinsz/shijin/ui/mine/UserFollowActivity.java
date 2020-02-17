package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.FollowDetailBean;
import com.hongchuang.ysblibrary.model.model.bean.FollowUserBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.MyPagerAdapter;
import com.shijinsz.shijin.ui.mine.adapter.FollowListAdapter;
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
 * Created by yrdan on 2018/9/5.
 */

public class UserFollowActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.delete)
    TextView delete;
    String id;
    @Override
    public int bindLayout() {
        return R.layout.comment_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        tvRight.setVisibility(View.GONE);
        id= getIntent().getStringExtra("id");
        initTab();
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getList();
            }
        });
    }

    private List<FollowUserBean> answerList = new ArrayList<>();
    private List<FollowUserBean> userList = new ArrayList<>();
    private FollowListAdapter answerAdapter;
    private FollowListAdapter usersAdapter;
    private PowerfulRecyclerView answerRecycerview, userRecycerview;
    private SmartRefreshLayout answerRefresh,userRefresh;
    private RelativeLayout answerEmpty,userEmpty;
    private TextView answertvEmpty,usertvEmpty;
    private boolean isAnswer=true,isUser=true;
    private void initTab() {
        List<View> mFragments = new ArrayList<>();
        View answerView = mInflater.inflate(R.layout.get_comment_fragment, null);
        View userView = mInflater.inflate(R.layout.get_comment_fragment, null);
        answerRecycerview = answerView.findViewById(R.id.recyclerView);
        userRecycerview = userView.findViewById(R.id.recyclerView);
        answerEmpty=answerView.findViewById(R.id.rl_empty);
        userEmpty=userView.findViewById(R.id.rl_empty);
        answertvEmpty=answerView.findViewById(R.id.tv_empty);
        usertvEmpty=userView.findViewById(R.id.tv_empty);
        answertvEmpty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_focus),null,null);
        usertvEmpty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_fans),null,null);
        answertvEmpty.setText(getString(R.string.follow_empty));
        usertvEmpty.setText(getString(R.string.fans_empty));
        mFragments.add(answerView);
        mFragments.add(userView);
        answerRefresh = answerView.findViewById(R.id.refresh);
        userRefresh = userView.findViewById(R.id.refresh);
        answerRefresh.setRefreshHeader(new HeaderView(mContext));
        userRefresh.setRefreshHeader(new HeaderView(mContext));
        answerRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isAnswer = false;
                getAnswer(answerList.get(answerList.size() - 1).getCreated_at());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isAnswer = true;
                getAnswer(System.currentTimeMillis()/1000 + "");
            }
        });
        userRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isUser = false;
                getUser(userList.get(userList.size() - 1).getCreated_at());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isUser = true;
                getUser(System.currentTimeMillis()/1000 + "");
            }
        });
        answerAdapter = new FollowListAdapter(R.layout.my_follow_item, answerList);
        usersAdapter = new FollowListAdapter(R.layout.my_follow_item, userList);
        answerRecycerview.setAdapter(answerAdapter);
        answerAdapter.setOnFollow(new FollowListAdapter.OnFollow() {
            @Override
            public void call(int pos) {
                if (answerList.get(pos).getIs_follow().equals("on")){
                    unfollow(pos,1);
                }else {
                    follow(pos,1);
                }
            }
        });
        usersAdapter.setOnFollow(new FollowListAdapter.OnFollow() {
            @Override
            public void call(int pos) {
                if (userList.get(pos).getIs_follow().equals("on")){
                    unfollow(pos,2);
                }else {
                    follow(pos,2);
                }
            }
        });
        answerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext,UserDetailActivity.class);
                intent.putExtra("id",answerList.get(position).getId());
                startActivity(intent);
            }
        });
        usersAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext,UserDetailActivity.class);
                intent.putExtra("id",userList.get(position).getId());
                startActivity(intent);
            }
        });
        userRecycerview.setAdapter(usersAdapter);
        MyPagerAdapter mTabAdapter = new MyPagerAdapter(mFragments);
        viewpager.setAdapter(mTabAdapter);
        viewpager.setOffscreenPageLimit(mFragments.size());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.getTabAt(0).setText(getString(R.string.user_like));
        tabLayout.getTabAt(1).setText(getString(R.string.fans));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mStateView.showLoading();
        getList();
    }

    private void getList() {
        Map map = new HashMap();
        map.put("mode","person");
        YSBSdk.getService(OAuthService.class).follow_details(id,map, new YRequestCallback<FollowDetailBean>() {
            @Override
            public void onSuccess(FollowDetailBean var1) {
                mStateView.showContent();
                answerList.clear();
                userList.clear();
                if (var1.getFans().size()==0){
                    userEmpty.setVisibility(View.VISIBLE);
                }
                if (var1.getUsers().size()==0){
                    answerEmpty.setVisibility(View.VISIBLE);
                }
                answerList.addAll(var1.getUsers());
                userList.addAll(var1.getFans());
                answerAdapter.notifyDataSetChanged();
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showRetry();
            }
        });
    }

    private void getUser(String created_at) {
        Map map = new HashMap();
        map.put("mode","user_follow");
        map.put("cursor",created_at);
        map.put("size","10");
        map.put("user_id",id);
        YSBSdk.getService(OAuthService.class).other_fans(map, new YRequestCallback<FollowDetailBean>() {
            @Override
            public void onSuccess(FollowDetailBean var1) {
                userRefresh.finishRefresh();
                userRefresh.finishLoadMore();
                if (isUser) {
                    userList.clear();
                }
                userList.addAll(var1.getFans());
                if (var1.getFans().size() < 10) {
                    userRefresh.setNoMoreData(true);
                } else {
                    userRefresh.setNoMoreData(false);
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                userRefresh.finishRefresh(false);
                userRefresh.finishLoadMore(false);
            }

            @Override
            public void onException(Throwable var1) {
                userRefresh.finishRefresh(false);
                userRefresh.finishLoadMore(false);
            }
        });
    }

    private void getAnswer(String created_at) {
        Map map = new HashMap();
        map.put("mode", "follow_user");
        map.put("size", "10");
        map.put("cursor", created_at);
        map.put("user_id",id);
        YSBSdk.getService(OAuthService.class).other_user_like(map, new YRequestCallback<FollowDetailBean>() {
            @Override
            public void onSuccess(FollowDetailBean var1) {
                answerRefresh.finishLoadMore();
                answerRefresh.finishRefresh();
                if (isAnswer) {
                    answerList.clear();
                }
                answerList.addAll(var1.getUsers());
                if (var1.getUsers().size() < 10) {
                    answerRefresh.setNoMoreData(true);
                } else {
                    answerRefresh.setNoMoreData(false);
                }
                answerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                answerRefresh.finishLoadMore(false);
                answerRefresh.finishRefresh(false);
            }

            @Override
            public void onException(Throwable var1) {
                answerRefresh.finishLoadMore(false);
                answerRefresh.finishRefresh(false);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void follow(final int pos,int type){
        Map map =new HashMap();
        String userid="";
        if (type==1){
            userid=answerList.get(pos).getId();
        }else {
            userid=userList.get(pos).getId();
        }
        YSBSdk.getService(OAuthService.class).fans(userid,map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                if (type==1) {
                    answerList.get(pos).setIs_follow("on");
                    answerAdapter.notifyDataSetChanged();
                }else {
                    userList.get(pos).setIs_follow("on");
                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }
    private void unfollow(final int pos,int type) {
        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView)mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.unfollow_dialog));
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map =new HashMap();
                if (type==1){
                    YSBSdk.getService(OAuthService.class).unfans(answerList.get(pos).getId(),map, new YRequestCallback<PicCodeBean>() {
                        @Override
                        public void onSuccess(PicCodeBean var1) {
                            answerList.get(pos).setIs_follow("off");
                            mailDialog.dismiss();
                            answerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailed(String var1, String message) {
                            ErrorUtils.error(mContext,var1,message);
                        }

                        @Override
                        public void onException(Throwable var1) {

                        }
                    });
                }else {
                    YSBSdk.getService(OAuthService.class).unfans(userList.get(pos).getId(),map, new YRequestCallback<PicCodeBean>() {
                        @Override
                        public void onSuccess(PicCodeBean var1) {
                            userList.get(pos).setIs_follow("off");
                            mailDialog.dismiss();
                            usersAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailed(String var1, String message) {
                            ErrorUtils.error(mContext,var1,message);
                        }

                        @Override
                        public void onException(Throwable var1) {

                        }
                    });
                }


            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });

    }

}
