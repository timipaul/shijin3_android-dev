package com.shijinsz.shijin.ui.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DevicesUtil;
import com.hongchuang.ysblibrary.common.Constants;
import com.meituan.android.walle.WalleChannelReader;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2018/3/17.
 */

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,SplashADListener {

    private SplashAD splashAD;
    private ViewGroup container;
    private TextView skipView;
    private ImageView splashHolder;
    private static final String SKIP_TEXT = "点击跳过 %d";

    public boolean canJump = false;
    private boolean needStartDemoList = true;
    /**
     * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
     * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
     * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值（单位ms）
     **/
    private int minSplashTimeWhenNoAD = 2000;
    /**
     * 记录拉取广告的时间
     */
    private long fetchSplashADTime = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Handler
     */
    private static Handler handlerdelay = new Handler();
    public final Runnable JumpActivity = new Runnable() {
        @Override
        public void run() {
//            requestPermission();
        }
    };
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.frame)
    FrameLayout frame;
    private String url = "";


    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }
    @Override
    public void initView(View view) {
        String channel = WalleChannelReader.getChannel(mContext.getApplicationContext());
        //img.setImageResource(R.mipmap.splash_360);
        switch (channel + "") {
            case "360":
                img.setImageResource(R.mipmap.splash_360);
                break;
            case "ali":
//                img.setImageResource(R.mipmap.splash_ali);
                break;
            case "baidu":
                break;
            case "huawei":
                break;

            case "meizu":
                break;
            case "oppo":
                break;
            case "other":
                break;
            case "sougou":
                break;
            case "vivo":
                break;
            case "xiaomi":
                break;
            case "yingyongbao":
                img.setImageResource(R.mipmap.splash_yingyongbao);
                break;
            default:
                break;
        }
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_open, "on");

        container = (ViewGroup) this.findViewById(R.id.splash_container);
        skipView = (TextView) findViewById(R.id.skip_view);
        splashHolder = (ImageView) findViewById(R.id.splash_holder);
        boolean needLogo = getIntent().getBooleanExtra("need_logo", true);
        needStartDemoList = getIntent().getBooleanExtra("need_start_demo_list", true);
        if (!needLogo) {
            findViewById(R.id.app_logo).setVisibility(View.GONE);
        }
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
        }


        //requestPermission();
//        handlerdelay.postDelayed(JumpActivity, 980);
    }

    /**
     *
     * ----------非常重要----------
     *
     * Android6.0以上的权限适配简单示例：
     *
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     *
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity        展示广告的activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId           应用ID
     * @param posId           广告位ID
     * @param adListener      广告状态监听器
     * @param fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        fetchSplashADTime = System.currentTimeMillis();
        Map<String, String> tags = new HashMap<>();
        tags.put("tag_s1", "value_s1");
        tags.put("tag_s2", "value_s2");

        /*splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener,
                fetchDelay, tags);*/
        // 如果不需要传tag，使用如下构造函数
         splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }

    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
    }

    @Override
    public void onADClicked() {
        /*Log.i("AD_DEMO", "SplashADClicked clickUrl: "
                + (splashAD.getExt() != null ? splashAD.getExt().get("clickUrl") : ""));*/
    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onADTick(long millisUntilFinished) {
        Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
        skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
    }

    @Override
    public void onADExposure() {
        Log.i("AD_DEMO", "SplashADExposure");
    }

    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }

    @Override
    public void onNoAD(AdError error) {
        Log.i(
                "AD_DEMO",
                String.format("LoadSplashADFail, eCode=%d, errorMsg=%s", error.getErrorCode(),
                        error.getErrorMsg()));
        /**
         * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
         * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
         * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值
         **/
        long alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime;//从拉广告开始到onNoAD已经消耗了多少时间
        long shouldDelayMills = alreadyDelayMills > minSplashTimeWhenNoAD ? 0 : minSplashTimeWhenNoAD
                - alreadyDelayMills;//为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD
        // 计算出还需要延时多久
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (needStartDemoList) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, shouldDelayMills);
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     *    * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        if (canJump) {
            if (needStartDemoList) {
                this.startActivity(new Intent(this, MainActivity.class));
            }
            this.finish();
        } else {
            canJump = true;
        }
    }



















    /**
     * 请求读取sd卡的权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(getApplicationContext(), mPermissionList)) {
                //initAd();

                //已经同意过
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        getString(R.string.need_sd_prim), //提示文言
                        10001, //请求码
                        mPermissionList //权限列表
                );
            }
        } else {


            //initAd();
        }
    }

    //授权结果，分发下去
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //跳转到onPermissionsGranted或者onPermissionsDenied去回调授权结果
        if (requestCode == 10001)
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }*/


    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == 10001) {
            //initAd();
        }
    }

    //拒绝授权
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 10001) {

            startActivity(MainActivity.class);


            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作
            startActivity(OpenActivity.class);
            finish();
        }
    }



    /*@Override
    public void initView(View view) {
        String channel = WalleChannelReader.getChannel(mContext.getApplicationContext());
        //img.setImageResource(R.mipmap.splash_360);
        switch (channel + "") {
            case "360":
                img.setImageResource(R.mipmap.splash_360);
                break;
            case "ali":
//                img.setImageResource(R.mipmap.splash_ali);
                break;
            case "baidu":
                break;
            case "huawei":
                break;

            case "meizu":
                break;
            case "oppo":
                break;
            case "other":
                break;
            case "sougou":
                break;
            case "vivo":
                break;
            case "xiaomi":
                break;
            case "yingyongbao":
                img.setImageResource(R.mipmap.splash_yingyongbao);
                break;
            default:
                break;
        }
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_open, "on");
        requestPermission();
//        handlerdelay.postDelayed(JumpActivity, 980);
    }*/


    public boolean isFirstStart() {
        String firstStart = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_FIRST_START);
        if (firstStart != null && firstStart.equals(DevicesUtil.getVersion(this))) {
            ShareDataManager.getInstance().save(getApplicationContext(), SharedPreferencesKey.KEY_FIRST_START, DevicesUtil.getVersion(this));
            return false;
        }
        ShareDataManager.getInstance().save(getApplicationContext(), SharedPreferencesKey.KEY_FIRST_START, DevicesUtil.getVersion(this));
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        handlerdelay.removeCallbacks(JumpActivity);
//    }

//    @Override
//    protected void onDestroy() {
//        handlerdelay.removeCallbacks(JumpActivity);
//        super.onDestroy();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void initAd() {

        Log.i(TAG, "initAd: "+System.currentTimeMillis());
        /*SplashAd splashAd = new SplashAd(this, frame, new AdListener() {
            @Override
            public void onAdShown() {
                Log.d("lance", "onAdShown");
            }

            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onAdLoaded: "+System.currentTimeMillis());
                Log.d("lance", "onAdLoaded");
            }


            @Override
            public void onAdFailedToLoad(int i) {
                Log.i(TAG, "onAdFailedToLoad: "+System.currentTimeMillis());
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

        splashAd.setCloseButtonPadding(10, 20, 10, 10);*/
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
            System.out.println("----------***** 11111 ******-------------");
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

        //腾讯广告
        canJump = false;

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
        /*if (canJumpImmediately) {
            jumpWhenCanClick();
        }
        canJumpImmediately = true;*/

        //腾讯广告
        if (canJump) {
            next();
        }
        canJump = true;
    }

}
