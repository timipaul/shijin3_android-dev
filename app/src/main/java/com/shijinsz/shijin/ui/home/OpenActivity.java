package com.shijinsz.shijin.ui.home;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hubcloud.adhubsdk.AdListener;
import com.hubcloud.adhubsdk.SplashAd;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/2.
 */

public class
OpenActivity extends BaseActivity {

    @BindView(R.id.frame)
    FrameLayout frame;
    @BindView(R.id.rl)
    RelativeLayout rl;

    @Override
    public int bindLayout() {
        return R.layout.open_activity;
    }


    @Override
    public void initView(View view) {
        SplashAd splashAd = new SplashAd(this, frame, new AdListener() {
            @Override
            public void onAdShown() {
                Log.d("lance", "onAdShown");
            }

            @Override
            public void onAdLoaded() {
                Log.d("lance", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.d("lance", "onAdFailedToLoad");
                jump();
            }

            @Override
            public void onAdClosed() {
                Log.d("lance", "onAdClosed");
                jumpWhenCanClick();
            }

            @Override
            public void onAdClicked() {
                Log.d("lance", "onAdClicked");
            }
        }, "7139");

        splashAd.setCloseButtonPadding(10, 20, 10, 10);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加canJumpImmediately判断。 另外，点击开屏还需要在onResume中调用jumpWhenCanClick接口。
     */
    public boolean canJumpImmediately = false;

    private void jumpWhenCanClick() {
        Log.d("SplashActivity", "canJumpImmediately:" + canJumpImmediately);
        if (canJumpImmediately) {
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            canJumpImmediately = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SplashActivity", "onPause:" + canJumpImmediately);
        canJumpImmediately = false;
    }
    /**
     * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
     */
    private void jump() {
        this.startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SplashActivity", "onPause:" + canJumpImmediately);
        if (canJumpImmediately) {
            jumpWhenCanClick();
        }
        canJumpImmediately = true;
    }
}
