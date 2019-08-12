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
 * Created by yrdan on 2018/8/28.
 */

public class ChildAdapter extends BaseQuickAdapter<FatherCommentBean.Comments,BaseViewHolder> {
    public ChildAdapter(int layoutResId, @Nullable List<FatherCommentBean.Comments> data) {
        super(layoutResId, data);
    }


    public interface onShowListen{
        void onChilLike(int index);
        void onChilComment(int index);
    }
    public onShowListen onShowListen;
    public void setOnShowListen(onShowListen onShowListen){
        this.onShowListen=onShowListen;
    }

    @Override
    protected void convert(BaseViewHolder helper, FatherCommentBean.Comments item) {
        TextView like = helper.getView(R.id.tv_givealike);
        CircleImageView img = helper.getView(R.id.img_head);
        GlideApp.with(mContext).load(item.getUser().getImageurl()).into(img);
        helper.setText(R.id.tv_time, TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"MM-dd HH:mm"));
//        helper.setText(R.id.tv_comment,item.getComment_number());
        helper.setText(R.id.tv_givealike,item.getLike_number());
        if (item.getUser().getIs_advertiser().equals("on")){
            helper.setGone(R.id.img_vip_into,true);
        }else {
            helper.setGone(R.id.img_vip_into,false);
        }
        if (item.getIs_like().equals("on")){
            helper.setTextColor(R.id.tv_givealike,mContext.getResources().getColor(R.color.colorPrimary));
            like.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_givealike_3_clickonthe),null,null,null);
        }else {
            helper.setTextColor(R.id.tv_givealike,mContext.getResources().getColor(R.color.text_999999));
            like.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_givealike_5),null,null,null);
        }
        helper.setText(R.id.tv_nickname,item.getUser().getNickname());
        if (item.getFather_id()!=null){
            if (!item.getFather_id().isEmpty()){
                String str=mContext.getString(R.string.request_to)+"@"+item.getFather_nickname()+":"+item.getContent();
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue)),2,item.getFather_nickname().length()+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content,style);
            }else {
                helper.setText(R.id.tv_content,item.getContent());
            }
        }else{
            helper.setText(R.id.tv_content,item.getContent());
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, UserDetailActivity.class));
            }
        });
        helper.setOnClickListener(R.id.tv_comment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShowListen!=null){
                    onShowListen.onChilComment(helper.getPosition());
                }
            }
        });
        helper.setOnClickListener(R.id.tv_givealike, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShowListen!=null){
                    onShowListen.onChilLike(helper.getPosition());
                }
            }
        });
    }
}
