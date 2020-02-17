package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.AdReleaseBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/8/14.
 */

public class DataPutAdapter extends BaseQuickAdapter<AdReleaseBean.Records,BaseViewHolder>{
    public DataPutAdapter(int layoutResId, @Nullable List<AdReleaseBean.Records> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdReleaseBean.Records item) {
            if (item.getAd().getReward_mode().equals("change")) {
                helper.setText(R.id.tv_look, item.getAd().getHistory_change());
                helper.setText(R.id.tv_click, item.getAd().getChange());
            }else {
                helper.setText(R.id.tv_look, item.getAd().getHistory_points());
                helper.setText(R.id.tv_click, item.getAd().getPoints());
            }
        helper.setText(R.id.tv_content,item.getAd().getAd_title());
        helper.setText(R.id.tv_time, item.getCreated_at());
    }
}
