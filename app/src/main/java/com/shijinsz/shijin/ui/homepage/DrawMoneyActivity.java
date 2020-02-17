package com.shijinsz.shijin.ui.homepage;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.TakeMoneyRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.PaySuccessActivity;
import com.shijinsz.shijin.ui.wallet.PaymentActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.callback.YRequestCallback;

/** 用户提现 */
public class DrawMoneyActivity extends BaseActivity {

    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.ed_money)
    EditText edMoney;
    @BindView(R.id.tv_wechat2)
    TextView tvWechat;
    @BindView(R.id.bt_pay_now)
    Button btPayNow;
    private float num;
    private DialogUtils dialogUtils;

    private String username;

    @Override
    public int bindLayout() {
        return R.layout.draw_money_activity;
    }

    @Override
    public void initView(View view) {

        dialogUtils = new DialogUtils(mContext);
        tvWechat.setText(String.format(getString(R.string.limit), "5000"));

        username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);

        if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change).isEmpty()) {
            float mo = Float.valueOf(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
            num = mo;
        } else {
            num = 0;
        }
    }


    @OnClick({R.id.iv_back,R.id.tv_right,R.id.withdraw_all,R.id.bt_pay_now})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //提现记录
                startActivity(PaymentActivity.class);
                break;
            case R.id.withdraw_all:
                //全部提现
                //edMoney.setText((int)num + "");
                break;
            case R.id.bt_pay_now:
                //提现
                String openid = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_OPENID);
                if(!openid.equals("")){
                    getMoney();
                }else{
                    //未绑定
                    wxLoging();
                }


                break;
        }
    }

    //判定微信
    private void wxLoging() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        wechat.SSOSetting(false);
//                wechat.removeAccount(true);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                ToastUtil.showToast("登录成功");
                platform.getDb().exportData();

                Map<String, Object> map = new HashMap<>();

                map.put("username", username);
                map.put("nickname", platform.getDb().getUserName());
                map.put("avatarUrl", platform.getDb().getUserIcon());
                map.put("openid", platform.getDb().getUserId());
                //保存用户信息
                submitWxData(map);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                ToastUtil.showToast("登录异常 onError");

            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtil.showToast("登录异常 onCancel");

            }
        });
        wechat.authorize();
    }

    //保存个人信息
    private void submitWxData(Map<String, Object> map) {

        YSBSdk.getService(OAuthService.class).putCarGoods(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                Toast.makeText(mContext, "提交订单成功", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                //Toast.makeText(mContext,"message",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable var1) {
                // Toast.makeText(mContext,"提交订单失败二",Toast.LENGTH_LONG).show();

            }
        });
    }

    //提现
    private void getMoney() {
        int with = 0;
        if (edMoney.getText().toString().isEmpty()) {
            with = 0;
        } else {
            with = Integer.valueOf(edMoney.getText().toString());
        }
        if (with < 10) {
            ToastUtil.showToast(getString(R.string.more_than_10));
            return;
        }



        //判断钱是否够
        if (with > num) {
            dialogUtils.showWithdrapDialog(mActivity, 7, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(MainActivity.class);
                    dialogUtils.dismissWithdrapDialog();
                }
            });
            return;
        }

        transfer(with);

    }

    private void transfer(int money) {
        btPayNow.setEnabled(false);
        Map map = new HashMap();
        //map.put("mode","wxpay");
        //map.put("change",edMoney.getText().toString());
        map.put("num",money);

        YSBSdk.getService(OAuthService.class).postCapitalFlow(map, new YRequestCallback<TakeMoneyRecordBean>() {
            @Override
            public void onSuccess(TakeMoneyRecordBean var1) {

                getUserInfo();
                ShareDataManager.getInstance().save(mActivity,SharedPreferencesKey.KEY_pay_type,"100");
                Intent intent=new Intent(mContext, PaySuccessActivity.class);
                intent.putExtra("type","100");
                startActivity(intent);
            }

            @Override
            public void onFailed(String var1, String message) {
                btPayNow.setEnabled(true);
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1){
                try {
                    btPayNow.setEnabled(true);
                    var1.printStackTrace();
                }catch (Exception e){

                }
            }
        });

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
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_five_withdraw_status,var1.getFive_withdraw_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_eight_withdraw_status,var1.getEight_withdraw_status());
                float mo=Float.valueOf(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
                num = mo;
                money.setText(num + "");
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
