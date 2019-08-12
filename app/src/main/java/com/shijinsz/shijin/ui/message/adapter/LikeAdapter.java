package com.shijinsz.shijin.ui.message.adapter;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.model.model.bean.AdLikeBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/9.
 */

public class LikeAdapter extends BaseQuickAdapter<AdLikeBean.Messages,BaseViewHolder> {
    public LikeAdapter(int layoutResId, @Nullable List<AdLikeBean.Messages> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdLikeBean.Messages item) {
        if (item.isShow()) {
            helper.setGone(R.id.radio_button,true);
        }else {
            helper.setGone(R.id.radio_button,false);
        }
        ImageView radioButton = helper.getView(R.id.radio_button);
        if (item.isCheck()) {
            radioButton.setImageResource(R.mipmap.radio_button_on);
        }else {
            radioButton.setImageResource(R.mipmap.radio_button_off);
        }
        ImageView head = helper.getView(R.id.img_head);
        TextView title = helper.getView(R.id.tv_content);
        GlideApp.with(mContext).load(item.getUser().getImgurl()).placeholder(R.mipmap.ic_launcher).into(head);
        String srt=item.getUser().getNickname()+" "+mContext.getString(R.string.like_you_ad)+" 《"+item.getAd().getAd_title()+"》";
        SpannableStringBuilder style = new SpannableStringBuilder(srt);
        style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_6)),0,item.getUser().getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(style);
        String thisYear= TimeUtil.formatToYear(System.currentTimeMillis()/1000);
        String nowYear=TimeUtil.formatToYear(Long.valueOf(item.getCreated_at()));
        if (thisYear.equals(nowYear)){
            helper.setText(R.id.tv_time,TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"MM-dd HH:mm:ss"));
        }else {
            helper.setText(R.id.tv_time,TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"yyyy-MM-dd HH:mm:ss"));
        }
    }
}
