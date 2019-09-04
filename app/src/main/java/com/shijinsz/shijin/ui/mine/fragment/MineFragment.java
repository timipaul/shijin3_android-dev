package com.shijinsz.shijin.ui.mine.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.AndroidSystemUtil;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.Constants;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.DataBean;
import com.hongchuang.ysblibrary.model.model.bean.MessageBean;
import com.hongchuang.ysblibrary.model.model.bean.ShenmiBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.hongchuang.ysblibrary.utils.NetworkUtil;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.AppInstallReceiver;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.message.MessageActivity;
import com.shijinsz.shijin.ui.mine.CertificationActivity;
import com.shijinsz.shijin.ui.mine.DataCacheActivity;
import com.shijinsz.shijin.ui.mine.FeedbackActivity;
import com.shijinsz.shijin.ui.mine.FollowListActivity;
import com.shijinsz.shijin.ui.mine.MyAdActivity;
import com.shijinsz.shijin.ui.mine.MyCollectionActivity;
import com.shijinsz.shijin.ui.mine.MyLookActivity;
import com.shijinsz.shijin.ui.mine.MyRushActivity;
import com.shijinsz.shijin.ui.mine.MySwopActivity;
import com.shijinsz.shijin.ui.mine.MyVipActivity;
import com.shijinsz.shijin.ui.mine.SettingActivity;
import com.shijinsz.shijin.ui.mine.ShoppingCouponActivity;
import com.shijinsz.shijin.ui.mine.UserDetailActivity;
import com.shijinsz.shijin.ui.task.InviteFriendActivity;
import com.shijinsz.shijin.ui.wallet.PointActivity;
import com.shijinsz.shijin.ui.wallet.PointDetailActivity;
import com.shijinsz.shijin.ui.wallet.WalletActivity;
import com.shijinsz.shijin.utils.BadgeUtil;
import com.shijinsz.shijin.utils.DownloadAPK;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.LoginUtil;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.xiqu.sdklibrary.util.XWUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/30.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.img_notica)
    ImageView imgNotica;
    @BindView(R.id.tv_noread)
    TextView tvNoread;
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_locate)
    TextView tvLocate;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_myad)
    TextView tvMyad;
    @BindView(R.id.tv_conversion)
    TextView tvConversion;
    @BindView(R.id.img_vip)
    ImageView imgVip;
    @BindView(R.id.ln_money)
    LinearLayout lnMoney;
    @BindView(R.id.ln_point)
    LinearLayout lnPoint;
    @BindView(R.id.tv_shenqing)
    TextView tvShenqing;
    @BindView(R.id.ln_myad)
    LinearLayout lnMyad;
    Unbinder unbinder;
    @BindView(R.id.webview)
    WebView webview;

    private String id;

    private static final int KEY_vip_page_code = 100;

    @Override
    protected int provideContentViewId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            id = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
            updata();
            if (Build.VERSION.SDK_INT >= 23) {
                showContacts();
            } else {
                getAd();
            }
        }
    }

    private void getAd() {
        AndroidSystemUtil util = new AndroidSystemUtil(getContext());
        Map bean = new HashMap();
        DataBean databean = new DataBean();
        databean.setVer("1.7.4");
        databean.setAppid("3C871630B97545668AFE47533A13F317");
        databean.setLid("702CB9733DC24A5396FED85AC3790EDD");
        databean.setOs("1");
        databean.setOs("1");
        databean.setOsversion(Build.VERSION.RELEASE);
        databean.setAppversion("1.1.0");
        databean.setAndroidid(util.ID()+"");
        databean.setImei(util.IMEI()+"");
        databean.setMac(util.MAC()+"");
        databean.setAppname("十金");
        databean.setApppackagename("com.shijinsz.shijin");
        databean.setImsi(util.IMSI()+"");
        databean.setNetwork(util.getAPNType()+"");
        databean.setTime(System.currentTimeMillis() + "");
        databean.setScreenwidth(util.Width()+ "");
        databean.setScreenheight(util.Height()+ "");
        databean.setWidth("640");
        databean.setHeight("100");
        databean.setUa(webview.getSettings().getUserAgentString());
        databean.setBrand(Build.BRAND);
        databean.setModel(Build.MODEL);
        databean.setToken(TextUtil.md5(databean.getAppid() + "741208BD872B4AC83607254B70D8DA86" + databean.getLid() + databean.getTime()));
        databean.setIsGP(util.isAppInstalled("com.android.vending") + "");
        databean.setLat(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LAT));
        databean.setLon(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LON));
        Gson gson = new Gson();

        bean.put("data", gson.toJson(databean));
        YSBSdk.getService(OAuthService.class).getad(bean, new YRequestCallback<ShenmiBean>() {
            @Override
            public void onSuccess(ShenmiBean var1) {
                shenmiBean=var1;
                Log.i("SHENMI", "onSuccess: ");
                if (var1.getCount()>0){
                    //webview.setVisibility(View.VISIBLE);
                    webview.loadData(var1.getAds().get(0).getPage(), "text/html; charset=UTF-8", null);
                }else {
                    webview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                Log.i("Shenmi", "onFailed");
                webview.setVisibility(View.GONE);
            }

            @Override
            public void onException(Throwable var1) {
                webview.setVisibility(View.GONE);
                Log.i("Shenmi", "onException");
            }
        });
    }

    private void getMessage() {
        Map map = new HashMap();
        map.put("mode", "message_number");
        YSBSdk.getService(OAuthService.class).get_message(id, map, new YRequestCallback<MessageBean>() {
            @Override
            public void onSuccess(MessageBean var1) {
                if (var1.getTotal_message_number().equals("0")) {
                    tvNoread.setVisibility(View.GONE);
                } else {
                    tvNoread.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(var1.getTotal_message_number()) > 99) {
                        tvNoread.setText("99");
                        //设置图标消息数量提示
                        BadgeUtil.setBadgeCount(getContext(),99);
                    } else {
                        tvNoread.setText(var1.getTotal_message_number());
                        BadgeUtil.setBadgeCount(getContext(),Integer.valueOf(var1.getTotal_message_number()));
                    }
                }
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_total_message_number, var1.getTotal_message_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_asset_total_number, var1.getAsset_total_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_change_number, var1.getChange_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_point_number, var1.getPoint_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_trend_total_number, var1.getTrend_total_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_ad_comment_number, var1.getAd_comment_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_ad_like_number, var1.getAd_like_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_user_like_number, var1.getUser_like_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_notification_number, var1.getNotification_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_circular_total_number, var1.getCircular_total_number());
            }

            @Override
            public void onFailed(String var1, String message) {
//                ErrorUtils.error(getContext(),var1);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        id = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
        updata();
    }

    private void updata() {
        String locate = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LOCATE) + "";

        if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_login).endsWith("on")) {
            tvLocate.setText("未知");
            imgAvatar.setImageResource(R.mipmap.icon_header);
            tvNickname.setText("请先登录");
            tvMoney.setText("0.00");
            tvJifen.setText("0.00");
            tvNoread.setVisibility(View.GONE);
        } else {
            if (locate.isEmpty()) {
                tvLocate.setText("未定位");
            } else {
                tvLocate.setText(locate);
            }
            GlideApp.with(getContext()).load(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)).placeholder(R.mipmap.icon_header).into(imgAvatar);
            tvNickname.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname));
            getMessage();
            getUserInfo();
        }
    }

    private String isAd = "off";

    private void getUserInfo() {
        Map map = new HashMap();
        map.put("mode", "profile");
        YSBSdk.getService(OAuthService.class).getuserinfo(id, map, new YRequestCallback<UserBean.UserDetailBean>() {
            @Override
            public void onSuccess(UserBean.UserDetailBean var1) {
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_change, var1.getChange());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_points, var1.getPoints());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_fan_number, var1.getFan_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_change, var1.getHistory_change());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_points, var1.getHistory_points());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_recharge_change, var1.getHistory_recharge_change());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_recharge_points, var1.getHistory_recharge_points());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_draw_lottery_number, var1.getDraw_lottery_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_imageurl, var1.getImageurl());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_nickname, var1.getNickname());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_birth, var1.getBirth());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_gender, var1.getGender());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_address, var1.getAddress());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_income, var1.getIncome());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_job, var1.getJob());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_interest, var1.getInterests());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_is_advertiser, var1.getIs_advertiser());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_USER_NAME, var1.getUsername());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_withdraw_change_number, var1.getWithdraw_change_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_OPENID, var1.getOpenid());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_service_charge_percent, var1.getService_charge_percent());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_user_vip_state, var1.getMember_status());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_user_vip_end, var1.getExpiration_at());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KET_free_vip_state, var1.getTry_member_status());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_VIP_first_open, var1.getFirst_open_member());

                isAd = var1.getMember_status();

                if (isAd.equals("on")) {
                    imgVip.setVisibility(View.VISIBLE);
                } else {
                    imgVip.setVisibility(View.GONE);
                }
                tvNickname.setText(var1.getNickname());
                GlideApp.with(mActivity).load(var1.getImageurl()).into(imgAvatar);

                tvMoney.setText(var1.getChange());
                tvJifen.setText(var1.getPoints());
                if (isAd.equals("off")) {
                    tvShenqing.setVisibility(View.VISIBLE);
                } else
                    tvShenqing.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String var1, String message) {
//                ErrorUtils.error(getContext(),var1);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }
    private final BroadcastReceiver apkInstallListener = new AppInstallReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){

                    System.out.println("**************Broadcase*************");
                    //System.out.println(uninstallApk.size()+"(*******"+uApks.size());
                    System.out.println("******应用添加***");
                    if (shenmiBean!=null&&shenmiBean.getAds().get(0).getDplink()!=null&&!shenmiBean.getAds().get(0).getDplink().isEmpty()) {
                        if (CanOpenDeeplink(getContext(), shenmiBean.getAds().get(0).getDplink())) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    NetworkUtil.doGet(shenmiBean.getAds().get(0).getTrackingevents().get(0).getTracking().get(0));
                                }
                            }).start();
                            OpenDeeplink(getContext(), shenmiBean.getAds().get(0).getDplink());
                        }
                    }else if (shenmiBean!=null){
                        for (ShenmiBean.AdsBean.Trackingevents trackingevents : shenmiBean.getAds().get(0).getTrackingevents()) {
                            if (trackingevents.getEventtype().equals("installcomplete")){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        NetworkUtil.doGet(trackingevents.getTracking().get(0));
                                    }
                                }).start();
                            }
                        }
                    }
                }
                else  if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
                    System.out.println("*****应用被删除");
                }

                else  if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
                    System.out.println("****应用被替换");
                    if (shenmiBean!=null){
                        for (ShenmiBean.AdsBean.Trackingevents trackingevents : shenmiBean.getAds().get(0).getTrackingevents()) {
                            if (trackingevents.getEventtype().equals("installcomplete")){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        NetworkUtil.doGet(trackingevents.getTracking().get(0));
                                    }
                                }).start();
                            }
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    // 注册监听
    private void registerSDCardListener(){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        try {
            getActivity().registerReceiver(apkInstallListener, intentFilter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ShenmiBean shenmiBean;
    @Override
    protected void loadData() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {

            }
        });
        registerSDCardListener();
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (shenmiBean!=null&&shenmiBean.getAds().get(0).getDplink()!=null&&!shenmiBean.getAds().get(0).getDplink().isEmpty()){
//                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(shenmiBean.getAds().get(0).getDplink()));
//                    if (isDeepLink(shenmiBean.getAds().get(0).getDplink()) && deviceCanHandleIntent(getContext(), intent2)) {
//                        getContext().startActivity(intent2);
//                        return true;
//                    }
                    if (CanOpenDeeplink(getContext(),shenmiBean.getAds().get(0).getDplink())){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                NetworkUtil.doGet(shenmiBean.getAds().get(0).getTrackingevents().get(0).getTracking().get(0));
                            }
                        }).start();
                        OpenDeeplink(getContext(),shenmiBean.getAds().get(0).getDplink());
                    }
                    else {
//                        OpenDeeplink(getContext(),shenmiBean.getAds().get(0).getDplink());
                        if (shenmiBean!=null&&shenmiBean.getAds().get(0).getAction()==2){
                            if (!shenmiBean.getAds().get(0).getApn().isEmpty()){
                                PackageManager pm = getContext().getPackageManager();
                                AndroidSystemUtil androidSystemUtil=new AndroidSystemUtil(getContext());
                                if (androidSystemUtil.isAppInstalled(shenmiBean.getAds().get(0).getApn())==1) {
                                    Intent intent = pm.getLaunchIntentForPackage(shenmiBean.getAds().get(0).getApn());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getContext().startActivity(intent);
                                }else {
                                    showDownloadProgressDialog(getContext(),url);
                                }
                            }else {
                                showDownloadProgressDialog(getContext(),url);
                            }
                        }else {
                            Uri uri= Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            getContext().startActivity(intent);
//                            Intent intent = new Intent(getContext(), AdWebViewActivity.class);
//                            intent.putExtra("url",url);
//                            startActivity(intent);
                        }
                    }
                }else {
                    if (shenmiBean!=null&&shenmiBean.getAds().get(0).getAction()==2){
                        if (!shenmiBean.getAds().get(0).getApn().isEmpty()){
                            PackageManager pm = getContext().getPackageManager();
                            AndroidSystemUtil androidSystemUtil=new AndroidSystemUtil(getContext());
                            if (androidSystemUtil.isAppInstalled(shenmiBean.getAds().get(0).getApn())==1) {
                                try{
                                    Intent intent = pm.getLaunchIntentForPackage(shenmiBean.getAds().get(0).getApn());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getContext().startActivity(intent);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else {
                                showDownloadProgressDialog(getContext(),url);
                            }
                        }else {
                            showDownloadProgressDialog(getContext(),url);
                        }
                    }else {
                        Uri uri= Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        getContext().startActivity(intent);
//                        Intent intent = new Intent(getContext(), AdWebViewActivity.class);
//                        intent.putExtra("url",url);
//                        startActivity(intent);
                    }
                }
                return true;
            }

        });

//        updata();

    }
    public  boolean isDeepLink(final String url) {
        return !isHttpUrl(url);
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(apkInstallListener);
        }catch (Exception error){

        }
        super.onDestroy();
    }

    public  boolean deviceCanHandleIntent(final Context context, final Intent intent) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            final List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            return !activities.isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }
    //判断是否支持deeplink
    public boolean CanOpenDeeplink(Context context, String deeplink){

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> infos= packageManager.queryIntentActivities(intent,0);
        return !infos.isEmpty();
    }


    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            getAd();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 100:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getAd();
                    } else {
                        webview.setVisibility(View.GONE);
//                    ToastUtil.showToas;
                        //没有获取到权限，做特殊处理
                    }
                }
                break;
            default:
                break;
        }
    }


    //调起deeplink
    public void OpenDeeplink(Context context, String deeplink){
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static boolean isHttpUrl(final String url) {
        if (url == null) {
            return false;
        }

        final String scheme = Uri.parse(url).getScheme();
        return ("http".equals(scheme) || "https".equals(scheme));
    }
    @OnClick({R.id.ln_money, R.id.ln_point, R.id.img_notica, R.id.img_avatar, R.id.ln_myad,
            R.id.tv_vip, R.id.tv_looked, R.id.tv_conversion,R.id.tv_gz,
            R.id.tv_sc, R.id.tv_yj, R.id.tv_sj, R.id.tv_sz, R.id.tv_shenqing, R.id.tv_tg,
            R.id.tv_nickname,R.id.tv_rush,R.id.tv_coupon,R.id.gift_conversion,R.id.invite_friend,R.id.my_game})

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_money:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), WalletActivity.class));
                break;
            case R.id.ln_point:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), PointDetailActivity.class));
                break;

            case R.id.img_notica:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), MessageActivity.class));
                break;
            case R.id.img_avatar:
            case R.id.tv_nickname:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                Intent intent = new Intent(getContext(), UserDetailActivity.class);
                intent.putExtra("id", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID));
                startActivity(intent);
                break;
            case R.id.ln_myad:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
//                if (isAd.equals("off")) {
//                    startActivity(new Intent(getContext(), CertificationActivity.class));
//                } else
                startActivity(new Intent(getContext(), MyAdActivity.class));

                break;
            case R.id.tv_vip:
                //会员机制
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivityForResult(new Intent(getContext(), MyVipActivity.class),KEY_vip_page_code);

                break;
            case R.id.tv_looked:
                //我看过的
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), MyLookActivity.class));
                break;
            case R.id.tv_conversion:
                //我兑换的
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), MySwopActivity.class));
                break;
            case R.id.tv_rush:
                //我抢购的
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), MyRushActivity.class));
                break;
            case R.id.tv_gz:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), FollowListActivity.class));
                break;
            case R.id.tv_sc:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), MyCollectionActivity.class));
                break;
            case R.id.tv_yj:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), FeedbackActivity.class));
                //Beta.applyDownloadedPatch();

                break;
            case R.id.tv_sj:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), DataCacheActivity.class));
                break;
            case R.id.tv_sz:
                //Beta.checkUpgrade();
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.tv_tg:

//                Beta.checkUpgrade();
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                Uri uri = Uri.parse("https://weidian.com/?distributorid=1392220866&userid=1392220866&ifr=shopdetail&wfr=wx&src=shop");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
//                Intent intent2 = new Intent(mActivity, WebViewActivity.class);
//                intent2.putExtra("url", "https://weidian.com/?distributorid=1392220866&userid=1392220866&ifr=shopdetail&wfr=wx&src=shop");
//                intent2.putExtra("title", "十金特供");
                startActivity(intent2);
                break;
            case R.id.tv_shenqing:
//                Beta.checkUpgrade();
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), CertificationActivity.class));
                break;
            case R.id.tv_coupon:
                //优惠劵
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                //优惠劵
                startActivity(new Intent(getContext(), ShoppingCouponActivity.class));
                //优惠劵记录
                //startActivity(new Intent(getContext(), CouponRecordActivity.class));
                break;

            case R.id.gift_conversion:
                //礼品兑换
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(getContext(), PointActivity.class));
                break;
            case R.id.invite_friend:
                //邀请好友
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(mActivity, InviteFriendActivity.class));
                break;
            case R.id.my_game:
                //推广游戏
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }

                //嘻趣游戏
                String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
                XWUtils.getInstance(getContext()).init(Constants.XIQU_APPID,Constants.XIQU_SECRET,username);
                //跳转进入广告展示页面
                XWUtils.getInstance(getContext()).jumpToAd();

                break;
        }
    }
    private void showDownloadProgressDialog(Context context, String url) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        String downloadUrl = url;
        DownloadAPK downloadAPK=new DownloadAPK(getContext(),progressDialog);
        if (shenmiBean!=null){
            for (ShenmiBean.AdsBean.Trackingevents trackingevents : shenmiBean.getAds().get(0).getTrackingevents()) {
                if (trackingevents.getEventtype().equals("downloadstart")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NetworkUtil.doGet(trackingevents.getTracking().get(0));
                        }
                    }).start();
                }
            }
        }
        downloadAPK.setDownloadAPK(new DownloadAPK.onDownloadAPK() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                if (shenmiBean!=null){
                    for (ShenmiBean.AdsBean.Trackingevents trackingevents : shenmiBean.getAds().get(0).getTrackingevents()) {
                        if (trackingevents.getEventtype().equals("downloadcomplete")){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    NetworkUtil.doGet(trackingevents.getTracking().get(0));
                                }
                            }).start();
                        }
                    }
                }
            }

            @Override
            public void onOpen() {
                if (shenmiBean!=null){
                    for (ShenmiBean.AdsBean.Trackingevents trackingevents : shenmiBean.getAds().get(0).getTrackingevents()) {
                        if (trackingevents.getEventtype().equals("installstart")){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    NetworkUtil.doGet(trackingevents.getTracking().get(0));
                                }
                            }).start();
                        }
                    }
                }
            }
        });
        downloadAPK.execute(downloadUrl);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == KEY_vip_page_code && resultCode == 404){
            //跳转VIP页面
            ((MainActivity)MActivityManager.getInstance().getActivity(MainActivity.class)).setCurrentItem(0);
        }
    }
}
