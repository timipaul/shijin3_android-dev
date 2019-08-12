package com.shijinsz.shijin.ui.message.adapter;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.model.model.bean.AdCommentBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/9.
 */

public class GetCommentAdapter extends BaseQuickAdapter<AdCommentBean.MessagesBean,BaseViewHolder> {
    private int type=1;
    private int request=1;
    public GetCommentAdapter(int layoutResId, @Nullable List<AdCommentBean.MessagesBean> data,int type) {
        super(layoutResId, data);
        this.type=type;
    }
    public interface OnRequestListen{
        void callback(int pos,int request);
    }
    public OnRequestListen onRequestListen;
    public void setOnRequestListen(OnRequestListen onRequestListen){
        this.onRequestListen=onRequestListen;
    }

    @Override
    protected void convert(final BaseViewHolder helper, AdCommentBean.MessagesBean item) {
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
        if (item.getUser().getIs_advertiser().equals("on")){
            helper.setGone(R.id.img_vip_into,true);
        }else {
            helper.setGone(R.id.img_vip_into,false);
        }
        ImageView head = helper.getView(R.id.img_head);
        TextView title = helper.getView(R.id.tv_name);
        GlideApp.with(mContext).load(item.getUser().getImgurl()).placeholder(R.mipmap.ic_launcher).into(head);
        if (type==1){
            helper.setGone(R.id.request,true);
            if (null!=item.getChild_comment()){
                if (item.getChild_comment().getId()!=null){
                    request=1;
                    String srt=item.getUser().getNickname()+" "+mContext.getString(R.string.request_you);
                    SpannableStringBuilder style = new SpannableStringBuilder(srt);
                    style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),0,item.getUser().getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    title.setText(style);
//                    title.setSingleLine(true);
                    helper.setText(R.id.tv_content,item.getChild_comment().getContent());
                }else {
                    request=2;
                    String tit=item.getAd().getAd_title();
                    if (item.getAd().getAd_title().length()>6){
                        tit=tit.substring(0,4)+".."+tit.substring(tit.length()-1,tit.length());
                    }
                    String srt=item.getUser().getNickname()+" "+mContext.getString(R.string.comment_you)+"《"+tit+"》";
                    SpannableStringBuilder style = new SpannableStringBuilder(srt);
                    style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),0,item.getUser().getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    title.setText(style);
//                    title.setSingleLine(true);
                    helper.setText(R.id.tv_content,item.getFather_comment().getContent());
                }
            }else {
                request=2;
                String tit=item.getAd().getAd_title();
                if (item.getAd().getAd_title().length()>6){
                    tit=tit.substring(0,4)+".."+tit.substring(tit.length()-1,tit.length());
                }
                String srt=item.getUser().getNickname()+" "+mContext.getString(R.string.comment_you)+"《"+tit+"》";
                SpannableStringBuilder style = new SpannableStringBuilder(srt);
                style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),0,item.getUser().getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                title.setText(style);
//                title.setSingleLine(true);
                helper.setText(R.id.tv_content,item.getFather_comment().getContent());
            }
        }else {
            helper.setGone(R.id.request,false);
            if (null!=item.getChild_comment()){
                if (item.getChild_comment().getId()!=null){
                    String srt="你 回复了 "+item.getFather_comment().getNickname()+" 的评论";
                    SpannableStringBuilder style = new SpannableStringBuilder(srt);
                    style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),6,item.getUser().getNickname().length()+6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    title.setText(style);
//                    title.setSingleLine(true);
                    helper.setText(R.id.tv_content,item.getChild_comment().getContent());
                }else {
                    String tit=item.getAd().getAd_title();
                    if (item.getAd().getAd_title().length()>6){
                        tit=tit.substring(0,4)+".."+tit.substring(tit.length()-1,tit.length());
                    }
                    String srt="你 评论了 "+item.getAd().getNickname()+" 的文章 《"+tit+"》";
                    SpannableStringBuilder style = new SpannableStringBuilder(srt);
                    style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),6,item.getAd().getNickname().length()+6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    title.setText(style);
//                    title.setSingleLine(true);
                    helper.setText(R.id.tv_content,item.getFather_comment().getContent());
                }
            }else {
                String tit=item.getAd().getAd_title();
                if (item.getAd().getAd_title().length()>6){
                    tit=tit.substring(0,4)+".."+tit.substring(tit.length()-1,tit.length());
                }
                String srt="你 评论了 "+item.getAd().getNickname()+" 的文章 《"+tit+"》";
                SpannableStringBuilder style = new SpannableStringBuilder(srt);
                style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_999999)),6,item.getAd().getNickname().length()+6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                title.setText(style);
//                title.setSingleLine(true);
                helper.setText(R.id.tv_content,item.getFather_comment().getContent());
            }
        }

        String thisYear= TimeUtil.formatToYear(System.currentTimeMillis()/1000);
        String nowYear=TimeUtil.formatToYear(Long.valueOf(item.getCreated_at()));
        if (thisYear.equals(nowYear)){
            helper.setText(R.id.tv_time,TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"MM-dd HH:mm"));
        }else {
            helper.setText(R.id.tv_time,TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"yyyy-MM-dd HH:mm"));
        }

        helper.setOnClickListener(R.id.request, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRequestListen!=null){
                    onRequestListen.callback(helper.getPosition(),request);
                }
            }
        });
    }
}
