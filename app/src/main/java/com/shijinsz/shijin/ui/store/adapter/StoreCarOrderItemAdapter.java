package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**  确认订单 商品单个适配器 */
public class StoreCarOrderItemAdapter extends BaseQuickAdapter<StoreGoodsBean, BaseViewHolder> {

    public boolean vip_state;

    public StoreCarOrderItemAdapter(int layoutResId, @Nullable List<StoreGoodsBean> data) {
        super(layoutResId, data);
    }

    public void setIsVIP(boolean state){
        vip_state = state;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreGoodsBean item) {

        helper.setText(R.id.name_item,item.getName());
        if(item.getGoodsImg() != null){
            Glide.with(mContext).load(item.getGoodsImg()).into((ImageView) helper.getView(R.id.img_item));
        }else if(item.getCoverImg()[0] != null){
            Glide.with(mContext).load(item.getCoverImg()[0]).into((ImageView) helper.getView(R.id.img_item));
        }
        helper.setText(R.id.hint_item,item.getHintText());
        if(vip_state && item.getDiscount() > 0){
            helper.setText(R.id.goods_price,"¥"+item.getDiscount());
        }else {
            helper.setText(R.id.goods_price,"¥"+item.getPrice());
        }
        helper.setText(R.id.goods_num,"x"+item.getNum()+"");

    }
}
