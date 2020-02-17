package com.shijinsz.shijin.ui.video.fragment;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.widget.VerticalViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.home.SearchActivity;
import com.shijinsz.shijin.ui.video.adapter.VerticalViewPagerAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoginUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

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
    @BindView(R.id.site_but)
    RadioButton mRaBut;
    @BindView(R.id.seek_but)
    Button mSeek_but;
    //背景
    @BindView(R.id.top_layout)
    public RelativeLayout mTopLayout;
    @BindView(R.id.bottom_layout)
    public RelativeLayout mBottomLayout;

    @BindView(R.id.video_title)
    public TextView mTitle;

    public String cursor;
    String mode = "routine";


    private List<Ads> adsList = new ArrayList<>();
    private VerticalViewPagerAdapter pagerAdapter;



    private boolean isRefresh = true;

    @Override
    protected int provideContentViewId() {
        return R.layout.video_fragment;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);

        mTopLayout.getBackground().setAlpha(153);
        mBottomLayout.setAlpha(0.52f);
        hideStatusNavigationBar();

        newnotifyDataSetChanged(rootView);

        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.attention_but:
                        mode = "follow";
                        break;
                    case R.id.recommend_but:
                        mode = "routine";
                        break;
                    case R.id.site_but:
                        mode = "city";
                        break;
                }
                isRefresh = true;
                cursor = System.currentTimeMillis() / 1000 + 60000 + "";
                //pagerAdapter.getItemPosition(POSITION_NONE);
                adsList.clear();
                pagerAdapter.mData.clear();
                newnotifyDataSetChanged(rootView);
                getVideoData();
            }
        });

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                getVideoData();

            }
        });

        mSeek_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });

        mStateView.showLoading();
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        pagerAdapter.mData.clear();

        getVideoData();

    }




    //更新适配器数据
    public void newnotifyDataSetChanged(View rootView){
        pagerAdapter = new VerticalViewPagerAdapter(rootView,getChildFragmentManager());
        vvpBackPlay.setVertical(true);
        vvpBackPlay.setOffscreenPageLimit(10);
        pagerAdapter.setUrlList(adsList);
        vvpBackPlay.setAdapter(pagerAdapter);
        vvpBackPlay.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideStatusNavigationBar();
                //mTitle.setText(adsList.get(position).getAd_title());
                if (position == adsList.size() - 1) {
                    cursor = adsList.get(adsList.size() - 1).getCreated_at();
                    //加载数据
                    getVideoData();
                    //srlPage.setEnableAutoLoadMore(true);
                    //srlPage.setEnableLoadMore(true);
                } else {
                    srlPage.setEnableAutoLoadMore(false);
                    srlPage.setEnableLoadMore(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void getVideoData(){
        Map<String,String> map = new HashMap<String,String>();
//        map.put("mode", "recommend");
        map.put("cursor", cursor);
        map.put("size", "10");
        map.put("mode", mode);
        if(mode.equals("city")){
            map.put("cityName",mRaBut.getText().toString());
        }
        YSBSdk.getService(OAuthService.class).ads("video",map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {

                mStateView.showContent();
                if (isRefresh) {
                    adsList.clear();
                    adsList.addAll(var1.getAds());
                    isRefresh = false;
                } else {
                    adsList.addAll(var1.getAds());
                    //pagerAdapter.mData.addAll(var1.getAds());
                }

                pagerAdapter.setUrlList(adsList);
                pagerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showRetry();
                    var1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //隐藏状态栏
    private void hideStatusNavigationBar(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            hideStatusNavigationBar();
            String city = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LOCATE);
            if(mRaBut.getText().equals("")){
                mRaBut.setText(city);
            }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                //说明从后台回到了前台

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                Intent intent = new Intent(); // Itent就是我们要发送的内容  
                intent.setAction("com.shijinsz.VideoItemFragment"); // 设置你这个广播的action  
                intent.putExtra("isVisible", "false");
                try {
                    getActivity().sendBroadcast(intent); // 发送广播
                }catch (Exception e) {

                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }
}
