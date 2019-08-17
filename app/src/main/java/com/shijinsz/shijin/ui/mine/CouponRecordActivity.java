package com.shijinsz.shijin.ui.mine;

import android.view.View;

import com.hongchuang.ysblibrary.widget.NoScrollViewPager;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;

/**
 * Copyright (C)
 * FileName: CouponRecordActivity
 * Author: Administrator
 * Date: 2019/8/16 0016 上午 10:40
 * Description: 优惠劵记录
 */
public class CouponRecordActivity extends BaseActivity {

    @BindView(R.id.vp_content)
    NoScrollViewPager vpContent;

    @Override
    public int bindLayout() {
        return R.layout.mine_coupon_record;
    }

    @Override
    public void initView(View view) {

    }
}
