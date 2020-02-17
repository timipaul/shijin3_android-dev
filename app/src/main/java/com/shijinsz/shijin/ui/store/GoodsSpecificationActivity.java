package com.shijinsz.shijin.ui.store;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.admin.SystemUpdatePolicy;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.common.SysOSUtil;
import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.Utils;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.StroeCommentBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.SpecificationAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoginUtil;

import java.lang.reflect.Method;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/***
 *  商品规格
 */
public class GoodsSpecificationActivity extends BaseActivity {

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
    @BindView(R.id.add_shop_car)
    TextView mShopCar;

    private String goodsId;

    private String isVIP;


    List<StoreGoodsBean.Specification> list = new ArrayList<>();
    List<StoreGoodsBean.Sku> listSku = new ArrayList<>();
    LinkedHashMap<String, String> mapData = new LinkedHashMap<>();
    Map<String,String> priceMap = new HashMap<>();

    @Override
    public int bindLayout() {
        return R.layout.store_goods_specification;
    }

    @Override
    public void initView(View view) {
        //设置视图背景透明
        translucentActivity(GoodsSpecificationActivity.this);

        isVIP = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_store_vip);

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
                        mShopCar.setBackgroundColor(getResources().getColor(R.color.color_6d));
                        mShopCar.setEnabled(false);
                    } else {
                        mShopCar.setBackgroundColor(getResources().getColor(R.color.color_fdb));
                        mShopCar.setEnabled(true);
                    }
                }
                String textPrice = "";
                if (!textVal.isEmpty()) {
                    textPrice = priceMap.get(textVal);
                }
                mType.setText("选择 " + textVal);
                mPrice.setText("¥" + textPrice);
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
                StoreGoodsBean goodsData = var1.getGoods();
                listSku.addAll(var1.getGoods().getSku());

                for (int i = 0;i < var1.getGoods().getSku().size();i++){
                    StoreGoodsBean.Sku sku = var1.getGoods().getSku().get(i);
                    if(isVIP.equals("true")){
                        //会员价
                        priceMap.put(sku.getAttriValStr(),String.valueOf(sku.getDiscount()));
                        if (goodsData.getDiscount() > 0) {
                            mPrice.setText("¥" + goodsData.getDiscount());
                        } else {
                            mPrice.setText("¥" + goodsData.getPrice());
                        }
                    }else{
                        //普通价
                        priceMap.put(sku.getAttriValStr(),String.valueOf(sku.getPrice()));
                        mPrice.setText("¥" + goodsData.getPrice());
                    }
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

    @OnClick({R.id.tv_hide, R.id.img_cancel, R.id.layout, R.id.bt_reduce, R.id.bt_add, R.id.bt_car, R.id.add_shop_car})
    public void OnclickView(View view) {
        switch (view.getId()) {
            case R.id.tv_hide:
            case R.id.img_cancel:
                finish();
                break;
            case R.id.layout:
                break;
            case R.id.bt_reduce:
                setGoodsNum(-1);
                break;
            case R.id.bt_add:
                setGoodsNum(1);
                break;
            case R.id.bt_car:
                //查看购物车
                if (!LoginUtil.isLogin(mContext)) {
                    return;
                }

                break;
            case R.id.add_shop_car:
                //新增购物车
                if (!LoginUtil.isLogin(mContext)) {
                    return;
                }
                addCarData();
                break;


        }
    }

    //购物车新增数据
    private void addCarData() {
        Map<String, Object> map = new HashMap<>();
        String attri = "";
        for (Map.Entry<String, String> entry : mapData.entrySet()) {
            if (attri.equals("")) {
                attri += entry.getValue();
            } else {
                attri += "-" + entry.getValue();
            }
        }

        map.put("attri", attri);
        map.put("num", mGoodsNum.getText().toString());
        YSBSdk.getService(OAuthService.class).addCarGoods(goodsId, map, new YRequestCallback<StoreGoodsBean>() {
            @Override
            public void onSuccess(StoreGoodsBean var1) {
                AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setTitle("操作提示");
                alert.setMessage("添加购物车成功");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alert.show();

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
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
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
