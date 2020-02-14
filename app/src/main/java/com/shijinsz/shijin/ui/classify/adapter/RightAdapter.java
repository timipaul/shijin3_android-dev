package com.shijinsz.shijin.ui.classify.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.MyGridView;
import com.hongchuang.ysblibrary.model.model.bean.GoodsClassifyBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * 分类一级菜单适配器
 */
public class RightAdapter extends BaseQuickAdapter<GoodsClassifyBean.Child, BaseViewHolder> {

    RightMenuAdapter rightMenuAdapter;

    public RightAdapter(int layoutResId, @Nullable List<GoodsClassifyBean.Child> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsClassifyBean.Child item) {

        helper.setText(R.id.item_title, item.getChName());
        MyGridView gridView = helper.getView(R.id.grid_view);

        rightMenuAdapter = new RightMenuAdapter(mContext, item.getChild(), R.layout.item_right_child_goods_classify);
        gridView.setAdapter(rightMenuAdapter);
        rightMenuAdapter.setOnListen(new RightMenuAdapter.onListen() {
            @Override
            public void callback(GoodsClassifyBean.Child.ThreeChild pos) {
                onListen.callback(pos.get_id());
            }
        });


    }

    public interface onListen {
        void callback(String id);
    }

    public void setOnListen(onListen onListen) {
        this.onListen = onListen;
    }

    public onListen onListen;

}
