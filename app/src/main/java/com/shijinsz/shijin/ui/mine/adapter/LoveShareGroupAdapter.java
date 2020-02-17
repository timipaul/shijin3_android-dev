package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.InviteRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.LoveShareGroupBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 爱分享 团队适配器 */
public class LoveShareGroupAdapter extends BaseQuickAdapter<LoveShareGroupBean.Subordinate.Child, BaseViewHolder> {

    public LoveShareGroupAdapter(int layoutResId, @Nullable List<LoveShareGroupBean.Subordinate.Child> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoveShareGroupBean.Subordinate.Child item) {
        ImageView iv = helper.getView(R.id.user_head);
        Glide.with(mContext).load(item.getAvatarUrl()).into(iv);
        helper.setText(R.id.username,item.getNickname());
        helper.setText(R.id.group_num,String.valueOf(item.getPeopleNum()));
        helper.setText(R.id.individual_num,String.valueOf(item.getSv()));
        helper.setText(R.id.item_group,String.valueOf(item.getGroupAchievement()));

    }
}
