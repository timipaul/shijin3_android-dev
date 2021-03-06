package com.shijinsz.shijin.ui.home.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.CategoriesBean;
import com.hongchuang.ysblibrary.model.model.bean.ShenmiBean;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.activity.UnifiedInterstitialADActivity;
import com.shijinsz.shijin.ui.home.NewGuideActivity;
import com.shijinsz.shijin.ui.home.SearchActivity;
import com.shijinsz.shijin.ui.home.adapter.MainTabAdapter;
import com.shijinsz.shijin.ui.task.ChallengeTaskActivity;
import com.shijinsz.shijin.ui.task.SignInActivity;
import com.shijinsz.shijin.ui.wallet.PointActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.LocationUtils;
import com.shijinsz.shijin.utils.LoginUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/30.
 */

public class HomeFragment extends BaseFragment{

    @BindView(R.id.tv_locate)
    TextView tvLocate;
    @BindView(R.id.tv_guide)
    ImageView tvGuide;
    @BindView(R.id.tv_selete)
    TextView tvSelete;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.gif_img)
    ImageView mGif_img;

    public String ad_state = "off";

    public LocationClient mLocationClient = null;

    public UnifiedInterstitialADActivity adActivity;

    private CommonNavigator commonNavigator;
    private static final String[] CHANNELS = new String[]{"关注","爱分享","推荐"};
    private List<String> mDataList = new ArrayList<String>(Arrays.asList(CHANNELS));
    private final static String TAG = "HomeFragment";
    @Override
    protected int provideContentViewId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void loadData() {
        mInflater = LayoutInflater.from(getContext());
        initView();
    }

    public String getAd_state(){
        return ad_state;
    }

    public void setAd_state(String ad){
        ad_state = ad;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    private View footView1;
    private boolean isFirst = true;
    private List<Fragment> mFragments = new ArrayList<>();
    private void initView() {

        footView1 = mInflater.inflate(R.layout.empty_layout, null);
        View view = mInflater.inflate(R.layout.home_header, null);
        mFragments.add(NewListFragment.getInstance("follow"));
        mFragments.add(LoveShareFragment.getInstance("loveShare"));
        mFragments.add(NewListFragment.getInstance("individuation"));
//        tvGuide = view.findViewById(R.id.tv_guide);
        tvGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogUtils(getContext()).showIssueMenuDialog();

            }
        });

        tvSelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getChannel();
            }
        });
        getChannel();

        //开启定位
        mLocationClient = new LocationClient(getContext());
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);

        showContacts();

        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if (bdLocation==null||bdLocation.getLatitude()==0||(bdLocation.getLatitude()+"").isEmpty()) {
                    ShareDataManager.getInstance().save(getContext(),SharedPreferencesKey.KEY_LOCATION,"");
                    ShareDataManager.getInstance().save(getContext(),SharedPreferencesKey.KEY_LON,"");
                    ShareDataManager.getInstance().save(getContext(),SharedPreferencesKey.KEY_LAT,"");
                }else {
                    ShareDataManager.getInstance().save(getContext(),SharedPreferencesKey.KEY_LOCATION,bdLocation.getLongitude()+","+bdLocation.getLatitude());
                    ShareDataManager.getInstance().save(getContext(),SharedPreferencesKey.KEY_LON,bdLocation.getLongitude()+"");
                    ShareDataManager.getInstance().save(getContext(),SharedPreferencesKey.KEY_LAT,bdLocation.getLatitude()+"");
                }
                String city = bdLocation.getCity();

                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_LOCATE, city);
                if (null != city && !city.isEmpty()) {
                    if (city.length() > 3) {
                        city = city.substring(0, 3) + "..";
                    }
                    //tvLocate.setText(city + "");
                    //mLocationClient.stop();
                } else {
                    tvLocate.setText("未定位");
                }
            }
        });

        //动态图片
        Glide.with(mActivity).load(R.drawable.vote_gif_img).into(mGif_img);

        adActivity = new UnifiedInterstitialADActivity(getActivity());
        adActivity.loadAd();
        ifGameIntoEntrance();

    }


    //判断手机型号 隐藏广告弹窗入口
    public void ifGameIntoEntrance(){
        if(Build.MANUFACTURER.toLowerCase().contains("huawei")){
            YSBSdk.getService(OAuthService.class).getGameStatue(new YRequestCallback<ShenmiBean>() {
                @Override
                public void onSuccess(ShenmiBean var1) {
                    adActivity.loadAd();
                    setTimer();
                }

                @Override
                public void onFailed(String var1, String message) {
                    Log.i("game", "onFailed");

                }

                @Override
                public void onException(Throwable var1) {
                    Log.i("game", "onException");
                }
            });
        }else{
            adActivity.loadAd();
            setTimer();
        }
    }


    private static final int TIMER = 999;
    private static boolean flag = true;
    private void setTimer(){
        Message message = mHandler.obtainMessage(TIMER);     // Message
        mHandler.sendMessageDelayed(message, 20000);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIMER:
                    //在这里去执行定时操作逻辑

                    if (flag) {
                        adActivity.showAd();
                        stopTimer();
                        Message message = mHandler.obtainMessage(TIMER);
                        //mHandler.sendMessageDelayed(message, 5000);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void stopTimer(){
        flag = false;
    }

    private void getChannel() {
        YSBSdk.getService(OAuthService.class).categories(new YRequestCallback<BaseBean<CategoriesBean>>() {
            @Override
            public void onSuccess(BaseBean<CategoriesBean> var1) {
                for (CategoriesBean categoriesBean : var1.getCategories()) {
                     mDataList.add(categoriesBean.getValue());
                     mFragments.add(NewListFragment.getInstance(categoriesBean.getKey()));
                }
                initviewPage();
            }

            @Override
            public void onFailed(String var1, String message) {
                initviewPage();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showRetry();
//                initviewPage();
            }
        });
    }

    public void initviewPage(){
        MainTabAdapter mTabAdapter = new MainTabAdapter(mFragments, getChildFragmentManager());
        viewpager.setAdapter(mTabAdapter);
        viewpager.setOffscreenPageLimit(mFragments.size());
        viewpager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setSkimOver(true);
        commonNavigator.setLeftPadding(UIUtil.dip2px(getContext(),20));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int i) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(i));
                clipPagerTitleView.setTextSize(UIUtil.dip2px(getContext(),16));
                clipPagerTitleView.setTextColor(getContext().getResources().getColor(R.color.text_33));
                clipPagerTitleView.setClipColor(getContext().getResources().getColor(R.color.color_ffb));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(i);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        indicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(getContext(), 18);
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                viewpager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });
        indicator.onPageScrollStateChanged(1);
        viewpager.setCurrentItem(2,false);

    }

    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();

            String[] mPermissionList = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE};

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            } else {

            }

            if (EasyPermissions.hasPermissions(getContext(), mPermissionList)) {

                //已经同意过
                mLocationClient.start();
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        getString(R.string.need_site), //提示文言
                        100, //请求码
                        mPermissionList //权限列表
                );
            }

           // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            mLocationClient.start();
            try {
                LocationUtils locat = new LocationUtils(getContext());
                locat.getCity(tvLocate);
            }catch (Exception e) {

            }
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
                        mLocationClient.start();
                    } else {
//                    ToastUtil.showToas;
                        //没有获取到权限，做特殊处理
                    }
                }
                break;
            case 200:
                if (grantResults != null) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        isFirst = false;
                    } else {
//                    ToastUtil.showToas;
                        //没有获取到权限，做特殊处理
                    }
                }
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.gif_img})
    public void onGoodsClick(View view){
        switch (view.getId()){
            case R.id.gif_img:
                Log.i(TAG,"跳转到抽奖");
                //跳转到签到抽奖
                if(LoginUtil.isLogin(mActivity)){
                    startActivity(new Intent(mActivity, SignInActivity.class));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adActivity.Destroy();

    }
}
