package com.shijinsz.shijin.ui.activity.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/10/23.
 */

public class RankAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public RankAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item.equals("1")){
            helper.setBackgroundRes(R.id.tv_num,R.mipmap.icon_glod);
            helper.setText(R.id.tv_num,"");
        }else if (item.equals("2")){
            helper.setBackgroundRes(R.id.tv_num,R.mipmap.icon_silver);
            helper.setText(R.id.tv_num,"");
        }else if (item.equals("3")){
            helper.setBackgroundRes(R.id.tv_num,R.mipmap.icon_cu);
            helper.setText(R.id.tv_num,"");
        }else{
            helper.setBackgroundColor(R.id.tv_num,mContext.getResources().getColor(R.color.transparent));
            helper.setText(R.id.tv_num,item);
        }
    }
}
