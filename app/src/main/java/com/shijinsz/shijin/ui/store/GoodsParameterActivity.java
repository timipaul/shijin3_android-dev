package com.shijinsz.shijin.ui.store;

import android.net.wifi.aware.PublishConfig;
import android.view.KeyEvent;
import android.view.View;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.OnClick;

/***
 *  商品参数
 */
public class GoodsParameterActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.store_goods_parameter_activity;
    }

    @Override
    public void initView(View view) {

    }


    @OnClick({R.id.tv_hide,R.id.submit})
    public void OnclickView(View view){
        switch (view.getId()){
            case R.id.tv_hide:
                finish();
                break;
            case R.id.submit:
                finish();
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
    }
}
