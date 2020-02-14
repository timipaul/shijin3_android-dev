package com.shijinsz.shijin.ui.classify.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.GoodsClassifyBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 分类一级菜单适配器 */
public class LeftAdapter extends BaseQuickAdapter<GoodsClassifyBean, BaseViewHolder> {

    public LeftAdapter(int layoutResId, @Nullable List<GoodsClassifyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsClassifyBean item) {
        helper.setText(R.id.left_name,item.getChName());
        View layout = helper.getView(R.id.layout);
        //判断点击与不点击的背景
        if (item.isClick()) {
            helper.setTextColor(R.id.left_name,mContext.getResources().getColor(R.color.color_fdb));
        } else {
            helper.setTextColor(R.id.left_name,mContext.getResources().getColor(R.color.black));
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.callback(item,helper.getLayoutPosition());
            }
        });


    }

    public interface onListen{
        void callback(GoodsClassifyBean pos,int index);
    }

    public void setOnListen(onListen onListen) {
        this.onListen = onListen;
    }

    public onListen onListen;

}
