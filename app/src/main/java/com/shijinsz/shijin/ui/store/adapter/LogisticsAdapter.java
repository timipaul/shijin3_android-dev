package com.shijinsz.shijin.ui.store.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.LogisticBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.shijinsz.shijin.R;

import java.util.List;

public class LogisticsAdapter extends BaseQuickAdapter<LogisticBean.Accept, BaseViewHolder> {

    public CardOrderAdapter.onListen onListen;

    public LogisticsAdapter(int layoutResId, @Nullable List<LogisticBean.Accept> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticBean.Accept item) {

        String time = item.getAcceptTime();
        helper.setText(R.id.item_time,time.replace(" ","\n"));
        helper.setText(R.id.item_content,item.getAcceptStation());
    }

}
