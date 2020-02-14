package com.shijinsz.shijin.ui.home.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;

import java.util.List;


/** 爱分享 首页列表适配器 */
public class LoveShareAdapter extends BaseQuickAdapter<StoreGoodsBean, BaseViewHolder> {

    public LoveShareAdapter(int layoutResId, @Nullable List<StoreGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreGoodsBean item) {
        ImageView iv = helper.getView(R.id.image);
        Glide.with(iv).load(item.getCoverImg()[0]).into(iv);
        helper.setText(R.id.title,item.getName());
        TextView tv = helper.getView(R.id.look_details);
        //String num = String.format(mContext.getString(R.string.share_period), 0);
        //helper.setText(R.id.share_num,num);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.onClickInto(item);
                tv.setTextColor(mContext.getResources().getColor(R.color.gray));
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setTextColor(mContext.getResources().getColor(R.color.gray));
                onListen.onClickInto(item);
            }
        });

    }

    public interface onListen{
        void onClickInto(StoreGoodsBean bean);
    }

    public onListen onListen;

    public void setOnListen(onListen onListen){
        this.onListen = onListen;
    }

}
