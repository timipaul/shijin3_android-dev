package com.shijinsz.shijin.ui.wallet.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.PaymentBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/8/31.
 */

public class PaymentAdapter extends BaseQuickAdapter<PaymentBean,BaseViewHolder> {
    public PaymentAdapter(int layoutResId, @Nullable List<PaymentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PaymentBean item) {
        helper.setText(R.id.tv_money, "¥" + item.getChange());
        helper.setText(R.id.tv_time, item.getCreated_at());
        TextView tv = helper.getView(R.id.tv_status);
        if (item.getStatus().equals("on")) {
            tv.setText(mContext.getString(R.string.withdraw_success));
            tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_whitdraw_success), null, null, null);
        }else if (item.getStatus().equals("pending")){
            tv.setText(mContext.getString(R.string.processing));
            tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_withdraw_process), null, null, null);
        }else if (item.getStatus().equals("back")){
            tv.setText(mContext.getString(R.string.process_fail));
            tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_withdraw_fail), null, null, null);
        }else {
            tv.setText(mContext.getString(R.string.withdraw_fail));
            tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_withdraw_fail), null, null, null);
        }
    }
}
