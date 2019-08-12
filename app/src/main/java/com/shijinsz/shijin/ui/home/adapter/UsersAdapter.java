package com.shijinsz.shijin.ui.home.adapter;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.SearchedBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.mine.adapter.FollowListAdapter;

import java.util.List;

/**
 * Created by yrdan on 2018/9/5.
 */

public class UsersAdapter extends BaseQuickAdapter<SearchedBean.Users,BaseViewHolder> {
    public UsersAdapter(int layoutResId, @Nullable List<SearchedBean.Users> data) {
        super(layoutResId, data);
    }
    public interface OnFollow{
        void call(int pos);
    }
    public FollowListAdapter.OnFollow onFollow;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String key="";
    public void setOnFollow(FollowListAdapter.OnFollow onFollow){
        this.onFollow=onFollow;
    }
    @Override
    protected void convert(BaseViewHolder helper, SearchedBean.Users item) {
        CircleImageView img = helper.getView(R.id.img_head);
        Glide.with(mContext).load(item.getImageurl()).into(img);
        TextView tvFollow = helper.getView(R.id.tv_follow);
        if (item.getIs_advertiser()!=null&&item.getIs_advertiser().equals("on")){
            helper.setGone(R.id.img_vip_into,true);
        }else{
            helper.setGone(R.id.img_vip_into,false);
        }
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
        helper.setText(R.id.tv_fansnum,item.getFan_number()+"粉丝");
        int index=item.getNickname().indexOf(key);
        String srt=item.getNickname();
        SpannableStringBuilder style = new SpannableStringBuilder(srt);
        style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),index,index+key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tv_nickname,style);
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
