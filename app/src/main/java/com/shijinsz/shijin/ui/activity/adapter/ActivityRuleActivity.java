package com.shijinsz.shijin.ui.activity.adapter;

import android.view.View;
import android.widget.ImageView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/10/23.
 */

public class ActivityRuleActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    public int bindLayout() {
        return R.layout.rule_activity;
    }

    @Override
    public void initView(View view) {

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
