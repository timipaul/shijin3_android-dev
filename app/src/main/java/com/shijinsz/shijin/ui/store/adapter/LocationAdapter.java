package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.UserSiteBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * 收货地址适配器
 */
public class LocationAdapter extends BaseQuickAdapter<UserSiteBean, BaseViewHolder> {

    public LocationAdapter(int layoutResId, @Nullable List<UserSiteBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserSiteBean item) {

        if(item.getAddress() == null){

            return;
        }

        if (item.isUse()) {
            helper.setText(R.id.item_head, "");
            helper.setGone(R.id.item_state, true);
            helper.setBackgroundRes(R.id.item_head, R.mipmap.default_site_ic);
        } else {

            helper.setText(R.id.item_head, item.getName().subSequence(0, 1));
            helper.setGone(R.id.item_state, false);
            helper.setBackgroundRes(R.id.item_head, R.drawable.shape_label_gray);
        }

        helper.setText(R.id.item_name, item.getName());
        helper.setText(R.id.item_phone, item.getPhone());


        int index = item.getAddress().indexOf("-");
        if (index > 0) {
            helper.setText(R.id.item_city, item.getAddress().subSequence(0, index));
            try {
                helper.setText(R.id.item_detail_site, item.getAddress().subSequence(index + 1, item.getAddress().length()));
            } catch (Exception e) {
                helper.setText(R.id.item_detail_site, "");
            }

        } else {
            helper.setText(R.id.item_city, item.getAddress());
        }


        TextView tv = helper.getView(R.id.item_compile);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.callback(helper.getLayoutPosition());
            }
        });

    }

    public interface onListen {
        void callback(int pos);
    }

    public void setOnListen(onListen onListen) {
        this.onListen = onListen;
    }

    public onListen onListen;
}
