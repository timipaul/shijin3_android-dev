package com.shijinsz.shijin.ui.ad.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.FatherCommentBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.mine.UserDetailActivity;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/24.
 */

public class FatherAdapter extends BaseQuickAdapter<FatherCommentBean,BaseViewHolder> {
    public FatherAdapter(int layoutResId, @Nullable List<FatherCommentBean> data) {
        super(layoutResId, data);
    }
    public interface OnListen{
        void call(int pos);
        void like(int pos);
        void fathercall(int pos);
        void fatharComment(int pos);
        void child(int pos,int index);
    }
    public OnListen onListen;
    public void setOnListen(OnListen onListen){
        this.onListen=onListen;
    }
    @Override
    protected void convert(BaseViewHolder helper, FatherCommentBean item) {
        CircleImageView img = helper.getView(R.id.img_head);
        TextView like=helper.getView(R.id.tv_givealike);
        GlideApp.with(mContext).load(item.getUser().getImageurl()).into(img);
        helper.setText(R.id.tv_nickname,item.getUser().getNickname());
        helper.setText(R.id.tv_time, TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"MM-dd HH:mm"));
//        helper.setText(R.id.tv_comment,item.getComment_number());
        helper.setText(R.id.tv_givealike,item.getLike_number());
        helper.setText(R.id.tv_content,item.getContent());
        if (item.getUser().getIs_advertiser().equals("on")){
            helper.setGone(R.id.img_vip_into,true);
        }else {
            helper.setGone(R.id.img_vip_into,false);
        }
        if (item.getComments().size()>0){
            helper.setGone(R.id.ln_comments,true);
            helper.setText(R.id.tv_total,String.format(mContext.getString(R.string.total_request),item.getComment_number()));
            if (item.getComments().size()>1){
                helper.setGone(R.id.tv_total,true);
            }else {
                helper.setGone(R.id.tv_total,false);
            }
            String srt=item.getComments().get(0).getUser().getNickname()+": "+item.getComments().get(0).getContent();
            SpannableStringBuilder style = new SpannableStringBuilder(srt);
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_6)),0,item.getComments().get(0).getUser().getNickname().length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_comments,style);
        }else {
            helper.setGone(R.id.ln_comments,false);
        }
        if (item.getIs_like().equals("on")){
            helper.setTextColor(R.id.tv_givealike,mContext.getResources().getColor(R.color.colorPrimary));
            like.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_givealike_2_clickonthe),null,null,null);
        }else {
            helper.setTextColor(R.id.tv_givealike,mContext.getResources().getColor(R.color.text_999999));
            like.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_givealike_2),null,null,null);
        }
        helper.setOnClickListener(R.id.ln_comments, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListen!=null){
                    onListen.child(helper.getPosition()-1,0);
                }
            }
        });
        helper.setOnClickListener(R.id.tv_comment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListen!=null){
                    onListen.fatharComment(helper.getPosition()-1);
                }
            }
        });
        helper.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListen!=null){
                    onListen.fathercall(helper.getPosition()-1);
                }
            }
        });
        helper.setOnClickListener(R.id.tv_givealike, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListen!=null){
                    onListen.like(helper.getPosition()-1);
                }
            }
        });
        helper.setOnClickListener(R.id.tv_total, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListen!=null){
                    onListen.call(helper.getPosition()-1);
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext,UserDetailActivity.class);
                intent.putExtra("id",item.getUser_id());
                mContext.startActivity(intent);
            }
        });


    }
}
