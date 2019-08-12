package com.shijinsz.shijin.ui.task;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;

import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.OnClick;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InviteGroupActivity2 extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.invite_group_activity2;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.invite_group));
    }



    @OnClick(R.id.tv)
    public void onViewClicked() {
        getWechatApi();
    }

    /**
     * 跳转到微信
     */
    private void getWechatApi(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showToast("检查到您手机没有安装微信，请安装后使用该功能");
        }
    }
}
