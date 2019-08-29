package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.CommodityCardBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
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
 * FileName: DiscountsParticularsActivity
 * Author: Administrator
 * Date: 2019/8/21 0021 下午 16:57
 * Description: 提货卡详情
 */
public class DiscountsParticularsActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView mName; //收货人
    @BindView(R.id.phone)
    TextView mPhone;//电话
    @BindView(R.id.user_site)
    TextView mSite;//地址
    @BindView(R.id.commodity_name)
    TextView mCard_name;//商品名字
    @BindView(R.id.commodity_img)
    ImageView mImg;
    @BindView(R.id.commodity_describe)
    TextView mDescribe;//描述
    @BindView(R.id.price)
    TextView mPrice;//商品价格
    @BindView(R.id.commodity_num)
    TextView mNum;//显示数量
    @BindView(R.id.num)
    TextView mNum2;//统计数量
    @BindView(R.id.postage_price)
    TextView mPos_price;//邮费
    @BindView(R.id.total_num)
    TextView mTotal;//共计
    @BindView(R.id.ed_details)
    EditText mDetails;





    private IWXAPI api;
    private CommodityCardBean mCard;

    @Override
    public int bindLayout() {
        return R.layout.discounts_particulars;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("提货卡礼品");
        showTitleBackButton();

        mCard = new CommodityCardBean();
        //接收参数
        Bundle data = getIntent().getBundleExtra("data");
        mCard.setCardId(data.getString("cardId"));
        System.out.println("接收到的Card");
        mCard.setCategoryName(data.getString("cardName"));
        mCard.setImgUrl(data.getString("imgurl"));
        mCard.setPrice(data.getString("price"));
        mCard.setPostage(data.getString("postage"));
        mCard.setNum(data.getString("cardNumber"));

        api= WXAPIFactory.createWXAPI(this,getString(R.string.WEIXIN_APPID),true);
        api.registerApp(getString(R.string.WEIXIN_APPID));

        //设置收货信息
        setUserSite();
        //设置商品信息
        setCommodityData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            setUserSite();
        }
    }

    //设置设置收货人信息
    public void setUserSite(){
        String key_name = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee);
        String key_phone = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_phone);
        String key_site = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_site);
        mName.setText(getString(R.string.take_person,key_name));
        mPhone.setText(key_phone);
        mSite.setText(getString(R.string.take_site,key_site));
    }

    //设置商品信息
    public void setCommodityData(){
        mCard_name.setText(mCard.getCategoryName());
        mNum.setText("数量 "+mCard.getNum());
        mPrice.setText("￥ "+Double.valueOf(mCard.getPrice()));
        mNum2.setText(mCard.getNum());
        mPos_price.setText("￥ "+Double.valueOf(mCard.getPostage()));
        mTotal.setText("￥ "+Double.valueOf(mCard.getPostage()));
        mDescribe.setText("描述：");
        Glide.with(this).load(mCard.getImgUrl()).into(mImg);
    }

    //判断收货信息是否为空
    public boolean DataIsNull(){
        if(mName.getText().equals("")){
            ToastUtil.showToast("请编辑收货人");
            return false;
        }else if(mPhone.getText().equals("") || mPhone.getText().length() != 11){
            ToastUtil.showToast("手机号码不正确");
            return false;
        }else if(mSite.getText().equals("")){
            ToastUtil.showToast("请编辑收货地址");
            return false;
        }else{
            return true;
        }
    }

    @OnClick({R.id.bt_submit,R.id.user_layout})
    public void onClickBut(View view){
        switch (view.getId()){
            case R.id.bt_submit:
                submitData();
                break;
            case R.id.user_layout:
                //跳转到地址信息编辑
                startActivityForResult(new Intent(this,UserHomeSiteActivity.class),101);
                break;
        }

    }

    public void submitData(){
        if(!DataIsNull()){
            ToastUtil.showToast("请输入收货地址");
            return;
        }

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("cardId", mCard.getCardId());
        map.put("name", mName.getText());
        map.put("address", mSite.getText());
        map.put("phone", mPhone.getText());
        map.put("details",mDetails.getText().toString());
        YSBSdk.getService(OAuthService.class).set_card_data(map, new YRequestCallback<CommodityCardBean>() {
            @Override
            public void onSuccess(CommodityCardBean var1) {
                getPayData(var1.getAttach());
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(DiscountsParticularsActivity.this, var1, message);
                mStateView.showContent();
                System.out.println("没有数据1");
            }

            @Override
            public void onException(Throwable var1) {
                System.out.println("处理报错");
            }
        });
    }

    //获取支付数据
    private void getPayData(String attach) {

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("mode","wxpay");
        map.put("channel","card");
        map.put("change",mCard.getPrice());
        Map<String,Object> attachBean =new HashMap<String, Object>();
        Map rewardPlan=new HashMap();
        attachBean.put("reward_plan",rewardPlan);
        System.out.println("attach----: " + attach);
        map.put("attach",attach);


        System.out.println("支付....");
        //System.out.println("user_id: " + user_id);

        YSBSdk.getService(OAuthService.class).preorder(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                //调取支付
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_type,"4");
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
                System.out.println("zhu");
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }
}
