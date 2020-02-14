package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 商城订单记录 */
public class OrderRecordAdapter extends BaseQuickAdapter<ShoppingShopBean, BaseViewHolder> {

    public onListen onListen;

    public String state_type;

    public OrderRecordAdapter(int layoutResId, @Nullable List<ShoppingShopBean> data) {
        super(layoutResId, data);
    }

    public void setType (String type){
        state_type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingShopBean item) {

        helper.setText(R.id.store_name,item.getName());
        PowerfulRecyclerView recyclerView = helper.getView(R.id.recyclerView);
        OrderRecordItemAdapter adapter = new OrderRecordItemAdapter(R.layout.shop_order_goods_item,item.getGoodsList());
        recyclerView.setAdapter(adapter);
        Button delete = helper.getView(R.id.order_delete);
        Button comment = helper.getView(R.id.order_comment);
        Button logistics = helper.getView(R.id.order_logistics);
        Button pay = helper.getView(R.id.order_pay);

        if(state_type.equals("WAITING-PAYMENT")){
            //待支付
            pay.setVisibility(View.VISIBLE);
            comment.setVisibility(View.GONE);
            logistics.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }else if(state_type.equals("WAITING-DELIVER-GOODS")){
            //待发货
            logistics.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            pay.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }else if(state_type.equals("WAITING-RECEIVEING-GOODS")){
            //待收货
            logistics.setVisibility(View.VISIBLE);
            pay.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
        }else if(state_type.equals("END")){
            //已完成
            comment.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            pay.setVisibility(View.GONE);
            logistics.setVisibility(View.GONE);
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
