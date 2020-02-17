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

public class AssetMessageActivity extends BaseActivity {
    @BindView(R.id.tv_change_noread)
    TextView tvChangeNoread;
    @BindView(R.id.tv_point_noread)
    TextView tvPointNoread;

    @Override
    public int bindLayout() {
        return R.layout.asset_message_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        noread();
    }

    private String change,point;
    private void noread() {
        change= ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change_number);
        point = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_point_number);
        if (change.equals("0")){
            tvChangeNoread.setVisibility(View.GONE);
        }else {
            tvChangeNoread.setVisibility(View.VISIBLE);
            if (Integer.parseInt(change)>99){
                tvChangeNoread.setText("99");
            }else {
                tvChangeNoread.setText(change);
            }
        }
        if (point.equals("0")) {
            tvPointNoread.setVisibility(View.GONE);
        }else {
            tvPointNoread.setVisibility(View.VISIBLE);
            if (Integer.parseInt(point)>99){
                tvPointNoread.setText("99");
            }else {
                tvPointNoread.setText(point);
            }
        }
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        setTitle(getString(R.string.user_message));

    }


    @OnClick({R.id.ln_change_message, R.id.ln_point_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_change_message:
                startActivity(ChangeMessageActivity.class);
                break;
            case R.id.ln_point_message:
                startActivity(PointMessageActivity.class);
                break;
        }
    }
}
