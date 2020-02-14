package com.shijinsz.shijin.ui.store.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.StoreBean;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.store.StoreVideoItemFragment;
import com.shijinsz.shijin.ui.video.fragment.VideoItemFragment;

import java.util.List;

/**
 * Copyright (C)
 * FileName: VerticalViewPagerAdapter
 * Author: Administrator
 * Date: 2019/8/6 0006 下午 15:18
 * Description: 视频fragment显示适配器
 */
public class StoreVerticalViewPagerAdapter extends PagerAdapter {
    private FragmentManager fragmentManager;
    public FragmentTransaction mCurTransaction;
    public BaseFragment mCurrentPrimaryItem = null;
    public List<StoreBean> mData;
    private View mView;

    public void setUrlList(List<StoreBean> data) {
        this.mData = data;
    }


    public StoreVerticalViewPagerAdapter(View view, FragmentManager fm) {
        this.fragmentManager = fm;
        mView = view;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (mCurTransaction == null) {
            mCurTransaction = fragmentManager.beginTransaction();
        }

        StoreVideoItemFragment fragment = new StoreVideoItemFragment();
        if (mData != null && mData.size() > 0) {
            Bundle bundle = new Bundle();

            if (position >= mData.size()) {
                //bundle.putString(VideoItemFragment.URL, urlList.get(position % urlList.size()));
            } else {
                //bundle.putString(VideoItemFragment.URL, urlList.get(position));
            }
            bundle.putString(StoreVideoItemFragment.URL,mData.get(position).getUrl());
            bundle.putString(StoreVideoItemFragment.TITLE,mData.get(position).getTitle());
            fragment.setArguments(bundle);
        }

        mCurTransaction.add(container.getId(), fragment,
                makeFragmentName(container.getId(), position));
        fragment.setUserVisibleHint(false);

        return fragment;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = fragmentManager.beginTransaction();
        }
        mCurTransaction.detach((BaseFragment) object);
        mCurTransaction.remove((BaseFragment) object);

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((BaseFragment) object).getView() == view;
    }

    private String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + position;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        BaseFragment fragment = (BaseFragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }
}
