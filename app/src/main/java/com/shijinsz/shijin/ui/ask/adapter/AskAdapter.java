package com.shijinsz.shijin.ui.ask.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.home.adapter.NewsAdapter;

import java.util.List;

/**
 * Created by yrdan on 2018/8/29.
 */

public class AskAdapter extends BaseQuickAdapter<Ads, BaseViewHolder> {
    public interface OnCloseClickListen {
        void onClick(View view, int pos);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String key = "";

    public void setCloseClickListen(NewsAdapter.OnCloseClickListen closeClickListen) {
        CloseClickListen = closeClickListen;
    }

    public NewsAdapter.OnCloseClickListen CloseClickListen;

    public AskAdapter(int layoutResId, @Nullable List<Ads> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Ads item) {
        ImageView bigimg = helper.getView(R.id.img);
        bigimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        helper.setGone(R.id.im_play, true);
        helper.setGone(R.id.tv_red_type, false);
        helper.setText(R.id.tv_red_type, "广告");
        Glide.with(mContext).load(item.getAd_title_pics().get(0)).into(bigimg);
        if (item.getReward_mode().equals("change")) {
            helper.setText(R.id.tv_type, "现金");
        } else {
            helper.setText(R.id.tv_type, "金币");
        }
        helper.setText(R.id.title, item.getAd_title() + "");
        helper.setText(R.id.name, item.getRelease_record().getNickname() + "");
        helper.setText(R.id.time, item.getRelease_record().getRelease_time() + "");
        final View close = helper.getView(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CloseClickListen != null) {
                    CloseClickListen.onClick(close, helper.getPosition());
                }
            }
        });
    }
}
