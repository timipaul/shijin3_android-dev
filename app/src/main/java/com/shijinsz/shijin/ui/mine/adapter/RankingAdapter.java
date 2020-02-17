package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.UserRankingBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 排行适配器 */
public class RankingAdapter extends BaseQuickAdapter<UserRankingBean.Ranking, BaseViewHolder> {

    public RankingAdapter(int layoutResId, @Nullable List<UserRankingBean.Ranking> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserRankingBean.Ranking item) {

        Glide.with(mContext).load(item.getImgUrl()).into((CircleImageView)helper.getView(R.id.item_img));
        helper.setText(R.id.item_name,item.getNickname());
        helper.setText(R.id.item_index,String.valueOf(item.getIndex()));
        helper.setText(R.id.item_num,String.valueOf(item.getGoldCoin()));

    }
}
