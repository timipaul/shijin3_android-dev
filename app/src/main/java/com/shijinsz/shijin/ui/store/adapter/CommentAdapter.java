package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.StroeCommentBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 商城评论适配器 */
public class CommentAdapter extends BaseQuickAdapter<StroeCommentBean, BaseViewHolder> {

    public CommentAdapter(int layoutResId, @Nullable List<StroeCommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StroeCommentBean item) {

        Glide.with(mContext).load(item.getImg()).into((CircleImageView)helper.getView(R.id.item_img));
        helper.setText(R.id.item_user,item.getName());
        helper.setText(R.id.item_vip,"");
        helper.setText(R.id.item_time,item.getComment_time());
        helper.setText(R.id.item_measure,item.getGoods_measure());
        helper.setText(R.id.item_content,item.getContent());


    }
}
