package com.shijinsz.shijin.ui.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.UserSiteBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.StoreCarOrderAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/** 确认订单 */
public class ShopOrderActivity extends BaseActivity {

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_total_price)
    TextView mPrice;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.user_site)
    TextView userSite;
    @BindView(R.id.top_view)
    View topView;

    private IWXAPI api;

    private String userId;
    private StoreCarOrderAdapter adpater;
    private List<ShoppingShopBean> list = new ArrayList<>();

    double total_price = 0;

    //判断会员状态
    private String isVIP;

    //用户收货地址
    public UserSiteBean siteData;

    @Override
    public int bindLayout() {
        return R.layout.shop_pay_order;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("确认订单");
        showTitleBackButton();
        topView.setBackgroundColor(Color.WHITE);

        api= WXAPIFactory.createWXAPI(this,getString(R.string.WEIXIN_APPID),true);
        api.registerApp(getString(R.string.WEIXIN_APPID));
        isVIP = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_store_vip);
        userId = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
        getData();
        adpater = new StoreCarOrderAdapter(R.layout.shop_order_item, list);
        adpater.setIsVIP(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_store_vip));
        recyclerView.setAdapter(adpater);



    }

    private void getData() {

        Bundle bundle = getIntent().getBundleExtra("data");
        String bean = bundle.getString("listBean");
        Type userListType = new TypeToken<ArrayList<ShoppingShopBean>>(){}.getType();
        list =  new Gson().fromJson(bean,userListType);
        mStateView.showContent();
        total_price = 0;

        for (int i = 0;i < list.size();i++){
            //店铺
            for (int k = 0;k < list.get(i).getGoodsList().size();k++){
                StoreGoodsBean goods = list.get(i).getGoodsList().get(k);

                if(isVIP.equals("true") && goods.getDiscount() > 0){
                    total_price += goods.getDiscount() * goods.getNum();
                }else{
                    total_price += goods.getPrice() * goods.getNum();
                }


            }
            //店铺合计
            total_price += list.get(i).getPostage_price();
        }

        mPrice.setText("" + total_price);


    }


    @OnClick({R.id.site_layout,R.id.tv_go_to_pay})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.site_layout:
                //收货地址
                Intent intent = new Intent(mContext,UserLocationActivity.class);
                intent.putExtra("select","true");
                startActivityForResult(intent,101);
                break;
            case R.id.tv_go_to_pay:
                putCartData();
                break;
        }

    }

    //提交订单
    private void putCartData() {

        if(siteData == null){
            Intent intent = new Intent(mContext,UserLocationActivity.class);
            intent.putExtra("select","true");
            startActivityForResult(intent,101);
            return;
        }

        Map map = new HashMap();
        map.put("type","GOODS");
        map.put("attach","");
        map.put("payType","WX");
        map.put("name",siteData.getName());
        map.put("phone",siteData.getPhone());
        map.put("address",siteData.getAddress());
        List<Map> listmap = new ArrayList();
        for (int i = 0;i < list.size();i++){
            for (int k = 0;k < list.get(i).getGoodsList().size();k++){
                Map goodsMap = new HashMap();
                StoreGoodsBean goods = list.get(i).getGoodsList().get(k);
                goodsMap.put("goods_id",goods.getGoods_id());
                goodsMap.put("sku_id",goods.getSku_id());
                goodsMap.put("shopping_cart_id",goods.get_id());
                goodsMap.put("num",goods.getNum());
                listmap.add(goodsMap);
            }
        }
        map.put("goods",listmap);
        YSBSdk.getService(OAuthService.class).putCarGoods(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                Toast.makeText(mContext,"提交订单成功",Toast.LENGTH_LONG).show();
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_type,"5");
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
            siteData = new Gson().fromJson(data.getStringExtra("site"),UserSiteBean.class);
            //设置收货地址显示
            setSiteShow();
        }
    }

    public void setSiteShow(){
        userName.setText(siteData.getName());
        userPhone.setText(siteData.getPhone());
        userSite.setText(siteData.getAddress());
    }
}
