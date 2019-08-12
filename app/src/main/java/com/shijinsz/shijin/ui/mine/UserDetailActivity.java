package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.model.model.bean.UserDetailBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.MyPagerAdapter;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
import com.shijinsz.shijin.ui.mine.adapter.UserAdAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.LoginUtil;
import com.shijinsz.shijin.utils.ShareDialog;
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

public class UserDetailActivity extends BaseActivity {
    @BindView(R.id.img_vip_into)
    ImageView imgVip;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.bt_follow)
    TextView btFollow;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.ln_tab)
    LinearLayout lnTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.img_header)
    CircleImageView imgHeader;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    private SmartRefreshLayout answerRefresh;
    private SmartRefreshLayout userRefresh;
    private String id;

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();
    }

    @Override
    public int bindLayout() {
        return R.layout.user_detail_activity;
    }
    ShareDialog shareDialog;
    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.detail_info));
        id = getIntent().getStringExtra("id");
        if (id.equals(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID))){
            btFollow.setVisibility(View.GONE);
        }
        shareDialog=new ShareDialog(mContext);
        showTitleRightBackButton(R.mipmap.icon_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
               getShare();
            }
        });
        initTab();
        mStateView.showLoading();

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getDetail();
            }
        });
        imgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginUtil.isLogin(mContext)) {
                    if (id.equals(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID))) {
                        startActivity(UserInformationActivity.class);
                    }
                }
            }
        });
    }
    //获取分享信息
    public void getShare(){
        mStateView.showLoading();
        String mode ="person_detail";
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                mStateView.showContent();
                shareDialog.showWithdrapDialog(mActivity, bean.getUser().getIs_black(), "为您推荐"+bean.getUser().getNickname()+"用户", var1.getShare_info(), bean.getUser().getImgurl(), "https://prod.shijinsz.net/app_h5/author_detail#uid="+id+"&time=1321545", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bean.getUser().getIs_black().equals("off")){
                            black();
                        }else {
                            unblack();
                        }
                    }
                },id);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
    private void unblack() {
        Map map=new HashMap();
        YSBSdk.getService(OAuthService.class).unblack(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                bean.getUser().setIs_black("off");
                shareDialog.dismissWithdrapDialog();
                ToastUtil.showToast("已移除黑名单");
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                shareDialog.dismissWithdrapDialog();
                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                if (shareDialog!=null)
                shareDialog.dismissWithdrapDialog();
            }
        });
    }

    private void black() {
        Map map=new HashMap();
        YSBSdk.getService(OAuthService.class).black(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                bean.getUser().setIs_black("on");
                shareDialog.dismissWithdrapDialog();
                ToastUtil.showToast("已加入黑名单");

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                shareDialog.dismissWithdrapDialog();
                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                if (shareDialog!=null)
                shareDialog.dismissWithdrapDialog();
            }
        });
    }

    private UserDetailBean bean;
    private void getDetail() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).follow(id,map, new YRequestCallback<UserDetailBean>() {
            @Override
            public void onSuccess(UserDetailBean var1) {
                mStateView.showContent();
                bean=var1;
                Glide.with(mContext).load(var1.getUser().getImgurl()).into(imgHeader);
                if (var1.getUser().getIs_advertiser().equals("on")) {
                    imgVip.setVisibility(View.VISIBLE);
                } else {
                    imgVip.setVisibility(View.GONE);
                }
                if (var1.getUser().getIs_follow().equals("on")) {
                    btFollow.setText(getString(R.string.isfollow));
                } else {
                    btFollow.setText(getString(R.string.user_like));
                }
                tvNickname.setText(var1.getUser().getNickname());
                tvFans.setText(var1.getUser().getFan_number());
                tvFollowNum.setText(var1.getUser().getFollow_people_number());
                answerList.clear();
                answerList.addAll(var1.getAnswer_ads());
                if (answerList.size()<10){
                    answerRefresh.setNoMoreData(true);
                }else {
                    answerRefresh.setNoMoreData(false);
                }
                answerAdapter.notifyDataSetChanged();
                userList.clear();
                userList.addAll(var1.getRelease_ads());
                if (userList.size()<10){
                    userRefresh.setNoMoreData(true);
                }else {
                    userRefresh.setNoMoreData(false);
                }
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


    private List<Ads> answerList = new ArrayList<>();
    private List<Ads> userList = new ArrayList<>();
    private UserAdAdapter answerAdapter;
    private UserAdAdapter usersAdapter;
    private PowerfulRecyclerView answerRecycerview, userRecycerview;

    private void initTab() {
        List<View> mFragments = new ArrayList<>();
        View answerView = mInflater.inflate(R.layout.get_comment_fragment, null);
        View userView = mInflater.inflate(R.layout.get_comment_fragment, null);
        answerRecycerview = answerView.findViewById(R.id.recyclerView);
        userRecycerview = userView.findViewById(R.id.recyclerView);
        mFragments.add(answerView);
        mFragments.add(userView);
        answerRefresh = answerView.findViewById(R.id.refresh);
        userRefresh = userView.findViewById(R.id.refresh);
        answerRefresh.setRefreshHeader(new HeaderView(mContext));
        userRefresh.setRefreshHeader(new HeaderView(mContext));
//        answerRefresh.setOnRefreshLoadMoreListener(this);
//        userRefresh.setOnRefreshLoadMoreListener(this);
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
        answerAdapter = new UserAdAdapter(R.layout.user_news_item, answerList);
        usersAdapter = new UserAdAdapter(R.layout.user_news_item, userList);
        answerRecycerview.setAdapter(answerAdapter);
        userRecycerview.setAdapter(usersAdapter);
        answerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("id",answerList.get(position).getId());
                startActivity(intent);
            }
        });
        usersAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("id",userList.get(position).getId());
                startActivity(intent);
            }
        });
        MyPagerAdapter mTabAdapter = new MyPagerAdapter(mFragments);
        viewpager.setAdapter(mTabAdapter);
        viewpager.setOffscreenPageLimit(mFragments.size());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.getTabAt(0).setText(getString(R.string.answer));
        tabLayout.getTabAt(1).setText(getString(R.string.publish));
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
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public boolean isAnswer = true;
    public boolean isUser = true;

    public void getAnswer(String cursor) {
        Map map = new HashMap();
        map.put("mode", "follow");
        map.put("size", "10");
        map.put("cursor", cursor);
        map.put("user_id", id);
        YSBSdk.getService(OAuthService.class).otheranswer(id,map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {
                answerRefresh.finishLoadMore();
                answerRefresh.finishRefresh();
                if (isAnswer) {
                    answerList.clear();
                }
                answerList.addAll(var1.getAnswer_ads());
                if (var1.getAnswer_ads().size() < 10) {
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
                try {
                    answerRefresh.finishLoadMore(false);
                    answerRefresh.finishRefresh(false);
                }catch (Exception e){

                }
            }
        });
    }

    public void getUser(String cursor) {
        Map map = new HashMap();
        map.put("mode", "follow");
        map.put("size", "10");
        map.put("cursor", cursor);
        map.put("user_id", id);
        YSBSdk.getService(OAuthService.class).otherpulish(id,map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {
                userRefresh.finishLoadMore();
                userRefresh.finishRefresh();
                if (isUser) {
                    userList.clear();
                }
                userList.addAll(var1.getRelease_ads());
                if (var1.getRelease_ads().size() < 10) {
                    userRefresh.setNoMoreData(true);
                } else {
                    userRefresh.setNoMoreData(false);
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                userRefresh.finishLoadMore(false);
                userRefresh.finishRefresh(false);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    userRefresh.finishLoadMore(false);
                    userRefresh.finishRefresh(false);
                }catch (Exception e){

                }

            }
        });
    }


    private void follow() {
        Map map = new HashMap();

        YSBSdk.getService(OAuthService.class).fans(id,map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                btFollow.setText(getString(R.string.isfollow));
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

    private void unfollow() {
        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView) mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.unfollow_dialog));
        ((TextView) mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();

                YSBSdk.getService(OAuthService.class).unfans(id, map, new YRequestCallback<PicCodeBean>() {
                    @Override
                    public void onSuccess(PicCodeBean var1) {
                        btFollow.setText(getString(R.string.user_like));
                        mailDialog.dismiss();
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
        });
        ((TextView) mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });

    }


    @OnClick({R.id.bt_follow, R.id.ln_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_follow:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                if (btFollow.getText().toString().equals(getString(R.string.user_like))) {
                    follow();
                } else {
                    unfollow();
                }
                break;
            case R.id.ln_follow:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                Intent intent=new Intent(mContext,UserFollowActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
        }
    }

}
