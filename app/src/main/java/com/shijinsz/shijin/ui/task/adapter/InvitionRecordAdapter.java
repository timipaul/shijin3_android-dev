package com.shijinsz.shijin.ui.task.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.InvitationBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InvitionRecordAdapter extends BaseQuickAdapter<InvitationBean,BaseViewHolder> {
    public InvitionRecordAdapter(int layoutResId, @Nullable List<InvitationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InvitationBean item) {
        helper.setText(R.id.tv_phone,item.getInvitee_username());
        helper.setText(R.id.tv_time,String.format(mContext.getString(R.string.registered_at),item.getCreated_at()));
        CircleImageView img = helper.getView(R.id.user_img);
        Glide.with(mContext).load(item.getUser_img()).into(img);
        helper.setText(R.id.tv_status,String.format(mContext.getString(R.string.invite_earnings),item.getInviter_reward()));


        if (item.getInvitation_status().equals("success")){
            helper.setText(R.id.tv_status,String.format(mContext.getString(R.string.invite_earnings),item.getInviter_reward()));
        }else {
            helper.setText(R.id.tv_status,"邀请中");
        }
    }
}
