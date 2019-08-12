package com.shijinsz.shijin.ui.video.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.video.fragment.VideoItemFragment;

import java.util.List;

/**
 * Copyright (C)
 * FileName: VerticalViewPagerAdapter
 * Author: Administrator
 * Date: 2019/8/6 0006 下午 15:18
 * Description: 视频fragment显示适配器
 */
public class VerticalViewPagerAdapter extends PagerAdapter {
    private FragmentManager fragmentManager;
    private FragmentTransaction mCurTransaction;
    private BaseFragment mCurrentPrimaryItem = null;
    private List<String> urlList;
    private View mView;

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }


    public VerticalViewPagerAdapter(View view,FragmentManager fm) {
        this.fragmentManager = fm;
        mView = view;

    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (mCurTransaction == null) {
            mCurTransaction = fragmentManager.beginTransaction();
        }

        VideoItemFragment fragment = new VideoItemFragment();
        if (urlList != null && urlList.size() > 0) {
            Bundle bundle = new Bundle();
            if (position >= urlList.size()) {
                bundle.putString(VideoItemFragment.URL, urlList.get(position % urlList.size()));
            } else {
                bundle.putString(VideoItemFragment.URL, urlList.get(position));
            }
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

            ((Button)mView.findViewById(R.id.user_like)).setText("100");
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
