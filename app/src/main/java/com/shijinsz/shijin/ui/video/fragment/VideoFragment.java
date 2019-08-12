package com.shijinsz.shijin.ui.video.fragment;


import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.widget.VerticalViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.video.adapter.VerticalViewPagerAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: VideoFragment
 * Author: Administrator
 * Date: 2019/8/6 0006 上午 11:59
 * Description: 视频模块
 */
public class VideoFragment extends BaseFragment{

    @BindView(R.id.vvp_back_play)
    VerticalViewPager vvpBackPlay;
    @BindView(R.id.srl_page)
    SmartRefreshLayout srlPage;
    @BindView(R.id.radioGroup)
    RadioGroup mRadio;
    @BindView(R.id.seek_but)
    ImageView mSeek_but;
    //背景
    @BindView(R.id.top_layout)
    public RelativeLayout mTopLayout;
    @BindView(R.id.bottom_layout)
    public RelativeLayout mBottomLayout;

    private List<String> urlList;
    private VerticalViewPagerAdapter pagerAdapter;

    @Override
    protected int provideContentViewId() {
        return R.layout.video_fragment;
    }

    @Override
    protected void loadData() {
        String cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        Map map = new HashMap();
//        map.put("mode", "recommend");
        map.put("cursor", cursor);
        map.put("size", "10");
        YSBSdk.getService(OAuthService.class).ads("video",map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {

                System.out.println("请求成功返回数据： " +var1.getAds());

                //mStateView.showContent();
                /*refresh.finishLoadMore();
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
                        refresh.setNoMoreData(true);
                    }
                }
                adapter.notifyDataSetChanged();*/
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
                System.out.println("没有数据1");
                //refresh.finishLoadMore();
                //refresh.finishRefresh();
//                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                System.out.println("没有数据12");
                try {
                    mStateView.showRetry();
                    //refresh.finishLoadMore();
                    //refresh.finishRefresh();
                    var1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);

        mTopLayout.getBackground().setAlpha(153);
        mBottomLayout.setAlpha(0.52f);
        hideStatusNavigationBar();

        makeData();

        pagerAdapter = new VerticalViewPagerAdapter(rootView,getChildFragmentManager());
        vvpBackPlay.setVertical(true);
        vvpBackPlay.setOffscreenPageLimit(10);
        pagerAdapter.setUrlList(urlList);
        vvpBackPlay.setAdapter(pagerAdapter);
        vvpBackPlay.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideStatusNavigationBar();
                if (position == urlList.size() - 1) {
                    srlPage.setEnableAutoLoadMore(true);
                    srlPage.setEnableLoadMore(true);
                } else {
                    srlPage.setEnableAutoLoadMore(false);
                    srlPage.setEnableLoadMore(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                System.out.println("当前选择的but： " + i);
                //getChildFragmentManager()
                System.out.println("");
            }
        });


    }


    private void makeData() {
        urlList = new ArrayList<>();
        //http://prod-static.shijinsz.net/content/video_1561039115058_18977893052.mp4#length=10008
        //urlList.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201805/100651/201805181532123423.mp4");
        urlList.add("http://prod-static.shijinsz.net/content/video_1561039115058_18977893052.mp4#length=10008");
        urlList.add("http://prod-static.shijinsz.net/content/video_1561039115058_18977893052.mp4#length=10008");
        urlList.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803150923220770.mp4");
        urlList.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803150922255785.mp4");
        urlList.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803150920130302.mp4");
        urlList.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803141625005241.mp4");
        urlList.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803141624378522.mp4");
        urlList.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803131546119319.mp4");
    }

    private void hideNavigationBar() {
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void hideStatusNavigationBar(){

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

        /*if(Build.VERSION.SDK_INT<16){
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
            getActivity().getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }*/
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            hideStatusNavigationBar();
        }
        try {

            Intent intent = new Intent(); // Itent就是我们要发送的内容  
            intent.setAction("com.shijinsz.VideoItemFragment"); // 设置你这个广播的action  
            intent.putExtra("isVisible", String.valueOf(isVisibleToUser));
            getActivity().sendBroadcast(intent); // 发送广播 

        }catch (Exception e) {

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();



    }
}
