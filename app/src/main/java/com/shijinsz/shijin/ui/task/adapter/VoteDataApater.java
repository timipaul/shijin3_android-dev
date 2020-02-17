package com.shijinsz.shijin.ui.task.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Copyright (C)
 * FileName: VoteDataApater
 * Author: m1342
 * Date: 2019/6/24 15:45
 * Description: 投票历史数据适配器
 */
public class VoteDataApater extends BaseQuickAdapter<VoteBean,BaseViewHolder> {

    public VoteDataApater(int layoutResId, @Nullable List<VoteBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoteBean item) {

        helper.setText(R.id.item_name,item.getTitle());
        helper.setText(R.id.item_end_date, item.getCreated_at());
        helper.setText(R.id.item_button,"已投票");

    }
}
