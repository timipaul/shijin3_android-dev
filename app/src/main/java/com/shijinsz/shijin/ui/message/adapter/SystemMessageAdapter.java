package com.shijinsz.shijin.ui.message.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.model.model.bean.SystemMessageBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;


/**
 * Created by yrdan on 2018/8/8.
 */

public class SystemMessageAdapter extends BaseQuickAdapter<SystemMessageBean.Circulars,BaseViewHolder> {
    public SystemMessageAdapter(int layoutResId, @Nullable List<SystemMessageBean.Circulars> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMessageBean.Circulars item) {
        ImageView img = helper.getView(R.id.img_title);
        GlideApp.with(mContext).load(item.getCircular_imgurl()).into(img);
        helper.setText(R.id.tv_title,item.getCircular_title());
        helper.setText(R.id.tv_content,item.getCircular_brief());
        helper.setText(R.id.tv_time, TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"yyyy-MM-dd"));

    }
}
