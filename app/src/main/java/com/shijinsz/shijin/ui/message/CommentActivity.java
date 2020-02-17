package com.shijinsz.shijin.ui.message;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.home.adapter.MainTabAdapter;
import com.shijinsz.shijin.ui.message.fragment.GetCommentFragment;
import com.shijinsz.shijin.ui.message.fragment.PutCommentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/8/8.
 */

public class CommentActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.delete)
    TextView delete;
    public boolean isEdit=false;
    public interface OnGetListen {
        void callback(boolean isShow);
        void onSelectAll();
        void onDelete();
    }
    public interface OnPutListen {
        void callback(boolean isShow);
        void onSelectAll();
        void onDelete();
    }
    public void setOnGetListen(OnGetListen onGetListen) {
        this.onGetListen = onGetListen;
    }

    public OnGetListen onGetListen;


    public void setOnPutListen(OnPutListen onPutListen) {
        this.onPutListen = onPutListen;
    }

    public OnPutListen onPutListen;

    @Override
    public int bindLayout() {
        return R.layout.comment_activity;
    }

    @Override
    public void initView(View view) {
        int num=Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ad_comment_number));
        int asset = Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_trend_total_number));
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_trend_total_number,asset-num+"");
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_ad_comment_number,"0");

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new GetCommentFragment());
        mFragments.add(new PutCommentFragment());
        MainTabAdapter mTabAdapter = new MainTabAdapter(mFragments, getSupportFragmentManager());
        viewpager.setAdapter(mTabAdapter);
        viewpager.setOffscreenPageLimit(mFragments.size());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.getTabAt(0).setText(getString(R.string.my_get));
        tabLayout.getTabAt(1).setText(getString(R.string.my_push));
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
                isEdit=false;
                tvRight.setText(getString(R.string.edit));
                ivBack.setVisibility(View.VISIBLE);
                tvLeft.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                if (tabLayout.getSelectedTabPosition()==0){
                    if (onGetListen!=null){
                        onGetListen.callback(isEdit);
                    }
                }else {
                    if (onPutListen!=null){
                        onPutListen.callback(isEdit);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_right,R.id.tv_left, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if (isEdit){
                    isEdit=false;
                    tvRight.setText(getString(R.string.edit));
                    ivBack.setVisibility(View.VISIBLE);
                    tvLeft.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }else {
                    isEdit=true;
                    tvRight.setText(getString(R.string.finish));
                    ivBack.setVisibility(View.GONE);
                    tvLeft.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
                if (tabLayout.getSelectedTabPosition()==0){
                    if (onGetListen!=null){
                        onGetListen.callback(isEdit);
                    }
                }else {
                    if (onPutListen!=null){
                        onPutListen.callback(isEdit);
                    }
                }
                break;
            case R.id.tv_left:
                if (tabLayout.getSelectedTabPosition()==0){
                    if (onGetListen!=null){
                        onGetListen.onSelectAll();
                    }
                }else {
                    if (onPutListen!=null){
                        onPutListen.onSelectAll();
                    }
                }
                break;
            case R.id.delete:
                if (tabLayout.getSelectedTabPosition()==0){
                    if (onGetListen!=null){
                        onGetListen.onDelete();
                    }
                }else {
                    if (onPutListen!=null){
                        onPutListen.onDelete();
                    }
                }
                break;
        }
    }
}
