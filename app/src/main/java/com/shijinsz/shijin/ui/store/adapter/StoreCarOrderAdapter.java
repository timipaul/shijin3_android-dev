package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**  确认订单 结算适配器 */
public class StoreCarOrderAdapter extends BaseQuickAdapter<ShoppingShopBean, BaseViewHolder> {

    public String isVIP;

    public StoreCarOrderAdapter(int layoutResId, @Nullable List<ShoppingShopBean> data) {
        super(layoutResId, data);
    }

    public void setIsVIP(String type){
        isVIP = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingShopBean item) {
        helper.setText(R.id.shop_name,item.getName());
        helper.setText(R.id.hint_num,String.format(mContext.getString(R.string.goods_num),item.getGoodsList().size()));
        double price = 0;
        for (int i = 0;i<item.getGoodsList().size();i++){
            if(isVIP.equals("true") && item.getGoodsList().get(i).getDiscount() > 0){
                price += Double.valueOf(item.getGoodsList().get(i).getDiscount() * item.getGoodsList().get(i).getNum());
            }else{
                price += Double.valueOf(item.getGoodsList().get(i).getPrice() * item.getGoodsList().get(i).getNum());
            }

        }
        if(item.getPostage_price() > 0){
            helper.setText(R.id.goods_postage,"邮费 " +item.getPostage_price() +"元");
            price += item.getPostage_price();
        }
        helper.setText(R.id.price_num,"" + price);
        PowerfulRecyclerView recycler =  helper.getView(R.id.recyclerView);
        StoreCarOrderItemAdapter adapter = new StoreCarOrderItemAdapter(R.layout.store_order_detailed_item,item.getGoodsList());
        adapter.setIsVIP(isVIP.equals("true"));
        recycler.setAdapter(adapter);


    }
}
