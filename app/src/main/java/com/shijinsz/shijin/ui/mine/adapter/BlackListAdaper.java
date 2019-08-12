package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.dao.BlackListBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/8/7.
 */

public class BlackListAdaper extends BaseQuickAdapter<BlackListBean.Records,BaseViewHolder> {
    public interface onListen{
        void callback(int pos);
    }

    public void setOnListen(BlackListAdaper.onListen onListen) {
        this.onListen = onListen;
    }

    public onListen onListen;

    public BlackListAdaper(int layoutResId, @Nullable List<BlackListBean.Records> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, BlackListBean.Records item) {
        helper.setText(R.id.name,item.getBlack().getNickname());
        Glide.with(mContext).load(item.getBlack().getImgurl()).into((CircleImageView)helper.getView(R.id.im_header));
        TextView remove =helper.getView(R.id.tv_remove);
        if (item.getBlack().getIs_advertiser().equals("on")){
            helper.setGone(R.id.img_vip_into,true);
        }else {
            helper.setGone(R.id.img_vip_into,false);
        }
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListen!=null){
                    onListen.callback(helper.getPosition());
                }
            }
        });
    }
}
