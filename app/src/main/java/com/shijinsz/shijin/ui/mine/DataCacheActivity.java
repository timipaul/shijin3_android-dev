package com.shijinsz.shijin.ui.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DataCleanManager;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/8/7.
 */

public class DataCacheActivity extends BaseActivity {
    @BindView(R.id.img_auto)
    ImageView imgAuto;

    private boolean isAuto=false;

    @Override
    public int bindLayout() {
        return R.layout.data_cache_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        if (ShareDataManager.getInstance().getPara( SharedPreferencesKey.KEY_AUTO).equals("on")){
            isAuto=false;
            imgAuto.setImageResource(R.mipmap.icon_openthe);
        }
        setTitle(getString(R.string.data_and_cache));
    }



    @OnClick({R.id.ln_auto, R.id.tv_clear_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_auto:
                if (isAuto){
                    isAuto=false;
                    ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_AUTO,"on");
                    imgAuto.setImageResource(R.mipmap.icon_openthe);
                }else {
                    isAuto=true;
                    ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_AUTO,"off");
                    imgAuto.setImageResource(R.mipmap.icon_off);
                }
                break;
            case R.id.tv_clear_cache:
                showUnbindDialog();
                break;
        }
    }

    RelativeLayout mailBoxLay;
    NoticeDialog mailDialog;
    public void showUnbindDialog() {
        if (mailBoxLay==null)
            mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.nickname_dialog, null);
        if (mailDialog == null)
            mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        TextView content = mailBoxLay.findViewById(R.id.content);
        content.setText(getString(R.string.cache_dialog));
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateView.showLoading();
                DataCleanManager.cleanInternalCache(mContext);
                mStateView.showContent();
                ToastUtil.showToast(getString(R.string.cache_success));
                mailDialog.dismiss();
            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
    }
}
