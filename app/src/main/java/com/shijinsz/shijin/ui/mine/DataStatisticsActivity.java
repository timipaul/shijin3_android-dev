package com.shijinsz.shijin.ui.mine;

import android.view.View;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.OnClick;

/**
 * Created by yrdan on 2018/8/10.
 */

public class DataStatisticsActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.data_statistics_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.data_statistics));
    }


    @OnClick({R.id.tv_data_user, R.id.tv_data_ad, R.id.tv_data_put})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_data_user:
                startActivity(DataUserActivity.class);
                break;
            case R.id.tv_data_ad:
                startActivity(DataAdActivity.class);
                break;
            case R.id.tv_data_put:
                startActivity(DataPutActivity.class);
                break;
        }
    }
}
