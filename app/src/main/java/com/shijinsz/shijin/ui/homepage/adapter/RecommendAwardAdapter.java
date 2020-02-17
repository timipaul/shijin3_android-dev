package com.shijinsz.shijin.ui.homepage.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.InviteRecordBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 商城 团队人数 适配器 */
public class RecommendAwardAdapter extends BaseQuickAdapter<InviteRecordBean.Child, BaseViewHolder> {


    public RecommendAwardAdapter(int layoutResId, @Nullable List<InviteRecordBean.Child> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, InviteRecordBean.Child item) {

        helper.setText(R.id.item_ranking,""+helper.getLayoutPosition()+1);
        helper.setText(R.id.item_name,item.getNickname());
        helper.setText(R.id.item_state,item.getPrivilege());
    }
}
