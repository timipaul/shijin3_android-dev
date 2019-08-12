package com.shijinsz.shijin.ui.task;

import android.view.View;
import android.widget.TextView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InviteRuleActivity extends BaseActivity {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;


    @Override
    public int bindLayout() {
        return R.layout.invite_rule_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.invite_rule));
        tv1.setText(getIntent().getStringExtra("rule1"));
        tv2.setText(getIntent().getStringExtra("rule2"));
    }
}
