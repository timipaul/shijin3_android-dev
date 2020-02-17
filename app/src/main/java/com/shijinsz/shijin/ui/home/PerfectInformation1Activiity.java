package com.shijinsz.shijin.ui.home;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.hongchuang.hclibrary.manager.MActivityManager;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/27.
 */

public class PerfectInformation1Activiity extends BaseActivity {
    @BindView(R.id.girl)
    ImageView girl;
    @BindView(R.id.check_girl)
    ImageView checkGirl;
    @BindView(R.id.boy)
    ImageView boy;
    @BindView(R.id.check_boy)
    ImageView checkBoy;

    @Override
    public int bindLayout() {
        return R.layout.perfect_information1_activity;
    }

    @Override
    public void initView(View view) {

    }


    @OnClick({R.id.girl, R.id.check_girl, R.id.boy, R.id.check_boy,R.id.pass})
    public void onViewClicked(View view) {
        Intent intent=new Intent(mContext,PerfectInformation2Activity.class);
        switch (view.getId()) {
            case R.id.girl:
                intent.putExtra("gender","female");
                startActivity(intent);
                break;
            case R.id.check_girl:
                intent.putExtra("gender","female");
                startActivity(intent);
                break;
            case R.id.boy:
                intent.putExtra("gender","male");
                startActivity(intent);
                break;
            case R.id.check_boy:
                intent.putExtra("gender","male");
                startActivity(intent);
                break;
            case R.id.pass:
                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
