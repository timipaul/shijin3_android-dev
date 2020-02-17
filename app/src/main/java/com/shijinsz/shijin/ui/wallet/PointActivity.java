package com.shijinsz.shijin.ui.wallet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.hongchuang.ysblibrary.utils.NetworkUtil;
import com.hongchuang.ysblibrary.widget.NoScrollViewPager;
import com.meituan.android.walle.WalleChannelReader;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.adapter.PointTabAdapter;
import com.shijinsz.shijin.ui.mine.fragment.CommonFragment;
import com.shijinsz.shijin.ui.mine.fragment.VipFragment;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2019/5/8.
 * authon paul
 * 金币兑换
 */

public class PointActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.vp_content)
    NoScrollViewPager vpContent;
    @BindView(R.id.tv_swop)
    TextView tvSwop;
    @BindView(R.id.rg_conversion)
    RadioGroup rgConversion;
    @BindView(R.id.rb_general)
    RadioButton rb_general;
    @BindView(R.id.rb_vip)
    RadioButton rb_vip;
    @BindView(R.id.vi_line_1)
    View line1;
    @BindView(R.id.vi_line_2)
    View line2;

    //接收跳转过来的状态
    private int request_code = 0;

    @Override
    public int bindLayout() {
        return R.layout.point_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private Map<String, List<String>> map = new HashMap<>();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            result = NetworkUtil.doGet("http://api.fir.im/apps/latest/" + getResources().getString(R.string.FIR_ID) + "?api_token=" + getResources().getString(R.string.FIR_TOKEN));
            verifyStoragePermissions(mActivity);
//            ToastUtil.showToast("当前渠道："+channel);
            map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/ios_switch/ios.txt");
            Message msg = new Message();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
//                Log.d(TAG, "handleMessage: " + result);
//                Gson gson = new Gson();
//                FirResult bean = gson.fromJson(result, FirResult.class);
                if (map != null) {
                    if (map.get("x-oss-meta-android") != null && !map.get("x-oss-meta-android").get(0).isEmpty()) {
                        if (map.get("x-oss-meta-android").get(0).equals("off")){

                        }else {

                        }
                    }
                }

            }
        }
    };

    private void getUserInfo() {
        Map map = new HashMap();
        map.put("mode", "profile");
        YSBSdk.getService(OAuthService.class).getuserinfo(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID), map, new YRequestCallback<UserBean.UserDetailBean>() {
            @Override
            public void onSuccess(UserBean.UserDetailBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_change, var1.getChange());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_points, var1.getPoints());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_fan_number, var1.getFan_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_change, var1.getHistory_change());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_points, var1.getHistory_points());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_recharge_change, var1.getHistory_recharge_change());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_recharge_points, var1.getHistory_recharge_points());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draw_lottery_number, var1.getDraw_lottery_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_imageurl, var1.getImageurl());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_nickname, var1.getNickname());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_birth, var1.getBirth());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_gender, var1.getGender());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_address, var1.getAddress());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_income, var1.getIncome());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_job, var1.getJob());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_interest, var1.getInterests());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_advertiser, var1.getIs_advertiser());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_USER_NAME, var1.getUsername());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_withdraw_change_number, var1.getWithdraw_change_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_OPENID, var1.getOpenid());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_one_withdraw_status, var1.getOne_withdraw_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_five_withdraw_status, var1.getFirst_in_tantan_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_eight_withdraw_status, var1.getEight_withdraw_status());
                money.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points));

                tvSwop.setText(String.format(mContext.getString(R.string.day_rate),500+""));

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
    public void initView(View view) {
        try {
            //接收会员页面的参数
            Bundle data = getIntent().getExtras();
            request_code = data.getInt("code");
        }catch (Exception e) {
        }

        String channel = WalleChannelReader.getChannel(mContext.getApplicationContext());
        if (channel!=null&&channel.equals("360")){
            new Thread(runnable).start();
        }
        StatusBarUtil.setStatusTextColor(true, mActivity);
        money.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points));

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new CommonFragment());
        mFragments.add(new VipFragment());
        PointTabAdapter mTabAdapter = new PointTabAdapter(mFragments, getSupportFragmentManager());
        vpContent.setAdapter(mTabAdapter);
        vpContent.setOffscreenPageLimit(mFragments.size());

        if(request_code == 1){
            rb_vip.setChecked(true);
            setBelowLine(request_code);
            vpContent.setCurrentItem(request_code, false);
        }

        rgConversion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position = request_code;
                switch (checkedId){
                    case R.id.rb_general:
                        //普通兑换
                        position = 0;
                        break;
                    case R.id.rb_vip:
                        //会员兑换
                        position = 1;
                        break;
                }
                setBelowLine(position);
                vpContent.setCurrentItem(position, false);
            }
        });

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG,"-----ViewPager----2222----");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.tv_change_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                startActivity(PointDetailActivity.class);
                break;
            case R.id.tv_change_change:
                startActivity(ChangeChangeActivity.class);
                break;
        }
    }

    //设置下划线位置
    private void setBelowLine(int code){
        if(code == 0){
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
        }else{
            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.VISIBLE);
        }
    }


}
