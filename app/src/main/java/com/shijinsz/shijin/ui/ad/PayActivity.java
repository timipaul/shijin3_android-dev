package com.shijinsz.shijin.ui.ad;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PayChangeBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.MyPutActivity;
import com.shijinsz.shijin.ui.mine.PaySuccessActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/20.
 */

public class PayActivity extends BaseActivity {
    @BindView(R.id.rb_balance)
    ImageView rbBalance;
    @BindView(R.id.img_balance)
    ImageView imgBalance;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_balance2)
    TextView tvBalance2;
    @BindView(R.id.ln_balance)
    LinearLayout lnBalance;
    @BindView(R.id.rb_alipay)
    ImageView rbAlipay;
    @BindView(R.id.img_alipay)
    ImageView imgAlipay;
    @BindView(R.id.tv_alipay)
    TextView tvAlipay;
    @BindView(R.id.tv_alipay2)
    TextView tvAlipay2;
    @BindView(R.id.tv_alipay3)
    TextView tvAlipay3;
    @BindView(R.id.ln_alipay)
    LinearLayout lnAlipay;
    @BindView(R.id.rb_wechat)
    ImageView rbWechat;
    @BindView(R.id.img_wechat)
    ImageView imgWechat;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_wechat2)
    TextView tvWechat2;
    @BindView(R.id.tv_wechat3)
    TextView tvWechat3;
    @BindView(R.id.ln_wechat)
    LinearLayout lnWechat;
    @BindView(R.id.bt_pay_now)
    Button btPayNow;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    private IWXAPI api;
    private float money=0;
    private String id,total_number,people_number;
    @Override
    public int bindLayout() {
        return R.layout.pay_activity;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                startActivity(MyPutActivity.class);
            }
        });
        setTitle(getString(R.string.pay));
        id=getIntent().getStringExtra("id");
        total_number=getIntent().getStringExtra("money");
        people_number=getIntent().getStringExtra("num");
        money=Float.valueOf(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
        api= WXAPIFactory.createWXAPI(this,getString(R.string.WEIXIN_APPID),true);
        api.registerApp(getString(R.string.WEIXIN_APPID));
        tvMoney.setText(getIntent().getStringExtra("money"));
        tvBalance2.setText(String.format(getString(R.string.balance), money+""));
        tvAlipay2.setText(String.format(getString(R.string.limit),"3000"));
        tvWechat2.setText(String.format(getString(R.string.limit),"10000"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
            startActivity(MyPutActivity.class);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String payType="balance";
    @OnClick({R.id.ln_balance, R.id.ln_alipay, R.id.ln_wechat, R.id.bt_pay_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_balance:
                payType="balance";
                rbBalance.setImageResource(R.mipmap.radio_button_on);
                imgBalance.setImageResource(R.mipmap.icon_balance_2);
                tvBalance.setTextColor(getResources().getColor(R.color.text_33));
                tvBalance2.setTextColor(getResources().getColor(R.color.text_999999));
                rbAlipay.setImageResource(R.mipmap.radio_button_off);
                imgAlipay.setImageResource(R.mipmap.icon_alipay_2);
                tvAlipay.setTextColor(getResources().getColor(R.color.text_hint));
                tvAlipay2.setTextColor(getResources().getColor(R.color.text_hint));
                tvAlipay3.setTextColor(getResources().getColor(R.color.text_hint));
                rbWechat.setImageResource(R.mipmap.radio_button_off);
                imgWechat.setImageResource(R.mipmap.icon_wechat_pay);
                tvWechat.setTextColor(getResources().getColor(R.color.text_hint));
                tvWechat2.setTextColor(getResources().getColor(R.color.text_hint));
                tvWechat3.setTextColor(getResources().getColor(R.color.text_hint));
                break;
            case R.id.ln_alipay:
                payType="alipay";
                rbBalance.setImageResource(R.mipmap.radio_button_off);
                imgBalance.setImageResource(R.mipmap.icon_balance);
                tvBalance.setTextColor(getResources().getColor(R.color.text_hint));
                tvBalance2.setTextColor(getResources().getColor(R.color.text_hint));
                rbAlipay.setImageResource(R.mipmap.radio_button_on);
                imgAlipay.setImageResource(R.mipmap.icon_alipay);
                tvAlipay.setTextColor(getResources().getColor(R.color.text_33));
                tvAlipay2.setTextColor(getResources().getColor(R.color.text_999999));
                tvAlipay3.setTextColor(getResources().getColor(R.color.text_999999));
                rbWechat.setImageResource(R.mipmap.radio_button_off);
                imgWechat.setImageResource(R.mipmap.icon_wechat_pay);
                tvWechat.setTextColor(getResources().getColor(R.color.text_hint));
                tvWechat2.setTextColor(getResources().getColor(R.color.text_hint));
                tvWechat3.setTextColor(getResources().getColor(R.color.text_hint));
                break;
            case R.id.ln_wechat:
                payType="wechat";
                rbBalance.setImageResource(R.mipmap.radio_button_off);
                imgBalance.setImageResource(R.mipmap.icon_balance);
                tvBalance.setTextColor(getResources().getColor(R.color.text_hint));
                tvBalance2.setTextColor(getResources().getColor(R.color.text_hint));
                rbAlipay.setImageResource(R.mipmap.radio_button_off);
                imgAlipay.setImageResource(R.mipmap.icon_alipay_2);
                tvAlipay.setTextColor(getResources().getColor(R.color.text_hint));
                tvAlipay2.setTextColor(getResources().getColor(R.color.text_hint));
                tvAlipay3.setTextColor(getResources().getColor(R.color.text_hint));
                rbWechat.setImageResource(R.mipmap.radio_button_on);
                imgWechat.setImageResource(R.mipmap.icon_wechat_2);
                tvWechat.setTextColor(getResources().getColor(R.color.text_33));
                tvWechat2.setTextColor(getResources().getColor(R.color.text_999999));
                tvWechat3.setTextColor(getResources().getColor(R.color.text_999999));

                break;
            case R.id.bt_pay_now:
                if (payType.equals("balance")){
                    if(money<Float.valueOf(total_number)){
                        ToastUtil.showToast("余额不足");
                        return;
                    }
                    payusechange();
                }else {
                    pay();
                }
//                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
//                startActivity(PaySuccessActivity.class);
                break;
        }
    }
    private void pay(){
        Map map = new HashMap();
        map.put("mode","wxpay");
        map.put("channel","app");
        map.put("change",total_number);
        Map attachBean =new HashMap();
        attachBean.put("mode","ad");
        attachBean.put("ad_id",id);
        Map rewardPlan=new HashMap();
        Gson gson=new Gson();
        rewardPlan.put("people_number",people_number);
        rewardPlan.put("total_number",total_number);
        rewardPlan.put("reward_mode","change");
        attachBean.put("reward_plan",rewardPlan);
        map.put("attach",gson.toJson(attachBean)+"");
//        Map map = new HashMap();
//        Map attach=new HashMap();
//        Map reward=new HashMap();
//        attach.put("mode","ad");
//        attach.put("ad_id",id);
//        attach.put("username",ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME));
//        reward.put("total_number",total_number);
//        reward.put("people_number",people_number);
//        reward.put("reward_mode","change");
//        attach.put("reward_plan",reward);
//        map.put("mode","wxpay");
//        map.put("channel","app");
//        map.put("change","0.01");
//        map.put("attach",attach);
        YSBSdk.getService(OAuthService.class).preorder(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_type,"1");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_money,total_number+"");
                PayReq request = new PayReq();
                request.appId = var1.getAppid();
                request.partnerId = var1.getPartnerid();
                request.prepayId= var1.getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr= var1.getNoncestr();
                request.timeStamp= var1.getTimestamp();
                request.sign= var1.getSign();
                api.sendReq(request);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }
    private void payusechange(){
        PayChangeBean bean =new PayChangeBean();
        bean.setChange(total_number+"");
        bean.setMode("person");
//        bean.getReward_plan().setPeople_number(people_number);
//        bean.getReward_plan().setReward_mode("change");
//        bean.getReward_plan().setTotal_number(total_number);

//        Map map = new HashMap();
//        Map attach=new HashMap();
//        attach.put("total_number",total_number);
//        attach.put("people_number",people_number);
//        attach.put("reward_mode","change");
//        map.put("mode","person");
//        map.put("change",money);
//        map.put("reward_plan",attach);
        YSBSdk.getService(OAuthService.class).recharge(id,bean, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change,money-Float.valueOf(total_number)+"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_type,"1");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_money,total_number+"");
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_LONG).show();
                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                Intent intent=new Intent(mContext, PaySuccessActivity.class);
                intent.putExtra("type", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_pay_type));
                startActivity(intent);   }

            @Override
            public void onFailed(String var1, String message) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable var1) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_LONG).show();
            }
        });
    }
}
