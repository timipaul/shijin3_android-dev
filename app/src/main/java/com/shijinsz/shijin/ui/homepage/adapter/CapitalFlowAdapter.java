package com.shijinsz.shijin.ui.homepage.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.InviteRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.TakeMoneyRecordBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**  账户流水 适配器 */
public class CapitalFlowAdapter extends BaseQuickAdapter<TakeMoneyRecordBean, BaseViewHolder> {


    public CapitalFlowAdapter(int layoutResId, @Nullable List<TakeMoneyRecordBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, TakeMoneyRecordBean item) {

        helper.setText(R.id.item_ranking,"" + helper.getLayoutPosition()+1);
        helper.setText(R.id.item_name,item.getCause());
        helper.setText(R.id.item_state,item.getState());
        helper.setText(R.id.item_change,item.getChange());
    }
}
