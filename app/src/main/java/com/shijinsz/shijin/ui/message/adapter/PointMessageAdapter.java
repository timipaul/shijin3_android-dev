package com.shijinsz.shijin.ui.message.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.model.model.bean.PointBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/8/8.
 */

public class PointMessageAdapter extends BaseQuickAdapter<PointBean.Messages,BaseViewHolder> {

    public PointMessageAdapter(int layoutResId, @Nullable List<PointBean.Messages> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, PointBean.Messages item) {
        if (item.isShow()) {
            helper.setGone(R.id.radio_button,true);
        }else {
            helper.setGone(R.id.radio_button,false);
        }
        String thisYear=TimeUtil.formatToYear(System.currentTimeMillis()/1000);
        String nowYear=TimeUtil.formatToYear(Long.valueOf(item.getCreated_at()));
        if (thisYear.equals(nowYear)){
            helper.setText(R.id.tv_time,TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"MM-dd HH:mm:ss"));
        }else {
            helper.setText(R.id.tv_time,TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"yyyy-MM-dd HH:mm:ss"));
        }
        helper.setText(R.id.tv_title,item.getMessage_title());
        helper.setText(R.id.tv_content,item.getMessage_body());
        ImageView radioButton = helper.getView(R.id.radio_button);
        if (item.isCheck()) {
            radioButton.setImageResource(R.mipmap.radio_button_on);
        }else {
            radioButton.setImageResource(R.mipmap.radio_button_off);
        }
    }
}
