package com.shijinsz.shijin.ui.store;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreBean;
import com.hongchuang.ysblibrary.widget.VerticalViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.StoreVerticalViewPagerAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * 商城商品视频
 */
public class StoreVideoActivity extends BaseActivity {

    @BindView(R.id.vvp_back_play)
    VerticalViewPager vvpBackPlay;
    @BindView(R.id.srl_page)
    SmartRefreshLayout srlPage;

    private List<StoreBean> adsList = new ArrayList<>();
    private StoreVerticalViewPagerAdapter pagerAdapter;

    public String cursor;
    private int pageIndex = 1;
    private int intoIndex = -1;

    @Override
    public int bindLayout() {
        return R.layout.store_video_fragment;
    }

    @Override
    public void initView(View view) {

        intoIndex = getIntent().getIntExtra("index",0);

        hideStatusNavigationBar();
        newnotifyDataSetChanged(view);
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        getVideoData();
    }

    //更新适配器数据
    public void newnotifyDataSetChanged(View rootView) {
        pagerAdapter = new StoreVerticalViewPagerAdapter(rootView, getSupportFragmentManager());
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
                    pageIndex++;
                    //加载数据
                    getVideoData();
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

    //视频数据
    public void getVideoData() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", "10");
        map.put("pageIndex", ""+pageIndex);
        YSBSdk.getService(OAuthService.class).getVideo(map, new YRequestCallback<BaseBean<StoreBean>>() {
            @Override
            public void onSuccess(BaseBean<StoreBean> var1) {
                mStateView.showContent();
                adsList.addAll(var1.getResult());

                if(intoIndex != -1){
                    for (int i = 0; i < intoIndex; i++){
                        adsList.remove(0);
                    }
                    intoIndex = -1;
                }

                pagerAdapter.setUrlList(adsList);
                pagerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });

    }

    //隐藏状态栏
    private void hideStatusNavigationBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
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
                intent.setAction("com.shijinsz.StoreVideoItemFragment"); // 设置你这个广播的action  
                intent.putExtra("isVisible", "false");
                try {
                    sendBroadcast(intent); // 发送广播
                } catch (Exception e) {

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
