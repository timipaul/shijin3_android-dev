package com.shijinsz.shijin.ui.ishare;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.common.SysOSUtil;
import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.home.LoveSharePay;
import com.shijinsz.shijin.ui.store.adapter.SpecificationAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoginUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/** 爱分享支付 */
public class IshareOrderPay extends BaseActivity {

    @BindView(R.id.goods_img)
    ImageView mImg;
    @BindView(R.id.goods_price)
    TextView mPrice;
    @BindView(R.id.sell_num)
    TextView mSellNum;
    @BindView(R.id.goods_type)
    TextView mType;
    @BindView(R.id.recycler)
    PowerfulRecyclerView mRecycler;
    @BindView(R.id.et_num)
    TextView mGoodsNum;
    SpecificationAdapter adapter;
    @BindView(R.id.goods_pay)
    TextView mPay;
    @BindView(R.id.goods_sv)
    TextView mSV;

    private String goodsId;
    StoreGoodsBean goodsData;

    List<StoreGoodsBean.Specification> list = new ArrayList<>();
    List<StoreGoodsBean.Sku> listSku = new ArrayList<>();
    LinkedHashMap<String, String> mapData = new LinkedHashMap<>();
    Map<String,String> priceMap = new HashMap<>();
    Map<String,String> skuIdMap = new HashMap<>();

    String skuId = null;

    @Override
    public int bindLayout() {
        return R.layout.i_share_order_pay;
    }

    @Override
    public void initView(View view) {


        //设置视图背景透明
        translucentActivity(IshareOrderPay.this);

        adapter = new SpecificationAdapter(R.layout.goods_size_measure, list);
        mRecycler.setAdapter(adapter);
        adapter.setOnListen(new SpecificationAdapter.onListen() {
            @Override
            public void callback(String key, String val,int index) {
                String textVal = "";
                mapData.put(key, val);
                for (Map.Entry<String, String> entry : mapData.entrySet()) {
                    if (!textVal.equals("")) {
                        textVal += "-";
                    }
                    textVal += entry.getValue().length() > 0 ? entry.getValue() : entry.getKey();
                    if (entry.getValue().equals("")) {
                        mPay.setBackgroundColor(getResources().getColor(R.color.color_6d));
                        mPay.setEnabled(false);
                    } else {
                        mPay.setBackgroundColor(getResources().getColor(R.color.color_fdb));
                        mPay.setEnabled(true);
                    }
                }

                String textPrice = "";
                if (!textVal.isEmpty()) {
                    textPrice = priceMap.get(textVal);
                    goodsData.setPrice(Double.valueOf(textPrice));
                    skuId = skuIdMap.get(textVal);
                }
                mType.setText("选择 " + textVal);
                mPrice.setText("¥" + textPrice);
                System.out.println(index + "设置sv数据 " + goodsData.getSku());
                mSV.setText("(" + goodsData.getSku().get(index).getSv()+" sv)");
            }
        });

        goodsId = getIntent().getStringExtra("goodsId");
        getGoodsData(goodsId);


    }

    //获取数据
    private void getGoodsData(String goodsId) {
        YSBSdk.getService(OAuthService.class).getGoodsDetails(goodsId, new YRequestCallback<ShoppingShopBean>() {
            @Override
            public void onSuccess(ShoppingShopBean var1) {
                goodsData = var1.getGoods();
                listSku.addAll(var1.getGoods().getSku());

                for (int i = 0;i < var1.getGoods().getSku().size();i++){
                    StoreGoodsBean.Sku sku = var1.getGoods().getSku().get(i);
                    //普通价
                    priceMap.put(sku.getAttriValStr(),String.valueOf(sku.getPrice()));
                    skuIdMap.put(sku.getAttriValStr(),sku.get_id());
                    mPrice.setText("¥" + goodsData.getPrice());

                }
                Glide.with(mContext).load(goodsData.getCoverImg()[0]).into(mImg);
                mSellNum.setText("已售" + goodsData.getSalesVolume() + "件");
                String key = "";
                for (int i = 0; i < goodsData.getAttribute().length; i++) {
                    key += goodsData.getAttribute()[i].getKey() + " ";
                    list.add(goodsData.getAttribute()[i]);
                    mapData.put(goodsData.getAttribute()[i].getKey(), "");
                }
                mType.setText("选择 " + key);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    @OnClick({R.id.tv_hide, R.id.img_cancel, R.id.bt_reduce, R.id.bt_add,R.id.goods_pay})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_hide:
            case R.id.img_cancel:
                finish();
                break;
            case R.id.bt_reduce:
                setGoodsNum(-1);
                break;
            case R.id.bt_add:
                setGoodsNum(1);
                break;
            case R.id.goods_pay:
                //购买
                if (!LoginUtil.isLogin(mContext)) {
                    return;
                }
                payGoods();
                break;
        }
    }

    public void payGoods(){

        if(skuId == null){
            ToastUtil.showToast("请选择商品规格");
            return;
        }

        //提交支付
        Intent intent = new Intent(mContext, LoveSharePay.class);
        Bundle data = new Bundle();
        data.putString("goodsId",goodsData.get_id());
        data.putString("goodsName",goodsData.getName());
        data.putString("price",goodsData.getPrice()+"");
        data.putString("postage",goodsData.getPostage() == null ? "0" : goodsData.getPostage());
        data.putString("num",mGoodsNum.getText().toString());
        data.putString("imgurl",goodsData.getCoverImg()[0]);
        data.putString("skuId",skuId);
        intent.putExtra("data",data);
        startActivity(intent);

    }

    public void setGoodsNum(int val) {
        int num = Integer.valueOf(mGoodsNum.getText().toString());
        num += val;
        if (num < 0) {
            num = 0;
        }
        mGoodsNum.setText(String.valueOf(num));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
        super.finish();
    }

    //设置视图透明
    private void translucentActivity(Activity activity) {

        try {
            activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            activity.getWindow().getDecorView().setBackground(null);
            Method activityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            activityOptions.setAccessible(true);
            Object options = activityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> aClass = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    aClass = clazz;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent", aClass, ActivityOptions.class);
            method.setAccessible(true);
            method.invoke(activity, null, options);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
