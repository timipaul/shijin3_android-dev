package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Copyright (C)
 * FileName: ZeroAdapter
 * Author: m1342
 * Date: 2019/5/15 16:28
 * Description: 零元购适配器
 */
public class ZeroAdapter extends BaseQuickAdapter<GoodsBean,BaseViewHolder> {

    protected LayoutInflater mInflater;

    public ZeroAdapter(int layoutResId, @Nullable List<GoodsBean> data){
        super(layoutResId, data);
    }

    public interface OnListen{
        void call(GoodsBean item);
    }
    public OnListen onListen;
    public void setOnListen(OnListen onListen){
        this.onListen=onListen;
    }


    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        ImageView img = helper.getView(R.id.img);
        GlideApp.with(mContext).load(item.getGoods_imgurl()).into(img);
        helper.setText(R.id.tv_name,item.getGoods_title());

        try {
            String str = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            str = String.format(mContext.getString(R.string.zero_buy_hint),item.getZero_shop_number()+"",sdf2.format(sdf.parse(item.getOver_time())));
            helper.setText(R.id.tv_exchange_aday,str);
        } catch (ParseException e) {
            helper.setText(R.id.tv_exchange_aday,"");
        }


        //helper.setText(R.id.tv_point,"¥0");
        helper.setText(R.id.tv_exchanged,String.format(mContext.getString(R.string.exchanged),item.getExchange_number()));

        if(item.getAble_exchange_number().equals("0")){
            helper.setText(R.id.tv_click, "已抢完");
            helper.setBackgroundRes(R.id.tv_click,R.mipmap.button_gray_bg);
        }else{

            helper.setText(R.id.tv_click, "抢购");
            helper.setBackgroundRes(R.id.tv_click,R.mipmap.button_orange_bg);
            helper.setOnClickListener(R.id.tv_click, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onListen!=null){
                        onListen.call(item);
                    }
                }
            });
        }

    }
}
