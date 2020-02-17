package com.shijinsz.shijin.ui.ad.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.FatherCommentBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.mine.UserDetailActivity;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.LoginUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrdan on 2018/8/28.
 */

public class CommentAdatper extends BaseQuickAdapter<FatherCommentBean,BaseViewHolder> {
    public CommentAdatper(int layoutResId, @Nullable List<FatherCommentBean> data) {
        super(layoutResId, data);


    }
    private List<FatherCommentBean.Comments> list = new ArrayList<>();
    private ChildAdapter adapter;
    private PowerfulRecyclerView recyclerView;
    public interface onShowListen{
        void call(int pos);
        void callfathercall(int pos);
        void childcall(int pos,int index);
        void onlike(int pos);
        void onChilLike(int pod,int index);
        void onComment(int pos);
        void onChilComment(int pos,int index);
    }
    public onShowListen onShowListen;
    public void setOnShowListen(onShowListen onShowListen){
        this.onShowListen=onShowListen;
    }
    @Override
    protected void convert(BaseViewHolder helper, FatherCommentBean item) {

        Log.w("VideoCommentAdapter","评论数据");

        TextView like=helper.getView(R.id.tv_givealike);
        int num=Integer.parseInt(item.getComment_number());
        Log.i("VideoCommentAdapter",num+"");
        ImageView img =helper.getView(R.id.img_more);
        CircleImageView imgHead=helper.getView(R.id.img_head);
        GlideApp.with(mContext).load(item.getUser().getImageurl()).into(imgHead);
        helper.setText(R.id.tv_time, TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"MM-dd HH:mm"));
//        helper.setText(R.id.tv_comment,item.getComment_number());
        helper.setText(R.id.tv_givealike,item.getLike_number());
        helper.setText(R.id.tv_content,item.getContent());
        helper.setText(R.id.tv_nickname,item.getUser().getNickname());

        Log.d("VideoCommentAdapter",item.getContent());

        if (item.getIs_like().equals("on")){
            helper.setTextColor(R.id.tv_givealike,mContext.getResources().getColor(R.color.colorPrimary));
            like.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_givealike_2_clickonthe),null,null,null);
        }else {
            helper.setTextColor(R.id.tv_givealike,mContext.getResources().getColor(R.color.text_999999));
            like.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_givealike_2),null,null,null);
        }
        if (item.getUser().getIs_advertiser().equals("on")){
            helper.setGone(R.id.img_vip_into,true);
        }else {
            helper.setGone(R.id.img_vip_into,false);
        }
        list = item.getComments();
        adapter=new ChildAdapter(R.layout.child_comment_item,list);
        helper.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShowListen!=null){
                    onShowListen.callfathercall(helper.getPosition());
                }
            }
        });
        helper.setOnClickListener(R.id.tv_comment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShowListen!=null){
                    onShowListen.onComment(helper.getPosition());
                }
            }
        });
        helper.setOnClickListener(R.id.tv_givealike, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShowListen!=null){
                    onShowListen.onlike(helper.getPosition());
                }
            }
        });
        adapter.setOnShowListen(new ChildAdapter.onShowListen() {
            @Override
            public void onChilLike(int index) {
                if (onShowListen!=null){
                    onShowListen.onChilLike(helper.getPosition(),index);
                }
            }

            @Override
            public void onChilComment(int index) {
                if (onShowListen!=null){
                    onShowListen.onChilComment(helper.getPosition(),index);
                }
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onShowListen!=null){
                    onShowListen.childcall(helper.getPosition(),position);
                }
            }
        });
        recyclerView=helper.getView(R.id.recyclerView);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adapter);
        if (list.size()==2&& num>2){
            helper.setGone(R.id.ln_comments,true);
            helper.setGone(R.id.ln_more,true);
            helper.setText(R.id.tv_total,String.format(mContext.getString(R.string.more_request1),num-2+""));
            img.setRotation(90);
        }else if (list.size()==2&& num==2){
            if (list.size()==0){
                helper.setGone(R.id.ln_comments,false);
            }else {
                helper.setGone(R.id.ln_comments,true);
            }
            helper.setGone(R.id.ln_more,false);
            helper.setText(R.id.tv_total,"");
            img.setRotation(90);
        }else if (list.size()<2){
            if (list.size()==0){
                helper.setGone(R.id.ln_comments,false);
            }else {
                helper.setGone(R.id.ln_comments,true);
            }
            helper.setGone(R.id.ln_more,false);
            helper.setText(R.id.tv_total,"");
            img.setRotation(90);
        }else if (list.size()<num){
            helper.setGone(R.id.ln_comments,true);
            helper.setGone(R.id.ln_more,true);
            helper.setText(R.id.tv_total,mContext.getString(R.string.more_request2));
            img.setRotation(90);
        }else{
            helper.setGone(R.id.ln_comments,true);
            helper.setGone(R.id.ln_more,true);
            helper.setText(R.id.tv_total,mContext.getString(R.string.gone_request));
            img.setRotation(-90);
        }
        helper.setOnClickListener(R.id.ln_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShowListen!=null){
                    onShowListen.call(helper.getPosition());
                }
            }
        });
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext,UserDetailActivity.class);
                intent.putExtra("id",item.getUser_id());
                mContext.startActivity(intent);
            }
        });

    }
}
