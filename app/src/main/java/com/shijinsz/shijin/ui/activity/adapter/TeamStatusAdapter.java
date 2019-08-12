package com.shijinsz.shijin.ui.activity.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by yrdan on 2018/10/24.
 */

public class TeamStatusAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public TeamStatusAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
