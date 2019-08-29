package com.shijinsz.shijin.ui.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShieidsBean;
import com.hubcloud.adhubsdk.NativeAd;
import com.hubcloud.adhubsdk.NativeAdListener;
import com.hubcloud.adhubsdk.NativeAdResponse;
import com.hubcloud.adhubsdk.internal.network.ServerResponse;
import com.mrgao.luckly_popupwindow.LucklyPopopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
import com.shijinsz.shijin.ui.home.adapter.NewsAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.LoginUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/11/12.
 */

public class NewListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private static final String TAG = "NewListFragment";
    public static NewListFragment instance = null;
    LucklyPopopWindow mLucklyPopopWindow;
    private NewsAdapter adapter;
    private List<Ads> list = new ArrayList<>();
    private String cursor = "";
    private ShieidsBean shieidsBean;
    private boolean isRefresh = true;
    @Override
    protected int provideContentViewId() {
        return R.layout.new_list_fragment;
    }
    private String type;



    public static NewListFragment getInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(TAG, type);
        NewListFragment videoArticleView = new NewListFragment();
        videoArticleView.setArguments(bundle);
        return videoArticleView;
    }
    private List<? extends View> mAdViewList;
    private NativeAd nativeAd;
    @Override
    protected void loadData() {




        type=getArguments().getString(TAG);
        mInflater = LayoutInflater.from(getContext());
        footView = mInflater.inflate(R.layout.empty_layout, null);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setRefreshHeader(new HeaderView(getContext()));

        adapter = new NewsAdapter(R.layout.home_big_pic_item, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!list.get(position).getAd_mode().equals("adhub")){
                    for (String s : list.get(position).getTags()) {
                        if (s.equals("ad")){
                            Uri uri= Uri.parse(list.get(position).getUrl());
                            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent2);
                            return;
                        }
                    }
                    Intent intent = new Intent(mActivity, VideoDetailActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("purpose", "purpose");
                    startActivity(intent);
                }

            }
        });
        //2019.7.16 paul注释 测试服务器调试
        //getAdhub();

        adapter.isUseEmpty(true);
        adapter.setEmptyView(footView);
        adapter.setHeaderAndEmpty(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setCloseClickListen(new NewsAdapter.OnCloseClickListen() {
            @Override
            public void onClick(View view, final int pos) {
                mLucklyPopopWindow = new LucklyPopopWindow(getContext(), list.get(pos).getRelease_record().getNickname(), list.get(pos).getInterests());
                DisplayMetrics dm = getResources().getDisplayMetrics();
                mLucklyPopopWindow.setWidth(dm.widthPixels);
                //监听事件
                mLucklyPopopWindow.setOnItemClickListener(new LucklyPopopWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(boolean see, boolean interested, boolean content_level, boolean black_user, boolean black_label1, boolean black_label2, boolean black_label3) {
                        mLucklyPopopWindow.dismiss();
                        if (!LoginUtil.isLogin(mActivity)) {
                            return;
                        }
                        String user_id = "";
                        List<String> interests = new ArrayList<>();
                        if (black_user) {
                            user_id = list.get(pos).getUser_id();
                        }
                        if (black_label1) {
                            interests.add(list.get(pos).getInterests().get(0));
                        }
                        if (black_label2) {
                            interests.add(list.get(pos).getInterests().get(1));
                        }
                        if (black_label3) {
                            interests.add(list.get(pos).getInterests().get(2));
                        }
                        shieidsBean = new ShieidsBean(see, interested, content_level, user_id, interests);
                        black_ad(pos);

                    }
                });

                mLucklyPopopWindow.showAtLocation(getActivity().getWindow().getDecorView(), view);
            }
        });
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                getData(System.currentTimeMillis() / 1000 + 60000 + "");
//                getBanner();
            }
        });
        mStateView.showLoading();
        getData(System.currentTimeMillis() / 1000 + 60000 + "");


    }

    private View footView;
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    private void getAdhub(){

        /**
         * adCount(默认为3,取值可写1、2、3) 决定返回view类型广告的个数。
         * 具体返回的size以服务器返回为标准，请开发者自行判断，以防数组*下标越界。
         * 7143
         */

        nativeAd = new NativeAd(getActivity(), "979", 1, new NativeAdListener() {

            @Override
            public void onAdFailed(int errorcode) {

                System.out.println("-----nativeAd--------");
                Log.d("lance", "onAdFailed:" + errorcode);
                refresh.finishLoadMore();
                refresh.finishRefresh();
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onAdLoaded(NativeAdResponse response) {

                /**
                 * 因为该sdk接入了其他平台的sdk,所以native会返回两种类型
                 * 一种是直接返回渲染好的类型,另一种是返回数据（1条）
                 * 根据 response.getNativeInfoListView()是否为null判断选择广告类型
                 * 两种情况只能选一个
                 * 写法请务必参考以下代码:
                 */
                System.out.println("nativeAd 内容 " + response.getNativeInfoListView());
                if (response.getNativeInfoListView() != null) {
                    mAdViewList = response.getNativeInfoListView();
                    Log.i("lance", "getNativeInfoList:" + mAdViewList.size());
//                    mLyContainer.removeAllViews();
                    for (int i = 0; i < mAdViewList.size(); i++) {
                        View lyAdView = mAdViewList.get(i);
                        List<String> list1 = new ArrayList<>();
                        list1.add("adhub");
                        Ads ads = new Ads(nativeAd,null, "adhub", list.get(list.size()-1).getCreated_at(), null, null,lyAdView,null,null, null, null,null, "", null, null, null, null,null, null, null,null, null, null,null, null, null, null, null, null, null, null, null, null,list1,null, null, null,null,null);
                        Random random = new Random();
                        if (list.size()>5){
                            list.add(list.size()-random.nextInt(3)-1,ads);
                        }else{
                            list.add(ads);
                        }

//                      mLyContainer.addView(lyAdView);
                        nativeAd.nativeRender(lyAdView);
                    }

                } else {

                    // 一个广告只允许展现一次，多次展现、点击只会计入一次
                    //返回设置的广告的多个图片的URL，SDK并未处理加载urls里面的图片，需要集成者自己去加载展示
                    ArrayList<String> imageUrls = response.getImageUrls();
                    //返回设置的广告的多个视频流的URL，SDK并未处理加载urls里面的视频，需要集成者自己去加载展示
                    ArrayList<String> vedioUrls = response.getVedioUrls();
                    //返回设置的广告的多个文本信息
                    ArrayList<String> texts = response.getTexts();
                    //广告字样
                    ServerResponse.AdLogoInfo adUrl = response.getAdUrl();
                    //广告来源标识
                    ServerResponse.AdLogoInfo adLogoInfo = response.getlogoUrl();
                    List<String> list1 = new ArrayList<>();
                    list1.add("adhub");
                    Ads ads = new Ads(nativeAd,null, "adhub2", list.get(list.size()-1).getCreated_at(), null, null,response,null,null, null, null,null, response.getTexts().get(0), null, null, null, null,null, null, null,null, null, null,null, null, response.getImageUrls(), null, null, null, null, null, null, null,list1,null, null, null,null,null);
                    Random random = new Random();
                    if (list.size()>5){
                        list.add(list.size()-random.nextInt(3)-1,ads);
                    }else{
                        list.add(ads);
                    }
                }
                refresh.finishLoadMore();
                refresh.finishRefresh();
                adapter.notifyDataSetChanged();
            }
        });

        if(type.equals("individuation")){
            System.out.println("启动广告**************************************");
            nativeAd.loadAd();

        }
    }
    private void black_ad(final int pos) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("shields", shieidsBean);
        String id = "";
        id = list.get(pos).getId();
        YSBSdk.getService(OAuthService.class).shield(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                list.remove(pos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
//                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                var1.printStackTrace();
            }
        });
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        if (mAdViewList != null) {
            for (View view : mAdViewList) {
                nativeAd.nativeDestroy(view);
                Log.e("lance", "view.destroy()");
            }
        }
        getData(System.currentTimeMillis() / 1000 + 60000 + "");
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
            if (list.size() > 0) {
                getData(list.get(list.size() - 1).getCreated_at());
            } else {
                refresh.setNoMoreData(true);
                refresh.finishLoadMore();
            }
    }

    private void getData(String cursor1) {

        String channel = type;
        Map map = new HashMap();
//        map.put("mode", "index");
        map.put("cursor", cursor1);
        map.put("size", "10");
        map.put("channel",channel);
//        if (!type.equals("individuation")&&!type.equals("follow")){
//            channel="dynamic";
//            map.put("category",type);
//        }
        YSBSdk.getService(OAuthService.class).ads(channel,map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {

                System.out.println("---------------结果:" + var1);

                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    refresh.setNoMoreData(false);
                    list.clear();
                    list.addAll(var1.getAds());
//                        if (list.size() == 0) {
//                            banner.setVisibility(View.GONE);
//                        } else {
//                            banner.setVisibility(View.VISIBLE);
//                        }
                    if (list.size() < 10) {
                        refresh.setNoMoreData(true);
                    }
                } else {
                    list.addAll(var1.getAds());
                    if (var1.getAds().size() < 10) {
                        //这里应是设置不加载了
                        refresh.setNoMoreData(true);
                    }
                }


                if (type.equals("individuation")) {
                    //System.out.println("********************加载广告");
                    //加载广告
                    getAdhub();
                }else {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
//                ToastUtil.showToast(message);
                System.out.println("1  异常错误");
            }

            @Override
            public void onException(Throwable var1) {
                System.out.println("2  异常错误");
                try {
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                    var1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nativeAd.cancel();
        if (mAdViewList != null) {
            for (View view : mAdViewList) {
                nativeAd.nativeDestroy(view);
                Log.e("lance", "view.destroy()");
            }
        }
    }
}
