package com.shijinsz.shijin.ui.wallet.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/29.
 */

public class PointAdapter extends BaseQuickAdapter<GoodsBean,BaseViewHolder> {
    public PointAdapter(int layoutResId, @Nullable List<GoodsBean> data) {
        super(layoutResId, data);
    }
    public interface OnListen{
        void call(int pos);
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
        helper.setText(R.id.tv_exchange_aday,String.format(mContext.getString(R.string.exchange_aday),item.getAllowable_exchange_number()));
        helper.setText(R.id.tv_exchanged,String.format(mContext.getString(R.string.exchanged),item.getExchange_number()));

        if(Integer.valueOf(item.getExchange_point()) == 0){
            helper.setText(R.id.tv_exchange,"领取");
            helper.setText(R.id.tv_point,"免费送");
        }else{
            helper.setText(R.id.tv_point,item.getExchange_point()+mContext.getString(R.string.point));
        }

        helper.setOnClickListener(R.id.tv_exchange, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListen!=null){
                    onListen.call(helper.getPosition());
                }
            }
        });
    }
}
