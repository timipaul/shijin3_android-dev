package com.shijinsz.shijin.ui.store;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.BottomNavigationViewEx;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.storeUserBean;
import com.hongchuang.ysblibrary.widget.NoScrollViewPager;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.cart.CartFragment;
import com.shijinsz.shijin.ui.classify.ClassifyFragment;
import com.shijinsz.shijin.ui.homepage.StoreMyFragment;
import com.shijinsz.shijin.ui.store.adapter.StoreTabAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;


/** 商城 */
public class StoreHomeActivity extends BaseActivity {

    @BindView(R.id.store_bnve)
    BottomNavigationViewEx bnve;
    @BindView(R.id.vp_content)
    NoScrollViewPager vpContent;

    public String username;

    @Override
    public int bindLayout() {
        return R.layout.store_home_activity;
    }

    @Override
    public void initView(View view) {
        initBnve();

        //获取商城基本信息
        getStoreData();

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new StoreFragment());
        mFragments.add(new ClassifyFragment());
        mFragments.add(new CartFragment());
        mFragments.add(new StoreMyFragment());

        StoreTabAdapter mTabAdapter = new StoreTabAdapter(mFragments, getSupportFragmentManager());
        vpContent.setAdapter(mTabAdapter);
        vpContent.setOffscreenPageLimit(mFragments.size());

        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0;
                switch (item.getItemId()) {
                    case R.id.i_home:
                        position = 0;
                        break;
                    case R.id.i_classify:
                        position = 1;
                        break;
                    case R.id.i_cart:
                        position = 2;
                        break;
                    case R.id.i_my:
                        position = 3;
                        break;
                }

                if (previousPosition != position) {
                    vpContent.setCurrentItem(position, false);
                    previousPosition = position;
                    Log.i(TAG, "-----bnve-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");

                }
                return true;
            }
        });

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "-----ViewPager-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");
                bnve.setCurrentItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getStoreData() {
        username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        YSBSdk.getService(OAuthService.class).getStoreUser(map, new YRequestCallback<storeUserBean>() {
            @Override
            public void onSuccess(storeUserBean var1) {
                String state;
                if(var1.getPrivilege().equals("MEMBER") || var1.getPrivilege().equals("PARTNER")){
                    state = "true";
                }else{
                    state = "false";
                }
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_is_store_vip,state);
            }

            @Override
            public void onFailed(String var1, String message) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_is_store_vip,"false");
            }

            @Override
            public void onException(Throwable var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_is_store_vip,"false");
            }
        });

    }

    //设置显示模块
    public void showModelFragment(int index){
        vpContent.setCurrentItem(index, false);
    }


    private void initBnve() {
        bnve.setItemIconTintList(null);
        bnve.enableItemShiftingMode(false);
        bnve.enableShiftingMode(false);
        bnve.enableAnimation(false);
        bnve.setIconSize(20, 20);
        bnve.setTextSize(11);
        bnve.setTextTintList(0, getResources().getColorStateList(R.color.home_bottom));
        bnve.setTextTintList(1, getResources().getColorStateList(R.color.home_bottom));
        bnve.setTextTintList(2, getResources().getColorStateList(R.color.home_bottom));
        bnve.setTextTintList(3, getResources().getColorStateList(R.color.home_bottom));
        bnve.setItemHeight(BottomNavigationViewEx.dp2px(this, 52));
        bnve.setIconsMarginTop(BottomNavigationViewEx.dp2px(this, 10));

    }
}
