package com.shijinsz.shijin;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DataCleanManager;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.BottomNavigationViewEx;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.PointDetailBean;
import com.hongchuang.ysblibrary.utils.NetworkUtil;
import com.hongchuang.ysblibrary.widget.NoScrollViewPager;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.meituan.android.walle.WalleChannelReader;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.home.adapter.MainTabAdapter;
import com.shijinsz.shijin.ui.home.fragment.HomeFragment;
import com.shijinsz.shijin.ui.mine.fragment.MineFragment;
import com.shijinsz.shijin.ui.store.StoreHomeActivity;
import com.shijinsz.shijin.ui.task.fragment.TaskFragment;
import com.shijinsz.shijin.ui.video.fragment.VideoFragment;
import com.shijinsz.shijin.utils.APKVersionCodeUtils;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ImgUtils;
import com.shijinsz.shijin.utils.LoginUtil;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.xiqu.sdklibrary.util.AppUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit.callback.YRequestCallback;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;
    @BindView(R.id.vp_content)
    NoScrollViewPager vpContent;
    @BindView(R.id.activity_with_view_pager)
    RelativeLayout activityWithViewPager;
    @BindView(R.id.img_ad)
    ImageView imgAd;
    @BindView(R.id.pass)
    TextView pass;
    @BindView(R.id.open)
    FrameLayout open;
    private CountDownTimer timer;

    long time = 0;
    private int previousPosition = -1;



    @Override
    public int bindLayout() {
        return R.layout.main_activity;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        initBnve();
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new VideoFragment());
        //mFragments.add(new StoreFragment());
        mFragments.add(new TaskFragment());
        mFragments.add(new MineFragment());
        MainTabAdapter mTabAdapter = new MainTabAdapter(mFragments, getSupportFragmentManager());
        vpContent.setAdapter(mTabAdapter);
        vpContent.setOffscreenPageLimit(mFragments.size());
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int position = 0;
                switch (item.getItemId()) {
                    case R.id.i_music:
                        position = 0;
                        break;
                    case R.id.i_backup:
                        position = 1;
                        break;
                    case R.id.i_favor:
                        position = 2;
                        break;
                    case R.id.i_visibility:
                        position = 3;
                        break;
                }

//                if(position == -1){
//                    Intent intent = new Intent(mContext, StoreHomeActivity.class);
//                    startActivity(intent);
//                    return true;
//                }
                if(position == 1){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
                }else{
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                if (previousPosition != position) {
                    vpContent.setCurrentItem(position, false);
                    previousPosition = position;
                    Log.i(TAG, "-----bnve-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");
                }
                return true;
            }
        });
        System.out.println("哪里报错了3");
        // set listener to change the current checked item of bottom nav when scroll view pager
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "-----ViewPager-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");
                //if (position >= 2)// 2 is center
                    //position++;// if page is 2, need set bottom item to 3, and the same to 3 -> 4
                bnve.setCurrentItem(position);
                previousPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        System.out.println("哪里报错了4");
        /*fab.setOnLongTouchListener(new SectorMenuButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("off")) {
                    startActivity(CertificationActivity.class);
                    return;
                }
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                    ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                    return;
                }
                startActivity(NewAdActivity.class);
            }
        }, 1500);*/
        /*fab.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
//                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("off")) {
//                    startActivity(CertificationActivity.class);
//                    return;
//                }
                Log.e(TAG, "onButtonClicked: " + index);
                switch (index) {
                    case 1:
                        startActivity(DataStatisticsActivity.class);
                        break;
                    case 2:
                        startActivity(MyPutActivity.class);
                        break;
                    case 3:
                        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                            ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                            return;
                        }
                        startActivity(DraftActivity.class);
                        break;
                    case 4:
                        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                            ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                            return;
                        }
                        startActivity(NewAdActivity.class);
                        break;
                }
            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onCollapse() {

            }
        });*/

        // center item click listener
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        //新手红包弹框
        /*if (timer == null) {
            if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_login).equals("on")) {
                new DialogUtils(mContext).showNewPacketDialog();
            }
        }*/


        //每日登陆弹框
        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_login).equals("on")){
            //判断今日是否弹框  已弹就不再弹
            if(!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_open_pop).equals(TimeUtil.formatDate())){
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_open_pop,TimeUtil.formatDate());
                if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_user_vip_state).equals("on")){
                    new DialogUtils(mContext).showOpenVipDialog();

                }else{
                    DialogUtils dialogUtils = new DialogUtils(mContext);
                    dialogUtils.showOpenCommonDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //获取每日签到加金币
                            setGodlData();
                            dialogUtils.dismissOpenCommonDialog();
                            //打开一个新的弹框
                            //dialogUtils.showopenGetGoldDialog();
                        }
                    });
                }
            }
        }
        System.out.println("哪里报错了5");
        //判断用户是否同意协议
        if(!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consent_protocol).equals("true")){
            DialogUtils dialogUtils = new DialogUtils(mContext);
            dialogUtils.showUserAgreementDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consent_protocol,"true");
                    dialogUtils.dismissUserAgreementDialog();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogUtils.dismissUserAgreementDialog();
                    finish();
                }
            });
        }

        if (getIntent().getStringExtra("id") != null) {
//            bnve.setCurrentItem(3);
            vpContent.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("id")), false);
        }
        System.out.println("哪里报错了6");
        getOpenImg();
        new Thread(runnable).start();

    }

    //每日加金币
    public void setGodlData(){
        Map map = new HashMap();
        map.put("user_id",ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID));
        //points_add
        YSBSdk.getService(OAuthService.class).points_add(map, new YRequestCallback<PointDetailBean>() {
            @Override
            public void onSuccess(PointDetailBean var1) {
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataCleanManager.cleanInternalCache(mContext);
        if (timer != null)
            timer.cancel();
    }

    //控制父页面 显示
    public void setCurrentItem(int i) {
        vpContent.setCurrentItem(i, true);
    }

    private void initBnve() {
        bnve.setItemIconTintList(null);
        bnve.enableItemShiftingMode(false);
        bnve.enableShiftingMode(false);
        bnve.enableAnimation(false);
        bnve.setIconSize(20, 20);
        bnve.setTextSize(11);
        bnve.setTextTintList(0, getResources().getColorStateList(R.color.home_bottom));
        bnve.setTextTintList(1, getResources().getColorStateList(R.color.home_bottom));
        bnve.setTextTintList(2, getResources().getColorStateList(R.color.home_bottom));
        bnve.setTextTintList(3, getResources().getColorStateList(R.color.home_bottom));
        //bnve.setTextTintList(4, getResources().getColorStateList(R.color.home_bottom));
        bnve.setItemHeight(BottomNavigationViewEx.dp2px(this, 52));
        bnve.setIconsMarginTop(BottomNavigationViewEx.dp2px(this, 10));

    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long mExitTime;

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, getString(R.string.exit_again), Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            MActivityManager.getInstance().delAllActivity();
//            finish();
//            System.exit(0);
        }
    }

    private void getOpenImg() {
        Map map = new HashMap();
        map.put("status", "on");
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).start_pages(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_start_page_url, var1.getStart_page_url());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_jump_mode, var1.getJump_mode());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_aid, var1.getAid());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_time_length, var1.getTime_length());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_start_id, var1.getId());
                urls = var1.getStart_page_imgurl();
                requestPermission();
            }

            @Override
            public void onFailed(String var1, String message) {
            }

            @Override
            public void onException(Throwable var1) {
            }
        });
    }

    /**
     * 请求读取sd卡的权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE/*,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE*/};
            if (EasyPermissions.hasPermissions(getApplicationContext(), mPermissionList)) {
                //已经同意过
                saveImage();
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
            saveImage();
        }
    }

    String urls;

    /**
     * 保存图片
     */
    private void saveImage() {
//
        Glide.with(mContext).load(urls).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = null;
                BitmapDrawable bd = (BitmapDrawable) resource;
                bitmap = bd.getBitmap();
                if (TextUtil.isNotEmpty(ImgUtils.saveBitmapToLocal(mContext, bitmap))) {
                    Log.e(TAG, "保存图片成功");
                } else {
                    Log.e(TAG, "保存图片失败，请稍后重试");
                }
//                if (bitmap!=null&&!bitmap.isRecycled()) {
//                    bitmap.recycle();
//                }
            }
        });

    }


    //授权结果，分发下去
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //跳转到onPermissionsGranted或者onPermissionsDenied去回调授权结果
        if (requestCode == 10001)
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == 10001)
            saveImage();
    }

    //拒绝授权
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 10001) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                //打开系统设置，手动授权
                new AppSettingsDialog.Builder(this).build().show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作
            saveImage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_login).equals("on")) {
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_first_login_status, "off");
            if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_new_one_status).equals("on")) {
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_click_status).equals("on")) {
                    ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_click_status, "off");
                    new DialogUtils(mContext).getNewRed();
                }
            }
        }
    }

    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    private int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 111;
    private int REQUEST_SCAN_RESULT = 100;
    private NoticeDialog mailBoxDialog;
    private NoticeDialog mailBoxDialog2;
    //fir更新
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
//                Log.d(TAG, "handleMessage: " + result);
//                Gson gson = new Gson();
//                FirResult bean = gson.fromJson(result, FirResult.class);
                int build = 0;
                if (map != null) {
                    if (map.get("x-oss-meta-code") != null && !map.get("x-oss-meta-code").get(0).isEmpty()) {
                        build = Integer.parseInt(map.get("x-oss-meta-code").get(0));
                    }

                    if (build > APKVersionCodeUtils.getVersionCode(mContext)) {
                        showDownLoad();
                    }
                }

            }
        }
    };

    public void showDownLoad() {
        RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.new_version_diglog, null);
        mailBoxLay.findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(mActivity);
                String channel = WalleChannelReader.getChannel(mContext.getApplicationContext());
                switch (channel + "") {
                    case "360":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-360.apk");
                        break;
                    case "ali":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-ali.apk");
                        break;
                    case "baidu":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-baidu.apk");
                        break;
                    case "huawei":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-huawei.apk");
                        break;
                    case "meizu":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-meizu.apk");
                        break;
                    case "oppo":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-oppo.apk");
                        break;
                    case "other":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-other.apk");
                        break;
                    case "sougou":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-sougou.apk");
                        break;
                    case "vivo":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-vivo.apk");
                        break;
                    case "xiaomi":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-xiaomi.apk");
                        break;
                    case "yingyongbao":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-yingyongbao.apk");
                        break;
                    case "debug":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-debug.apk");
                        break;
                    case "qudao1":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao1.apk");
                        break;
                    case "qudao2":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao2.apk");
                        break;
                    case "qudao3":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao3.apk");
                        break;
                    case "qudao4":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao4.apk");
                        break;
                    case "qudao5":
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao5.apk");
                        break;
                    default:
                        showDownloadProgressDialog(MainActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release.apk");
                        break;
                }
            }
        });
        String str2 = new String(Base64.decode(map.get("x-oss-meta-content").get(0).getBytes(), Base64.DEFAULT));
        TextView tv_version = mailBoxLay.findViewById(R.id.tv_version);
        tv_version.setText(getString(R.string.new_version) + map.get("x-oss-meta-version").get(0));
        if (map.get("x-oss-meta-force") != null) {
            if (map.get("x-oss-meta-force").get(0) != null) {
                if (map.get("x-oss-meta-force").get(0).equals("1")) {
                    isForce = true;
                }
            }
        }
        TextView tv_content = mailBoxLay.findViewById(R.id.tv_content);
        tv_content.setText(str2);
        mailBoxDialog2 = new NoticeDialog(mContext);
        mailBoxDialog2.showDialog(mailBoxLay, 0, 0);
        if (isForce) {
            mailBoxDialog2.setCancelable(false);
        } else {
            mailBoxDialog2.setCancelable(true);
        }
    }

    String result = "";
    private Map<String, List<String>> map = new HashMap<>();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            result = NetworkUtil.doGet("http://api.fir.im/apps/latest/" + getResources().getString(R.string.FIR_ID) + "?api_token=" + getResources().getString(R.string.FIR_TOKEN));
            verifyStoragePermissions(mActivity);
            String channel = WalleChannelReader.getChannel(mContext.getApplicationContext());
//            ToastUtil.showToast("当前渠道："+channel);
            ShareDataManager.getInstance().save(mActivity, SharedPreferencesKey.KEY_LOCATE, channel);
            switch (channel + "") {
                case "360":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-360.apk");
                    break;
                case "ali":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-ali.apk");
                    break;
                case "baidu":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-baidu.apk");
                    break;
                case "huawei":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-huawei.apk");
                    break;
                case "meizu":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-meizu.apk");
                    break;
                case "oppo":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-oppo.apk");
                    break;
                case "other":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-other.apk");
                    break;
                case "sougou":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-sougou.apk");
                    break;
                case "vivo":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-vivo.apk");
                    break;
                case "xiaomi":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-xiaomi.apk");
                    break;
                case "yingyongbao":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-yingyongbao.apk");
                    break;
                case "debug":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-debug.apk");
                    break;
                case "qudao1":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao1.apk");
                    break;
                case "qudao2":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao2.apk");
                    break;
                case "qudao3":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao3.apk");
                    break;
                case "qudao4":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao4.apk");
                    break;
                case "qudao5":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-qudao5.apk");
                    break;
                default:
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release.apk");
                    break;
            }
            Message msg = new Message();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }
    };

    private void showDownloadProgressDialog(Context context, String url) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(getString(R.string.ps));
        progressDialog.setMessage(getString(R.string.downloading));
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        String downloadUrl = url;
        new DownloadAPK(progressDialog).execute(downloadUrl);
    }

    @OnClick({R.id.pass, R.id.open,R.id.i_store})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.i_store:
                Intent intent = new Intent(mContext, StoreHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.pass:
//                timer.onFinish();
//                timer.cancel();
//                open.setVisibility(View.GONE);
                break;
            case R.id.open:
//                timer.cancel();
//                clickBanner(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_start_id));
//                startActivity(MainActivity.class);
//                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_jump_mode).equals("inside")) {
//                    Intent intent = new Intent(mContext, VideoDetailActivity.class);
//                    intent.putExtra("id", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_aid));
//                    startActivity(intent);
//                } else {
//                    Uri uri = Uri.parse(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_start_page_url));
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
////                    Intent intent = new Intent(mContext, WebViewActivity.class);
////                    intent.putExtra("url", );
////                    startActivity(intent);
//                }
//                timer.onFinish();
//                timer.cancel();
//                open.setVisibility(View.GONE);
                break;
        }
    }

    public void clickBanner(String id) {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).start_click(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    public boolean isForce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 下载APK的异步任务
     */

    public class DownloadAPK extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        File file;

        public DownloadAPK(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;

            try {
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                bis = new BufferedInputStream(conn.getInputStream());
                int fileLength = conn.getContentLength();
                String fileName = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        .getAbsolutePath() + "/magkare/action.apk";
                file = new File(fileName);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                byte data[] = new byte[4 * 1024];
                long total = 0;
                int count;
                while ((count = bis.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    fos.write(data, 0, count);
                    fos.flush();
                }
                fos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            openFile(file);
            progressDialog.dismiss();
        }

        private void openFile(File file) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(activity,"包名.fileprovider", takeImageFile));
//            } else {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takeImageFile));
//            }
            if (file != null) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    // 声明需要的零时权限
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // 第二个参数，即第一步中配置的authorities
                   // Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    Uri contentUri = FileProvider.getUriForFile(mContext, "con.shijinsz.shijin" + ".fileprovider", file);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                }
                startActivity(intent);
            }

        }
    }
}
