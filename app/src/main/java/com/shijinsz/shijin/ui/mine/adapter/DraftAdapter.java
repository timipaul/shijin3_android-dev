package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/14.
 */

public class DraftAdapter extends BaseQuickAdapter<Ads,BaseViewHolder> {
    public DraftAdapter(int layoutResId, @Nullable List<Ads> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Ads item) {
        ImageView img = helper.getView(R.id.img);
        GlideApp.with(mContext).load(item.getAd_title_pics().get(0)).into(img);
        helper.setText(R.id.tv_content,item.getAd_title());
        helper.setText(R.id.tv_time, TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"yyyy-MM-dd HH:mm"));
    }
}
