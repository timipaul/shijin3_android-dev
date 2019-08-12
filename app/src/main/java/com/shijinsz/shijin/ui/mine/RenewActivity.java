package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.CashierInputFilter;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PayChangeBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.RewardPlan;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/15.
 */

public class RenewActivity extends BaseActivity {
    @BindView(R.id.et_red_num)
    EditText etRedNum;
    @BindView(R.id.ed_money)
    EditText edMoney;
    @BindView(R.id.bt_renew)
    Button btRenew;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.tv_unput)
    TextView tvUnput;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.shui)
    TextView shui;
    private String id;
    private boolean isClick = false;
    private int shuinum=0;
    @Override
    public int bindLayout() {
        return R.layout.renew_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.renew));
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_service_charge_percent).equals("")) {
            shui.setVisibility(View.GONE);
        } else {
            if (Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_service_charge_percent)) > 0) {
                shui.setVisibility(View.VISIBLE);
                shuinum = Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_service_charge_percent));
                shui.setText(String.format(getString(R.string.shui), 0 + "", shuinum + "%", 0 + ""));
            } else {
                shui.setVisibility(View.GONE);
            }
        }
        id = getIntent().getStringExtra("id");
        api = WXAPIFactory.createWXAPI(this, getString(R.string.WEIXIN_APPID), true);
        api.registerApp(getString(R.string.WEIXIN_APPID));
        InputFilter[] filters = {new CashierInputFilter()};
        edMoney.setFilters(filters);
        edMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edMoney.getText().toString().isEmpty()){
                    shui.setText(String.format(getString(R.string.shui),0+"",shuinum+"%",0+""));
                }else {
                    float num=Float.valueOf(edMoney.getText().toString())*(100-shuinum)/100;
                    DecimalFormat fnum  =   new  DecimalFormat("##0.00");
                    String   dd=fnum.format(num);
                    shui.setText(String.format(getString(R.string.shui),edMoney.getText().toString(),shuinum+"%",dd+""));
                }
                if (isClick) {
                    topView.setVisibility(View.VISIBLE);
                    tvUnput.setVisibility(View.GONE);
                    if (isEnoughtM()) {
                        btRenew.setEnabled(true);
                    } else {
                        btRenew.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etRedNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isClick) {
                    topView.setVisibility(View.VISIBLE);
                    tvUnput.setVisibility(View.GONE);
                    if (isEnoughtM()) {
                        btRenew.setEnabled(true);
                    } else {
                        btRenew.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.bt_renew)
    public void onViewClicked() {
        if (etRedNum.getText().toString().isEmpty()) {
            ToastUtil.showToast(getString(R.string.plz_setnum));
            return;
        }
        if (edMoney.getText().toString().isEmpty()) {
            ToastUtil.showToast(getString(R.string.plz_setmoney));
            return;
        }
        int num = Integer.parseInt(etRedNum.getText().toString());
        float money = Float.valueOf(edMoney.getText().toString());
        if (money < 1) {
            tvUnput.setText(getString(R.string.more_than_1yuan));
            tvUnput.setVisibility(View.VISIBLE);
            topView.setVisibility(View.GONE);
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            isClick = true;
            return;
        }
        if (money / num < 0.1) {
            tvUnput.setText(getString(R.string.more_than_01));
            tvUnput.setVisibility(View.VISIBLE);
            topView.setVisibility(View.GONE);
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            isClick = true;
            return;
        }
        showEarlyDialog();
    }

    private IWXAPI api;
    TextView money;
    TextView balance;
    TextView balance2;
    TextView alipay;
    TextView alipay2;
    TextView alipay3;
    TextView wechat;
    TextView wechat2;
    TextView wechat3;
    TextView commit;
    LinearLayout ln_balance;
    LinearLayout ln_wechat;
    LinearLayout ln_alipay;
    ImageView rb_balance;
    ImageView rb_alipay;
    ImageView rb_wechat;
    ImageView img_balance;
    ImageView img_alipay;
    ImageView img_wechat;
    private int type = 0;

    public void showEarlyDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout mailBoxLay = (RelativeLayout) inflater.inflate(
                R.layout.pay_dialog, null);
        money = mailBoxLay.findViewById(R.id.tv_money);
        balance = mailBoxLay.findViewById(R.id.tv_balance);
        balance2 = mailBoxLay.findViewById(R.id.tv_balance2);
        alipay = mailBoxLay.findViewById(R.id.tv_alipay);
        alipay2 = mailBoxLay.findViewById(R.id.tv_alipay2);
        alipay3 = mailBoxLay.findViewById(R.id.tv_alipay3);
        wechat = mailBoxLay.findViewById(R.id.tv_wechat);
        wechat2 = mailBoxLay.findViewById(R.id.tv_wechat2);
        wechat3 = mailBoxLay.findViewById(R.id.tv_wechat3);
        commit = mailBoxLay.findViewById(R.id.commit);
        ln_alipay = mailBoxLay.findViewById(R.id.ln_alipay);
        ln_balance = mailBoxLay.findViewById(R.id.ln_balance);
        ln_wechat = mailBoxLay.findViewById(R.id.ln_wechat);
        rb_alipay = mailBoxLay.findViewById(R.id.rb_alipay);
        rb_wechat = mailBoxLay.findViewById(R.id.rb_wechat);
        rb_balance = mailBoxLay.findViewById(R.id.rb_balance);
        img_alipay = mailBoxLay.findViewById(R.id.img_alipay);
        img_wechat = mailBoxLay.findViewById(R.id.img_wechat);
        img_balance = mailBoxLay.findViewById(R.id.img_balance);
        balance2.setText(String.format(getString(R.string.balance), ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change)));
        wechat2.setText(String.format(getString(R.string.limit), 10000 + ""));
        alipay2.setText(String.format(getString(R.string.limit), 3000 + ""));
        money.setText(edMoney.getText().toString());
        ln_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 0;
                rb_balance.setImageResource(R.mipmap.radio_button_on);
                img_balance.setImageResource(R.mipmap.icon_balance_2);
                balance.setTextColor(getResources().getColor(R.color.text_33));
                balance2.setTextColor(getResources().getColor(R.color.text_999999));
                rb_alipay.setImageResource(R.mipmap.radio_button_off);
                img_alipay.setImageResource(R.mipmap.icon_alipay_2);
                alipay.setTextColor(getResources().getColor(R.color.text_hint));
                alipay2.setTextColor(getResources().getColor(R.color.text_hint));
                alipay3.setTextColor(getResources().getColor(R.color.text_hint));
                rb_wechat.setImageResource(R.mipmap.radio_button_off);
                img_wechat.setImageResource(R.mipmap.icon_wechat_pay);
                wechat.setTextColor(getResources().getColor(R.color.text_hint));
                wechat2.setTextColor(getResources().getColor(R.color.text_hint));
                wechat3.setTextColor(getResources().getColor(R.color.text_hint));
            }
        });
        ln_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                rb_balance.setImageResource(R.mipmap.radio_button_off);
                img_balance.setImageResource(R.mipmap.icon_balance);
                balance.setTextColor(getResources().getColor(R.color.text_hint));
                balance2.setTextColor(getResources().getColor(R.color.text_hint));
                rb_alipay.setImageResource(R.mipmap.radio_button_off);
                img_alipay.setImageResource(R.mipmap.icon_alipay_2);
                alipay.setTextColor(getResources().getColor(R.color.text_hint));
                alipay2.setTextColor(getResources().getColor(R.color.text_hint));
                alipay3.setTextColor(getResources().getColor(R.color.text_hint));
                rb_wechat.setImageResource(R.mipmap.radio_button_on);
                img_wechat.setImageResource(R.mipmap.icon_wechat_2);
                wechat.setTextColor(getResources().getColor(R.color.text_33));
                wechat2.setTextColor(getResources().getColor(R.color.text_33));
                wechat3.setTextColor(getResources().getColor(R.color.text_999999));
            }
        });
        ln_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                rb_balance.setImageResource(R.mipmap.radio_button_off);
                img_balance.setImageResource(R.mipmap.icon_balance);
                balance.setTextColor(getResources().getColor(R.color.text_hint));
                balance2.setTextColor(getResources().getColor(R.color.text_hint));
                rb_alipay.setImageResource(R.mipmap.radio_button_on);
                img_alipay.setImageResource(R.mipmap.icon_alipay);
                alipay.setTextColor(getResources().getColor(R.color.text_33));
                alipay2.setTextColor(getResources().getColor(R.color.text_33));
                alipay3.setTextColor(getResources().getColor(R.color.text_999999));
                rb_wechat.setImageResource(R.mipmap.radio_button_off);
                img_wechat.setImageResource(R.mipmap.icon_wechat_pay);
                wechat.setTextColor(getResources().getColor(R.color.text_hint));
                wechat2.setTextColor(getResources().getColor(R.color.text_hint));
                wechat3.setTextColor(getResources().getColor(R.color.text_hint));

            }
        });
        final NoticeDialog mailBoxDialog = new NoticeDialog(this);
        mailBoxDialog.showDialog(mailBoxLay, 0, 0, 1);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    pay();
                } else if (type == 0) {
                    payusechange();
                }
//                Intent intent= new Intent(mContext,PaySuccessActivity.class);
//                intent.putExtra("redbag",etRedNum.getText().toString());
//                intent.putExtra("money",edMoney.getText().toString());
//                startActivity(intent);
//                finish();
                mailBoxDialog.dismiss();
            }
        });

    }

    private void pay() {
        Map map = new HashMap();
        map.put("mode", "wxpay");
        map.put("channel", "app");
        map.put("change", edMoney.getText().toString());
        Map attachBean = new HashMap();
        attachBean.put("mode", "ad");
        attachBean.put("ad_id", id);
        Map rewardPlan = new HashMap();
        Gson gson = new Gson();
        rewardPlan.put("people_number", etRedNum.getText().toString());
        rewardPlan.put("total_number", edMoney.getText().toString());
        rewardPlan.put("reward_mode", "change");
        attachBean.put("reward_plan", rewardPlan);
        map.put("attach", gson.toJson(attachBean) + "");
//        Map map = new HashMap();
//        map.put("mode","wxpay");
//        map.put("channel","app");
//        map.put("change","10");
//        map.put("attach","ad_34");
        YSBSdk.getService(OAuthService.class).preorder(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_type, "2");
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_money, edMoney.getText().toString() + "");
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_people, etRedNum.getText().toString() + "");
                PayReq request = new PayReq();
                request.appId = var1.getAppid();
                request.partnerId = var1.getPartnerid();
                request.prepayId = var1.getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = var1.getNoncestr();
                request.timeStamp = var1.getTimestamp();
                request.sign = var1.getSign();
                api.sendReq(request);
//                PayReq request = new PayReq();
//                request.appId = var1.getAppid();
//                request.partnerId = var1.getPartnerid();
//                request.prepayId= var1.getPrepayid();
//                request.packageValue = "Sign=WXPay";
//                request.nonceStr= var1.getNoncestr();
//                request.timeStamp= var1.getTimestamp();
//                request.sign= var1.getSign();
//                api.sendReq(request);
//                Log.d("jim","check args "+request.checkArgs());
//                Log.d("jim","send return :"+api.sendReq(request));
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    private void payusechange() {
        PayChangeBean bean = new PayChangeBean();
        bean.setChange(edMoney.getText().toString());
        bean.setMode("person");
        RewardPlan rewardPlan = new RewardPlan();
        rewardPlan.setPeople_number(etRedNum.getText().toString());
        rewardPlan.setReward_mode("change");
        rewardPlan.setTotal_number(edMoney.getText().toString());
        bean.setReward_plan(rewardPlan);
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
        YSBSdk.getService(OAuthService.class).recharge(id, bean, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_type, "2");
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_money, edMoney.getText().toString() + "");
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_people, etRedNum.getText().toString() + "");
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_LONG).show();
                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                Intent intent = new Intent(mContext, PaySuccessActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isEnoughtM() {
        if (edMoney.getText().toString().isEmpty() || etRedNum.getText().toString().isEmpty()) {
            return false;
        }
        int num = Integer.parseInt(etRedNum.getText().toString());
        float money = Float.valueOf(edMoney.getText().toString());
        if (money < 1) {
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            return false;
        }
        if (money / num < 0.1) {
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            return false;
        }
        tv1.setTextColor(getResources().getColor(R.color.text_33));
        tv2.setTextColor(getResources().getColor(R.color.text_33));
        tv3.setTextColor(getResources().getColor(R.color.text_33));
        tv4.setTextColor(getResources().getColor(R.color.text_33));
        edMoney.setTextColor(getResources().getColor(R.color.text_33));
        etRedNum.setTextColor(getResources().getColor(R.color.text_33));
        return true;
    }
}
