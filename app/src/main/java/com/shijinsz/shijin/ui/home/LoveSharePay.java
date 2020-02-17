package com.shijinsz.shijin.ui.home;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.CommodityCardBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.UserSiteBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.UserLocationActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/** 爱分享 订单支付 */
public class LoveSharePay extends BaseActivity {

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

    //用户收货地址
    public UserSiteBean siteData;

    private String skuId;

    private IWXAPI api;
    private StoreGoodsBean goodsData = new StoreGoodsBean();


    @Override
    public int bindLayout() {
        return R.layout.love_share_pay;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("提交订单");
        showTitleBackButton();

        //接收参数
        Bundle data = getIntent().getBundleExtra("data");
        goodsData.set_id(data.getString("goodsId"));
        goodsData.setName(data.getString("goodsName"));
        goodsData.setGoodsImg(data.getString("imgurl"));
        goodsData.setPrice(Double.valueOf(data.getString("price")));
        goodsData.setPostage(data.getString("postage"));
        goodsData.setNum(Integer.valueOf(data.getString("num")));
        skuId = data.getString("skuId");

        mName.setText(getString(R.string.take_person," "));
        mSite.setText(getString(R.string.take_site," "));


        api= WXAPIFactory.createWXAPI(this,getString(R.string.WEIXIN_APPID),true);
        api.registerApp(getString(R.string.WEIXIN_APPID));

        //设置商品信息
        setCommodityData();

    }

    //设置商品信息
    public void setCommodityData(){
        mCard_name.setText(goodsData.getName());
        mNum.setText("数量 "+goodsData.getNum());
        mPrice.setText("¥ "+Double.valueOf(goodsData.getPrice()));
        mNum2.setText("x"+goodsData.getNum());
        mPos_price.setText("¥ "+Double.valueOf(goodsData.getPostage()));

        mTotal.setText("¥ "+ goodsData.getPrice() * goodsData.getNum());
        if(goodsData.getHintText() != null){
            mDescribe.setText("描述：" + goodsData.getHintText());
        }

        Glide.with(this).load(goodsData.getGoodsImg()).into(mImg);
    }

    @OnClick({R.id.bt_submit,R.id.user_layout})
    public void onClickBut(View view){
        switch (view.getId()){
            case R.id.bt_submit:
                submitData();
                break;
            case R.id.user_layout:
                //收货地址
                Intent intent = new Intent(mContext, UserLocationActivity.class);
                intent.putExtra("select","true");
                startActivityForResult(intent,101);
                break;
        }

    }

    public void submitData(){

        if(siteData == null){
            Intent intent = new Intent(mContext,UserLocationActivity.class);
            intent.putExtra("select","true");
            startActivityForResult(intent,101);
            return;
        }

        Map map = new HashMap();
        map.put("type","I-SHARE");
        map.put("attach","");
        map.put("payType","WX");
        map.put("name",siteData.getName());
        map.put("phone",siteData.getPhone());
        map.put("address",siteData.getAddress());
        List<Map> listmap = new ArrayList();
        Map goodsMap = new HashMap();
        goodsMap.put("goods_id",goodsData.get_id());
        goodsMap.put("sku_id",skuId);
        goodsMap.put("num",goodsData.getNum());
        listmap.add(goodsMap);
        map.put("goods",listmap);

        YSBSdk.getService(OAuthService.class).putCarGoods(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                Toast.makeText(mContext,"提交订单成功",Toast.LENGTH_LONG).show();
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_type,"5");
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
                //Toast.makeText(mContext,"message",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable var1) {
                // Toast.makeText(mContext,"提交订单失败二",Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 101){
            //设置地址信息
            siteData = new Gson().fromJson(data.getStringExtra("site"), UserSiteBean.class);
            //设置收货地址显示
            setSiteShow();
        }
    }

    public void setSiteShow(){
        mName.setText(siteData.getName());
        mPhone.setText(siteData.getPhone());
        mSite.setText(siteData.getAddress().replace("-"," "));
    }
}
