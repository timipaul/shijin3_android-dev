package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 卡片详情适配器 */
public class CardOrderAdapter extends BaseQuickAdapter<ShoppingShopBean, BaseViewHolder> {

    public onListen onListen;

    public CardOrderAdapter(int layoutResId, @Nullable List<ShoppingShopBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingShopBean item) {

        ImageView iv = helper.getView(R.id.goods_img);
        Glide.with(mContext).load(item.getGoods().getCoverImg()[0]).into(iv);
        helper.setText(R.id.goods_price,item.getGoods().getPrice()+"");
        helper.setText(R.id.goods_num,"数量 "+item.getGoods().getNum());
        helper.setText(R.id.goods_name,item.getGoods().getName());
        helper.setText(R.id.order_time,item.getGoods().getCreatedAt());


        Button delete = helper.getView(R.id.order_delete);
        Button comment = helper.getView(R.id.order_comment);
        Button logistics = helper.getView(R.id.order_logistics);



        comment.setVisibility(View.GONE);
        String type = item.getGoods().getState();
        String state_text = "";
        if(type.equals("WAITING-PAYMENT")){
            state_text = "未支付";
            delete.setVisibility(View.GONE);
            logistics.setVisibility(View.GONE);
        }else if(type.equals("WAITING-DELIVER-GOODS")){
            //待发货
            delete.setVisibility(View.GONE);
            state_text = "待发货";
            logistics.setVisibility(View.VISIBLE);
        }else if(type.equals("WAITING-RECEIVEING-GOODS")){
            //待收货
            delete.setVisibility(View.GONE);
            state_text = "待收货";
            logistics.setVisibility(View.VISIBLE);
        }else if(type.equals("END")){
            //已完成
            delete.setVisibility(View.VISIBLE);
            state_text = "已完成";
            logistics.setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.order_status,state_text);
        if(item.getGoods().getOut_trade_no().equals("")){
            logistics.setVisibility(View.GONE);
        }else{
            logistics.setVisibility(View.VISIBLE);
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.delCallback(item.get_id(),helper.getLayoutPosition());
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.commentCall(item.get_id());
            }
        });

        logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.showLogistice(item);
            }
        });
    }

    public interface onListen{
        void delCallback(String id,int position);
        void commentCall(String id);
        void showLogistice(ShoppingShopBean data);
    }

    public void setOnList(onListen onListen){
        this.onListen = onListen;
    }
}
