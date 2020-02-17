package com.shijinsz.shijin.ui.message;

import android.view.View;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/8/7.
 */

public class MessageActivity extends BaseActivity {
    @BindView(R.id.tv_dynamic_noread)
    TextView tvDynamicNoread;
    @BindView(R.id.tv_user_noread)
    TextView tvUserNoread;
    @BindView(R.id.tv_system_noread)
    TextView tvSystemNoread;

    @Override
    public int bindLayout() {
        return R.layout.message_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        noread();
    }
    private String dynamicNoread,userNoread,systemNoread;
    private void noread() {
        dynamicNoread = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_trend_total_number);
        userNoread = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_asset_total_number);
        systemNoread = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_circular_total_number);
        if (dynamicNoread.equals("0")){
            tvDynamicNoread.setVisibility(View.GONE);
        }else {
            tvDynamicNoread.setVisibility(View.VISIBLE);
            try {
                if (Integer.parseInt(dynamicNoread)>99){
                    tvDynamicNoread.setText("99");
                }else {
                    tvDynamicNoread.setText(dynamicNoread);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if (userNoread.equals("0")){
            tvUserNoread.setVisibility(View.GONE);
        }else {
            tvUserNoread.setVisibility(View.VISIBLE);
            try {
                if (Integer.parseInt(userNoread)>99){
                    tvUserNoread.setText("99");
                }else {
                    tvUserNoread.setText(userNoread);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if (systemNoread.equals("0")){
            tvSystemNoread.setVisibility(View.GONE);
        }else {
            tvSystemNoread.setVisibility(View.VISIBLE);
            try {
                if (Integer.parseInt(systemNoread)>99){
                    tvSystemNoread.setText("99");
                }else {
                    tvSystemNoread.setText(systemNoread);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void initView(View view) {
        setTitle(getString(R.string.message));
        showTitleBackButton();
    }


    @OnClick({R.id.ln_dynamic_message, R.id.ln_user_message, R.id.ln_system_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_dynamic_message:
                startActivity(TrendMessageActivity.class);
                break;
            case R.id.ln_user_message:
                startActivity(AssetMessageActivity.class);
                break;
            case R.id.ln_system_message:
                startActivity(SystemMessageActivity.class);
                break;
        }
    }
}
