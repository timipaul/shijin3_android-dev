package com.shijinsz.shijin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.model.model.bean.AdReleaseBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/8/14.
 */

public class DataAdAdapter extends BaseQuickAdapter<AdReleaseBean.Records,BaseViewHolder>{
    public interface  OnClick{
        void callback(int pos);
    }
    public OnClick onClick;
    public void setOnClick(OnClick onClick){
        this.onClick=onClick;
    }
    public DataAdAdapter(int layoutResId, @Nullable List<AdReleaseBean.Records> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdReleaseBean.Records item) {
        ImageView arraw = helper.getView(R.id.arrow);
        if (item.isShow()){
            helper.setGone(R.id.ln_show,true);
            helper.setGone(R.id.tv_look,false);
            helper.setGone(R.id.tv_click,false);
            helper.setText(R.id.tv_liuran,item.getAd().getAd_exposure_number());
            helper.setText(R.id.tv_dianji,item.getAd().getAd_click_number());
            helper.setText(R.id.tv_zhuanfa,item.getAd().getAd_share_number());
            helper.setText(R.id.tv_shoucang,item.getAd().getAd_collection_number());
            helper.setText(R.id.tv_dianzan,item.getAd().getAd_like_number());
            helper.setText(R.id.tv_pinglun,item.getAd().getAd_comment_number());
            helper.setText(R.id.tv_dadui,item.getAd().getAnswer_right_number());
            helper.setText(R.id.tv_xie,item.getAd().getAnswer_number());
            arraw.setImageResource(R.mipmap.icon_down);

        }else {
            helper.setGone(R.id.ln_show,false);
            helper.setGone(R.id.tv_look,true);
            helper.setGone(R.id.tv_click,true);
            helper.setText(R.id.tv_look,item.getAd().getAd_exposure_number());
            helper.setText(R.id.tv_click,item.getAd().getAd_click_number());
            arraw.setImageResource(R.mipmap.icon_arraw);
        }
        helper.setText(R.id.tv_content,item.getAd().getAd_title());
        helper.setText(R.id.tv_time, TimeUtil.format(Long.valueOf(item.getCreated_at())*1000,"yyyy-MM-dd HH-mm"));
//        helper.setOnClickListener(R.id.ln_button, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onClick!=null)
//                    onClick.callback(helper.getPosition());
//            }
//        });
    }
}
