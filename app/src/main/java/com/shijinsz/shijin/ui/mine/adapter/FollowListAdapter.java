package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.FollowUserBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/21.
 */

public class FollowListAdapter extends BaseQuickAdapter<FollowUserBean,BaseViewHolder> {
    public interface OnFollow{
        void call(int pos);
    }
    public OnFollow onFollow;
    public void setOnFollow(OnFollow onFollow){
        this.onFollow=onFollow;
    }
    public FollowListAdapter(int layoutResId, @Nullable List<FollowUserBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, FollowUserBean item) {
        CircleImageView img = helper.getView(R.id.img_head);
        if(item.getIs_advertiser() == null){

        }else{
            if (item.getIs_advertiser().equals("on")) {
                helper.setGone(R.id.img_vip_into,true);
            }
            helper.setGone(R.id.img_vip_into,false);
        }

        GlideApp.with(mContext).load(item.getImageurl()).into(img);
        TextView tvFollow = helper.getView(R.id.tv_follow);
        if (item.getIs_follow().equals("on")){
            tvFollow.setText(mContext.getString(R.string.isfollow));
            tvFollow.setTextColor(mContext.getResources().getColor(R.color.text_999999));
            tvFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_isfollow),null,null,null);
            tvFollow.setBackgroundResource(R.drawable.bg_follow);
            tvFollow.setPadding(10,0,0,0);
        }else {
            tvFollow.setText(mContext.getString(R.string.user_like));
            tvFollow.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            tvFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_addfollow),null,null,null);
            tvFollow.setBackgroundResource(R.drawable.bg_addfollow);
            tvFollow.setPadding(24,0,0,0);
        }
        helper.setText(R.id.tv_nickname,item.getNickname());
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onFollow!=null){
                    onFollow.call(helper.getPosition());
                }
            }
        });
    }
}
