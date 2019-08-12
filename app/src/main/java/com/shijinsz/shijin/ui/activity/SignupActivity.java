package com.shijinsz.shijin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.activity.adapter.ActivityRuleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/10/23.
 */

public class SignupActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_rule)
    TextView tvRule;

    @Override
    public int bindLayout() {
        return R.layout.activity_signup_activity;
    }

    @Override
    public void initView(View view) {

    }

    @OnClick({R.id.iv_back, R.id.tv_commit, R.id.tv_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_commit:
                startActivity(SignAfterActivity.class);
                break;
            case R.id.tv_rule:
                startActivity(ActivityRuleActivity.class);
                break;
        }
    }
}
