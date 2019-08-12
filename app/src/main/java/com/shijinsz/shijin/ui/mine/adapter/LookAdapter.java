package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.AdReleaseBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/14.
 */

public class LookAdapter extends BaseQuickAdapter<AdReleaseBean.Records,BaseViewHolder> {
    public LookAdapter(int layoutResId, @Nullable List<AdReleaseBean.Records> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdReleaseBean.Records item) {
        ImageView img = helper.getView(R.id.img);
        GlideApp.with(mContext).load(item.getAd().getAd_title_pics().get(0)).into(img);
        helper.setText(R.id.tv_content,item.getAd().getAd_title());
        helper.setText(R.id.tv_time, item.getCreated_at());
    }
}
