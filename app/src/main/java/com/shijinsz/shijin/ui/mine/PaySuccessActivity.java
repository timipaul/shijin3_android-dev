package com.shijinsz.shijin.ui.mine;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.wallet.PointActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/15.
 */

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.tv_redbag_num)
    TextView tvRedbagNum;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.return_list)
    Button returnList;
    @BindView(R.id.tv_paysuccess)
    TextView tvPaysuccess;
    @BindView(R.id.tv_money2)
    TextView tvMoney2;
    @BindView(R.id.pay_type2)
    LinearLayout payType2;
    @BindView(R.id.pay_type)
    LinearLayout payType;
    private  String type;
    @Override
    public int bindLayout() {
        return R.layout.pay_success_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        type = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_pay_type);
        String redbag = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_pay_people);
        String money = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_pay_money);
        if (type.equals("1")) {
            setTitle(getString(R.string.pay_success));
            payType2.setVisibility(View.VISIBLE);
            payType.setVisibility(View.GONE);
            tvPaysuccess.setText(getString(R.string.pay_success_wait));
            tvMoney2.setText(money);
        } else if (type.equals("100")){
            setTitle("我要提现");
            payType2.setVisibility(View.VISIBLE);
            payType.setVisibility(View.GONE);
            tvPaysuccess.setText("提现申请已提交成功，24小时内到账喔~");
            tvMoney2.setText(money);
        } else if(type.equals("2")){
            setTitle(getString(R.string.pay_success));
            payType2.setVisibility(View.GONE);
            payType.setVisibility(View.GONE);
            tvPaysuccess.setText("开通会员");
            tvMoney2.setText("");
            returnList.setText("返回");
            //获取数据更新
            String id = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
            getUserInfo(id);
        }else if(type.equals("3")){
            setTitle("结果");
            payType2.setVisibility(View.GONE);
            payType.setVisibility(View.GONE);
            tvPaysuccess.setText("领取成功");
            tvMoney2.setText("");
            returnList.setText("返回");
        }else{
            setTitle(getString(R.string.reward_success));
            payType2.setVisibility(View.GONE);
            payType.setVisibility(View.VISIBLE);
            tvPaysuccess.setText(getString(R.string.reward_success));
            tvRedbagNum.setText(String.format(getString(R.string.tv_red_bag_num), redbag));
            tvMoney.setText(String.format(getString(R.string.reward_money), money));
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (type.equals("100")){
            finish();
        }else if(type.equals("2")) {
            finish();
            startActivity(MyVipActivity.class);
        }else if(type.equals("3")){
            finish();
            startActivity(PointActivity.class);
        }else{
            MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
            startActivity(MyPutActivity.class);
        }
    }

    @OnClick(R.id.return_list)
    public void onViewClicked() {
        exit();
    }

    private void getUserInfo(String id) {
        Map map = new HashMap();
        map.put("mode", "profile");
        YSBSdk.getService(OAuthService.class).getuserinfo(id, map, new YRequestCallback<UserBean.UserDetailBean>() {
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
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_service_charge_percent, var1.getService_charge_percent());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_user_vip_state, var1.getMember_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_user_vip_end, var1.getExpiration_at());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KET_free_vip_state, var1.getTry_member_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_VIP_first_open, var1.getFirst_open_member());
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

}
