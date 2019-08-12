package com.shijinsz.shijin.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: UserSiteMessageActivity
 * Author: m1342
 * Date: 2019/8/2 10:44
 * Description: 商品领取填写信息页面
 */
public class UserSiteMessageActivity extends BaseActivity {

    @BindView(R.id.user_name)
    EditText mName;
    @BindView(R.id.user_phone)
    EditText mPhone;
    @BindView(R.id.user_site)
    EditText mSite;
    @BindView(R.id.user_remark)
    EditText mRemark;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.remind_box)
    CheckBox mCheckbox;

    private String user_id;
    private String user_name;

    private GoodsBean goods = new GoodsBean();
    private DialogUtils dialogUtils;
    private IWXAPI api;

    @Override
    public int bindLayout() { return R.layout.my_take_site; }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("收货地址");
        showTitleBackButton();

        mButton.setClickable(false);

        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mButton.setClickable(true);
                    mButton.setBackgroundColor(getResources().getColor(R.color.color_e01));
                }else{
                    mButton.setClickable(false);
                    mButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
        });

        api= WXAPIFactory.createWXAPI(this,getString(R.string.WEIXIN_APPID),true);
        api.registerApp(getString(R.string.WEIXIN_APPID));
        getUserData();
    }

    //获取数据
    private void getUserData() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils(mActivity);
        }

        Bundle data = getIntent().getExtras();
        goods.setId(data.getString("goods_id"));

        user_id = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
        user_name = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        //设置本地保存地址信息
        mName.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee));
        mPhone.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_phone));
        mSite.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_site));
        mRemark.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_remark));
    }

    @OnClick(R.id.button)
    public void buttonClick(View view){
        //判断数据是否为空  空就不能提交
        String name = mName.getText().toString();
        String phone = mPhone.getText().toString();
        String site = mSite.getText().toString();
        String remark = mRemark.getText().toString();

        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee,name);
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_phone,phone);
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_site,site);
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_remark,remark);

        if(TextUtil.isPhoneNumber(phone)){
            System.out.println("手机号码正确");
        }else{
            System.out.println("手机号码不正确");
        }

        if(name.trim().equals("") ||phone.trim().equals("") || site.trim().equals("")){
            Toast.makeText(mContext,"内容不能为空",Toast.LENGTH_SHORT).show();
        }else{
            Map map = new HashMap();
            //map.put("mode", "person");
            map.put("user_id", user_id);
            map.put("goods_id", goods.getId());
            map.put("username", user_name);
            map.put("consignee", name);
            map.put("contact_way", phone);
            map.put("shipping_address", site);
            map.put("remark", remark);
            YSBSdk.getService(OAuthService.class).set_goods_data(map,new YRequestCallback<GoodsBean>() {
                @Override
                public void onSuccess(GoodsBean var1) {

                    //提交成功调取支付
                    getPayData();

                }

                @Override
                public void onFailed(String var1, String message) {
                    ErrorUtils.error(mContext, var1, message);
                }

                @Override
                public void onException(Throwable var1) {
                    Toast.makeText(mContext, "提交失败", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //获取支付数据
    private void getPayData() {

        Map map = new HashMap();
        map.put("mode","wxpay");
        map.put("channel","app_goods");
        Map attachBean =new HashMap();
        Map rewardPlan=new HashMap();
        Gson gson=new Gson();
        //rewardPlan.put("member_type","1");
        rewardPlan.put("user_id",user_id);
        attachBean.put("reward_plan",rewardPlan);
        attachBean.put("goods_id",goods.getId());
        map.put("attach",gson.toJson(attachBean)+"");


        System.out.println("支付....");
        System.out.println("user_id: " + user_id);



        YSBSdk.getService(OAuthService.class).preorder(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_type,"3");
                //ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_money,total_number+"");
                PayReq request = new PayReq();
                request.appId = var1.getAppid();
                request.partnerId = var1.getPartnerid();
                request.prepayId= var1.getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr= var1.getNoncestr();
                request.timeStamp= var1.getTimestamp();
                request.sign= var1.getSign();
                api.sendReq(request);
                finish();
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

}
