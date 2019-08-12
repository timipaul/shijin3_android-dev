package com.shijinsz.shijin.ui.mine;

import android.view.View;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ad.NewAdActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.OnClick;

/**
 * Created by yrdan on 2018/8/9.
 */

public class MyAdActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.my_ad_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.my_ad));
    }


    @OnClick({R.id.ln_new, R.id.ln_draftbox, R.id.ln_release, R.id.ln_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_new:
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                    ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                    return;
                }
                startActivity(NewAdActivity.class);
                break;
            case R.id.ln_draftbox:
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                    ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                    return;
                }
                startActivity(DraftActivity.class);
                break;
            case R.id.ln_release:
                startActivity(MyPutActivity.class);
                break;
            case R.id.ln_data:
                startActivity(DataStatisticsActivity.class);
                break;
        }
    }
}
