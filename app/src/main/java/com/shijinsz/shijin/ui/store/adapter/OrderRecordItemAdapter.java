package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 商城订单记录子目录 */
public class OrderRecordItemAdapter extends BaseQuickAdapter<StoreGoodsBean, BaseViewHolder> {

    public OrderRecordItemAdapter(int layoutResId, @Nullable List<StoreGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreGoodsBean item) {
        helper.setText(R.id.goods_name,item.getName());
        helper.setText(R.id.goods_price,"¥"+item.getPrice());
        ImageView iv = helper.getView(R.id.goods_img);
        Glide.with(mContext).load(item.getCoverImg()[0]).into(iv);
        helper.setText(R.id.goods_num,"x"+item.getNum());
        helper.setText(R.id.attriVal,item.getAttriValStr());

        String type = item.getState();
        String state_text = "";
        if(type.equals("WAITING-DELIVER-GOODS")){
            //待发货
            state_text = "待发货";
        }else if(type.equals("WAITING-RECEIVEING-GOODS")){
            //待收货
            state_text = "待收货";
        }else if(type.equals("END")){
            //已完成
            state_text = "已完成";
        }
        helper.setText(R.id.order_status,state_text);
    }
}
