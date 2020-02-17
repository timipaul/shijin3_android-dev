package com.shijinsz.shijin.ui.wallet;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/29.
 */

public class WalletActivity extends BaseActivity {
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_withdraw)
    TextView tvWithdraw;
    @BindView(R.id.record)
    TextView record;
    @BindView(R.id.ln1)
    LinearLayout ln1;
    @BindView(R.id.ln_point)
    LinearLayout lnPoint;
    @BindView(R.id.ln_shijin_box)
    LinearLayout lnShijinBox;
    @BindView(R.id.tv_point)
    TextView tvPoint;

    @Override
    public int bindLayout() {
        return R.layout.wallet_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }
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
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_one_withdraw_status,var1.getOne_withdraw_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_five_withdraw_status,var1.getFirst_in_tantan_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_eight_withdraw_status,var1.getEight_withdraw_status());
                money.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
                tvPoint.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points));
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
        StatusBarUtil.setStatusTextColor(true, mActivity);
      }


    @OnClick({R.id.iv_back, R.id.tv_withdraw, R.id.record, R.id.ln_point, R.id.ln_shijin_box})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_withdraw:
                startActivity(WithdrawActivity.class);
                break;
            case R.id.record:
                startActivity(WalletDetailActivity.class);
                break;
            case R.id.ln_point:
                startActivity(PointActivity.class);
                break;
            case R.id.ln_shijin_box:
                startActivity(BoxListActivity.class);
                break;
        }
    }

}
